package com.appzoneltd.lastmile.customer.features.tracking.model;

import android.support.annotation.NonNull;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.requesters.SecureUrlLocator;
import com.appzoneltd.lastmile.customer.requesters.WebSocketsUrlLocator;
import com.base.abstraction.annotations.interfaces.Address;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.cached.ServerMessage;
import com.base.presentation.repos.base.Repository;
import com.base.presentation.repos.json.JsonRequest;
import com.base.presentation.repos.json.JsonResponse;
import com.base.usecases.annotations.Mock;
import com.base.usecases.events.RequestMessage;
import com.base.usecases.events.ResponseMessage;
import com.base.usecases.requesters.base.EntityGateway;
import com.base.usecases.requesters.base.EntityRequester;
import com.base.usecases.requesters.base.OpenEntityGateway;
import com.base.usecases.requesters.server.base.HttpMethod;
import com.base.usecases.requesters.server.ssl.AuthorizedHttpsRequester;
import com.base.usecases.requesters.server.websocket.SocketMessage;
import com.base.usecases.requesters.server.websocket.implementers.DynamicWebSocketRequester;
import com.entities.cached.QueryModelResponse;
import com.entities.cached.ServerImage;
import com.google.gson.reflect.TypeToken;

/**
 * Created by Wafaa on 11/23/2016.
 */
@Address(R.id.addressTrackingRepository)
class TrackingRepository extends Repository {

    @Mock(value = R.id.requestCancelPickupRequest)
    ServerMessage serverMessage;

    private JsonRequest serverPostRequestCommand;
    private JsonRequest webSocketRequestCommand;
    private JsonRequest serverGetRequestCommand;
    private EntityGateway postEntityGateway;
    private EntityGateway socketEntityGateway;
    private EntityGateway getEntityGateway;

    @Override
    public void preInitialize() {

        EntityRequester requester = new AuthorizedHttpsRequester(new SecureUrlLocator());
        postEntityGateway = new EntityGateway(requester, this);
        postEntityGateway.addConcurrentRequestId(R.id.requestSearchTopic);
        serverPostRequestCommand = new JsonRequest(postEntityGateway);

        EntityRequester webSocketRequester = new DynamicWebSocketRequester(new WebSocketsUrlLocator());
        socketEntityGateway = new OpenEntityGateway(webSocketRequester, this);
        socketEntityGateway.addConcurrentRequestId(R.id.requestOpenWebSocket);
        webSocketRequestCommand = new JsonRequest(socketEntityGateway);

        requester = new AuthorizedHttpsRequester(new SecureUrlLocator()).method(HttpMethod.GET);
        getEntityGateway = new EntityGateway(requester, this);
        serverGetRequestCommand = new JsonRequest(getEntityGateway);
    }

    @NonNull
    @Override
    protected CommandExecutor<Long, RequestMessage> createOnRequestCommands() {
        CommandExecutor<Long, RequestMessage> commandExecutor = new CommandExecutor<>();
        commandExecutor.put((long) R.id.requestCancelPickupRequest, serverPostRequestCommand);
        commandExecutor.put((long) R.id.requestSearchTopic, serverPostRequestCommand);
        commandExecutor.put((long) R.id.requestOpenWebSocket, webSocketRequestCommand);
        commandExecutor.put((long) R.id.requestCloseWebSocket, webSocketRequestCommand);
        commandExecutor.put((long) R.id.requestFindImage, serverGetRequestCommand);
        commandExecutor.put((long) R.id.requestRating, serverPostRequestCommand);
        return commandExecutor;
    }

    @NonNull
    @Override
    protected CommandExecutor<Long, ResponseMessage> createOnResponseCommands() {
        CommandExecutor<Long, ResponseMessage> commandExecutor = new CommandExecutor<>();
        commandExecutor.put((long) R.id.requestCancelPickupRequest, onCancelRequestResponse());
        commandExecutor.put((long) R.id.requestSearchTopic, onRequestSearchTopicResponse());
        commandExecutor.put((long) R.id.requestOpenWebSocket, onListenToPortResponse());
        commandExecutor.put((long) R.id.requestFindImage, onFindImageResponse());
        commandExecutor.put((long) R.id.requestRating, onRatingResponse());
        return commandExecutor;
    }

    private JsonResponse<ServerMessage> onCancelRequestResponse() {
        return new JsonResponse<ServerMessage>(this) {

            @Override
            protected TypeToken<ServerMessage> getTypeToken() {
                return new TypeToken<ServerMessage>() {
                };
            }
        };
    }

    private JsonResponse<QueryModelResponse> onRequestSearchTopicResponse() {
        return new JsonResponse<QueryModelResponse>(this) {

            @Override
            protected TypeToken<QueryModelResponse> getTypeToken() {
                return new TypeToken<QueryModelResponse>() {
                };
            }
        };
    }

    private JsonResponse<SocketMessage> onListenToPortResponse() {
        return new JsonResponse<SocketMessage>(this) {

            @Override
            protected TypeToken<SocketMessage> getTypeToken() {
                return new TypeToken<SocketMessage>() {
                };
            }
        };
    }

    private JsonResponse<ServerImage> onFindImageResponse() {
        return new JsonResponse<ServerImage>(this) {
            @Override
            protected TypeToken<ServerImage> getTypeToken() {
                return new TypeToken<ServerImage>() {
                };
            }
        };
    }

    private JsonResponse<ServerMessage> onRatingResponse() {
        return new JsonResponse<ServerMessage>(this) {

            @Override
            protected TypeToken<ServerMessage> getTypeToken() {
                return new TypeToken<ServerMessage>() {
                };
            }
        };
    }


    @Override
    public void onClear() {
        serverPostRequestCommand = null;
        webSocketRequestCommand = null;
        if (postEntityGateway != null) {
            postEntityGateway.clear();
            postEntityGateway = null;
        }
        if (socketEntityGateway != null) {
            socketEntityGateway.clear();
            socketEntityGateway = null;
        }
        if (getEntityGateway != null) {
            getEntityGateway.clear();
            getEntityGateway = null;
        }
    }
}
