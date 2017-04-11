package com.appzoneltd.lastmile.customer.features.tracking.model;

import android.support.annotation.NonNull;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.subfeatures.notificationcommands.CancelRequestCommand;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.abstraction.events.Event;
import com.base.abstraction.events.Message;
import com.base.abstraction.logs.Logger;
import com.base.abstraction.serializers.StringJsonParser;
import com.base.abstraction.system.App;
import com.base.abstraction.system.AppResources;
import com.base.cached.ServerMessage;
import com.base.presentation.annotations.interfaces.Sync;
import com.base.presentation.models.Model;
import com.base.presentation.references.BooleanProperty;
import com.base.presentation.references.Property;
import com.base.presentation.repos.base.Repository;
import com.base.presentation.subfeatures.RoutesService;
import com.base.usecases.events.RequestMessage;
import com.base.usecases.events.ResponseMessage;
import com.base.usecases.events.SocketRequestMessage;
import com.base.usecases.requesters.server.ssl.params.PathVariableGroup;
import com.base.usecases.requesters.server.websocket.SocketMessage;
import com.base.usecases.requesters.server.websocket.SocketMode;
import com.directions.route.Route;
import com.entities.Notification;
import com.entities.cached.PayloadActiveVehicleDetails;
import com.entities.cached.QueryModelResponse;
import com.entities.cached.Rating;
import com.entities.cached.ServerImage;
import com.entities.cached.TrackingLocation;
import com.entities.cached.WebSocketMessage;
import com.entities.requesters.QueryParams;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by Wafaa on 11/23/2016.
 */

public class TrackingModel extends Model {


    @Sync("vehicleDetails")
    public PayloadActiveVehicleDetails payloadActiveVehicleDetails;
    @Sync("destination")
    public LatLng destinationLatLng;
    @Sync("source")
    public LatLng pickupLocation;

    public final Property<Notification> notification = new Property<>();
    public final BooleanProperty webSocketOpened = new BooleanProperty();
    public final Property<Long> packageId = new Property<>(0L);
    public final Property<Rating> rating = new Property<>();
    public final Property<PublishSubject<TrackingLocation>> subject = new Property<>();

    private RoutesService routesService;
    private Disposable connectionUpdates;
    private TrackingLocation trackingLocation;
    private QueryModelResponse modelResponse;

    public TrackingModel() {
        connectionUpdates = resetConnectionUpdates();
    }

    private Disposable resetConnectionUpdates() {
        clearConnectionUpdates();
        PublishSubject<TrackingLocation> publishSubject = PublishSubject.create();
        return subject.set(publishSubject)
                .buffer(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.trampoline())
                .subscribe(notifyPresentationLayer(), logError(), closeSocket());
    }

    private void clearConnectionUpdates() {
        if (connectionUpdates != null) {
            if (!connectionUpdates.isDisposed()) {
                connectionUpdates.dispose();
            }
            connectionUpdates = null;
        }
    }

    @NonNull
    private Consumer<List<TrackingLocation>> notifyPresentationLayer() {
        return new Consumer<List<TrackingLocation>>() {
            @Override
            public void accept(List<TrackingLocation> locations) throws Exception {
                if (locations.size() >= 1) {
                    requestRoute(locations.get(locations.size() - 1));
                }
            }

            private void requestRoute(TrackingLocation location) {
                destinationLatLng = new LatLng(Double.parseDouble(location.getLatitude())
                        , Double.parseDouble(location.getLongitude()));
                startRouting();
            }

            private void startRouting() {
                String apiKey = AppResources.string(R.string.google_server_api_kay);
                routesService = new RoutesService(apiKey, notifyTrackingFragment(),
                        destinationLatLng, pickupLocation);
                routesService.start();
            }

        };
    }

    private Command<List<Route>, Void> notifyTrackingFragment() {
        return new Command<List<Route>, Void>() {
            @Override
            public Void execute(List<Route> routes) {
                Message message = new Message.Builder().content(routes).build();
                Event event = new Event.Builder(R.id.onRouting)
                        .message(message)
                        .receiverActorAddresses(R.id.addressTrackingFragment)
                        .build();
                App.getInstance().getActorSystem().send(event);
                return null;
            }
        };
    }

    @NonNull
    private Consumer<Throwable> logError() {
        return new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Logger.getInstance().error(TrackingModel.class, throwable.getMessage());
                connectionUpdates = resetConnectionUpdates();
            }
        };
    }

    @NonNull
    private Action closeSocket() {
        return new Action() {
            @Override
            public void run() throws Exception {
                Logger.getInstance().error(TrackingModel.class, "onComplete()");
                requestCloseWebSocket();
            }
        };
    }


    @NonNull
    @Override
    protected Repository createRepository() {
        return new TrackingRepository();
    }

    @NonNull
    @Override
    protected CommandExecutor<Long, Message> createOnViewsUpdatedCommands() {
        CommandExecutor<Long, Message> commandExecutor = new CommandExecutor<>();
        Command<Message, Void> command = createCancelRequestCommand();
        commandExecutor.put((long) R.id.requestCancelPickupRequest, command);
        command = createRequestSearchTopicCommand();
        commandExecutor.put((long) R.id.requestSearchTopic, command);
        command = createOnOpenWebSocketRequest();
        commandExecutor.put((long) R.id.requestOpenWebSocket, command);
        command = createOnCloseWebSocketRequest();
        commandExecutor.put((long) R.id.requestCloseWebSocket, command);
        command = createOnFindImagePathCommand();
        commandExecutor.put((long) R.id.requestFindImage, command);
        command = createRatingRequestCommand();
        commandExecutor.put((long) R.id.requestRating, command);
        return commandExecutor;
    }

    private Command<Message, Void> createCancelRequestCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message p) {
                Message message = new Message.Builder().content(packageId.get()).build();
                new CancelRequestCommand(TrackingModel.this).execute(message);
                return null;
            }
        };
    }

    private Command<Message, Void> createRequestSearchTopicCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message p) {
                requestSearchTopic();
                return null;
            }
        };
    }

    private void requestSearchTopic() {
        QueryParams queryParams = createQueryParams();
        RequestMessage message = new RequestMessage.Builder().content(queryParams).build();
        requestFromRepository(R.id.requestSearchTopic, message);

    }

    private QueryParams createQueryParams() {
        QueryParams queryParams = new QueryParams();
        queryParams.setVehicleId(payloadActiveVehicleDetails.getVehicleId());
        queryParams.setHubId(payloadActiveVehicleDetails.getHubId());
        queryParams.setDriverId(payloadActiveVehicleDetails.getDriverId());
        return queryParams;
    }

    private Command<Message, Void> createOnOpenWebSocketRequest() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                if (webSocketOpened.isTrue()) {
                    requestOpenSocket(modelResponse);
                }
                return null;
            }
        };
    }

    private Command<Message, Void> createOnCloseWebSocketRequest() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                SocketRequestMessage requestMessage =
                        new SocketRequestMessage.Builder(SocketMode.CLOSE)
                                .content(modelResponse.getPort()).build();
                requestFromRepository((long) R.id.requestCloseWebSocket, requestMessage);
                return null;
            }
        };
    }

    private Command<Message, Void> createOnFindImagePathCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                if (payloadActiveVehicleDetails != null) {
                    PathVariableGroup variableGroup = new PathVariableGroup();
                    variableGroup.add(payloadActiveVehicleDetails.getDriverImageId());
                    RequestMessage requestMessage = new RequestMessage.Builder()
                            .pathVariablesGroup(variableGroup).build();
                    requestFromRepository(R.id.requestFindImage, requestMessage);
                }
                return null;
            }
        };
    }

    private Command<Message, Void> createRatingRequestCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                RequestMessage requestMessage = new RequestMessage.Builder()
                        .content(rating.get()).build();
                requestFromRepository(R.id.requestRating, requestMessage);
                return null;
            }

        };
    }

    @NonNull
    @Override
    protected CommandExecutor<Long, ResponseMessage> createOnRepositoryUpdatedCommands() {
        CommandExecutor<Long, ResponseMessage> commandExecutor = new CommandExecutor<>();
        Command<ResponseMessage, Void> command = createOnCancelPickupRequestResponseCommand();
        commandExecutor.put((long) R.id.requestCancelPickupRequest, command);
        command = createOnSearchTopicResponseCommand();
        commandExecutor.put((long) R.id.requestSearchTopic, command);
        command = createOnWebSocketMessageCommand();
        commandExecutor.put((long) R.id.requestOpenWebSocket, command);
        command = createOnFindImageResponse();
        commandExecutor.put((long) R.id.requestFindImage, command);
        command = createOnRatingResponse();
        commandExecutor.put((long) R.id.requestRating, command);
        return commandExecutor;
    }

    private Command<ResponseMessage, Void> createOnCancelPickupRequestResponseCommand() {
        return new Command<ResponseMessage, Void>() {
            @Override
            public Void execute(ResponseMessage message) {
                notifyOnRepositoryResponse(message);
                return null;
            }
        };
    }

    private Command<ResponseMessage, Void> createOnSearchTopicResponseCommand() {
        return new Command<ResponseMessage, Void>() {
            @Override
            public Void execute(ResponseMessage message) {
                if (message.isSuccessful()) {
                    modelResponse = message.getContent();
                    requestOpenSocket(modelResponse);
                } else {
                    requestSearchTopic();
                }
                return null;
            }
        };
    }

    private void requestOpenSocket(QueryModelResponse modelResponse) {
        PathVariableGroup variableGroup = new PathVariableGroup();
        variableGroup.add(modelResponse.getServerId());
        variableGroup.add(modelResponse.getPort());
        SocketRequestMessage requestMessage = new SocketRequestMessage.Builder(SocketMode.OPEN)
                .content(modelResponse)
                .pathVariablesGroup(variableGroup)
                .build();
        requestFromRepository((long) R.id.requestOpenWebSocket, requestMessage);
    }

    public void requestCloseWebSocket() {
        if (modelResponse != null) {
            SocketRequestMessage requestMessage = new SocketRequestMessage.Builder(SocketMode.CLOSE)
                    .content(modelResponse.getPort()).build();
            requestFromRepository((long) R.id.requestOpenWebSocket, requestMessage);
        }
    }

    private Command<ResponseMessage, Void> createOnWebSocketMessageCommand() {
        return new Command<ResponseMessage, Void>() {
            @Override
            public Void execute(ResponseMessage message) {
                if (message.isSuccessful()) {
                    SocketMessage socketMessage = message.getContent();
                    if (SocketMode.RECEIVED.equals(socketMessage.getMode())) {
                        retrieveWebSocketMessageAndRequestDrawRoute(socketMessage);
                    }
                } else if (isErrorMessage(message)) {
                    Logger.getInstance().error(TrackingModel.class, message);
                    requestSearchTopic();
                } else {
                    notifyToShowToast();
                    requestCloseWebSocket();
                }
                notifyOnRepositoryResponse(message);
                return null;
            }

            private boolean isErrorMessage(ResponseMessage message) {
                Object content = message.getContent();
                return content instanceof ServerMessage
                        && (ServerMessage.MessageType.ERROR.equals(((ServerMessage) content)
                        .getMessageType()));
            }

            private void retrieveWebSocketMessageAndRequestDrawRoute(SocketMessage socketMessage) {
                WebSocketMessage webSocketMessage = new StringJsonParser<>(WebSocketMessage.class)
                        .execute(socketMessage.getContent());
                if (webSocketMessage != null) {
                    retrieveLocationAndRequestRoute(webSocketMessage);
                } else {
                    notifyToShowToast();
                }
            }

            private void retrieveLocationAndRequestRoute(WebSocketMessage webSocketMessage) {
                trackingLocation = webSocketMessage.getLocation();
                if (trackingLocation != null) {
                    destinationLatLng = new LatLng(Double.parseDouble(trackingLocation.getLatitude())
                            , Double.parseDouble(trackingLocation.getLongitude()));
                    subject.get().onNext(trackingLocation);
                } else {
                    Logger.getInstance().error(getClass().getClass(), "TrackingLocation is null");
                    notifyToShowToast();
                }
            }

            private void notifyToShowToast() {
                Event event = new Event.Builder(R.id.showToast).message(new Message.Builder()
                        .content(R.string.routing_error).build())
                        .receiverActorAddresses(R.id.addressActivity)
                        .build();
                App.getInstance().getActorSystem()
                        .get((long) R.id.addressActivity)
                        .execute(event);
            }

        };
    }

    private Command<ResponseMessage, Void> createOnFindImageResponse() {
        return new Command<ResponseMessage, Void>() {
            @Override
            public Void execute(ResponseMessage message) {
                if (message.isSuccessful()) {
                    ServerImage serverImage = message.getContent();
                    if (serverImage != null)
                        notifyOnRepositoryResponse(message);
                }
                return null;
            }
        };
    }

    private Command<ResponseMessage, Void> createOnRatingResponse() {
        return new Command<ResponseMessage, Void>() {
            @Override
            public Void execute(ResponseMessage message) {
                notifyOnRepositoryResponse(message);
                return null;
            }
        };
    }

    @Override
    public void onClear() {
        clearConnectionUpdates();
    }


}
