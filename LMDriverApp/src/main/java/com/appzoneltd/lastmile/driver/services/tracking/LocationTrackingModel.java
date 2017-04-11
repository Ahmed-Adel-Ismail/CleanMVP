package com.appzoneltd.lastmile.driver.services.tracking;

import android.location.Location;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.appzoneltd.lastmile.driver.R;
import com.base.abstraction.commands.Command;
import com.base.abstraction.logs.Logger;
import com.base.cached.PushNotificationTokens;
import com.base.cached.ServerMessage;
import com.base.cached.TrackingDetails;
import com.base.presentation.annotations.interfaces.JsonRequest;
import com.base.presentation.annotations.interfaces.Repository;
import com.base.presentation.locations.LocationTracking;
import com.base.presentation.locations.commands.LocationConverter;
import com.base.presentation.models.Model;
import com.base.presentation.references.Entity;
import com.base.presentation.references.Property;
import com.base.usecases.events.RequestMessage;
import com.base.usecases.events.SocketRequestMessage;
import com.base.usecases.requesters.server.websocket.SocketMessage;
import com.base.usecases.requesters.server.websocket.SocketMode;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import java.io.Serializable;

@Repository(LocationTrackingRepository.class)
class LocationTrackingModel extends Model {

    final Property<Location> currentLocation = new Property<>();

    @JsonRequest(R.id.requestOpenDriverLocationSocket)
    final Entity<SocketMessage> socketConnection = new Entity<>();

    @JsonRequest(R.id.requestSendDriverLocationSocketMessage)
    final Entity<SocketMessage> trackingDetailsUpdate = new Entity<>();


    public LocationTrackingModel() {

        socketConnection
                .onRequestMessage(createSocketOpenMessage())
                .onResponse(acceptOnlyIfOpened());

        trackingDetailsUpdate
                .onRequestMessage(createSocketMessage())
                .onRequest(sendTrackingDetailsIfLocationValid());

    }

    private Command<Entity.ResponseParam<SocketMessage>, ?> acceptOnlyIfOpened() {
        return new Command<Entity.ResponseParam<SocketMessage>, Void>() {
            @Override
            public Void execute(Entity.ResponseParam<SocketMessage> p) {
                if (p.responseMessage.isSuccessful()) {
                    handleSuccessResponse(p);
                } else {
                    clearCurrentSocket(p);
                }
                return null;
            }

            private void handleSuccessResponse(Entity.ResponseParam<SocketMessage> p) {
                SocketMessage socketMessage = p.responseMessage.getContent();
                if (SocketMode.OPEN.equals(socketMessage.getMode())) {
                    p.entity.set(socketMessage);
                } else if (SocketMode.CLOSE.equals(socketMessage.getMode())) {
                    p.entity.set(null);
                }
            }

            private void clearCurrentSocket(Entity.ResponseParam<SocketMessage> p) {
                ServerMessage serverMessage = p.responseMessage.getContent();
                String json = serverMessage.getMessage();

                if (json != null) {
                    handleSocketMessage(p, json);
                } else {
                    Logger.getInstance().error(LocationTracking.class, "null @ clearCurrentSocket()");
                }

            }

            private void handleSocketMessage(Entity.ResponseParam<SocketMessage> p, String json) {
                SocketMessage socketMessage = new Gson().fromJson(json, SocketMessage.class);
                if (SocketMode.OPEN.equals(socketMessage.getMode())
                        || SocketMode.CLOSE.equals(socketMessage.getMode())) {
                    p.entity.set(null);
                }
            }
        };
    }


    @NonNull
    private Command<Serializable, RequestMessage> createSocketOpenMessage() {
        return new Command<Serializable, RequestMessage>() {
            @Override
            public RequestMessage execute(Serializable parameter) {
                return new SocketRequestMessage.Builder(SocketMode.OPEN).build();
            }
        };
    }

    @NonNull
    private Command<Entity.RequestParam<SocketMessage>, Void> sendTrackingDetailsIfLocationValid() {
        return new Command<Entity.RequestParam<SocketMessage>, Void>() {
            @Override
            public Void execute(Entity.RequestParam<SocketMessage> params) {
                Location location = currentLocation.get();
                if (location != null) {
                    params.entity.requestFromRepository(createTrackingDetails(location));
                }
                return null;
            }


        };
    }

    @NonNull
    private TrackingDetails createTrackingDetails(Location location) {
        TrackingDetails trackingDetails = new TrackingDetails();
        trackingDetails.setLocation(new LocationConverter().execute(location));
        trackingDetails.setPushNotificationTokens(createPushNotificationTokens());
        return trackingDetails;
    }

    @NonNull
    private PushNotificationTokens createPushNotificationTokens() {
        String token = FirebaseInstanceId.getInstance().getToken();
        PushNotificationTokens pushNotificationTokens = new PushNotificationTokens();
        if (!TextUtils.isEmpty(token)) {
            pushNotificationTokens.setFirebaseToken(token);
        } else {
            Logger.getInstance().error(getClass(), "no push notification token saved to upload");
        }
        return pushNotificationTokens;
    }

    private Command<Serializable, RequestMessage> createSocketMessage() {
        return new Command<Serializable, RequestMessage>() {
            @Override
            public RequestMessage execute(Serializable parameter) {
                return new SocketRequestMessage.Builder(SocketMode.SEND)
                        .content(parameter)
                        .build();

            }
        };
    }

}
