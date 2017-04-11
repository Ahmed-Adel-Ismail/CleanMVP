package com.appzoneltd.lastmile.customer.features.notificationlist;

import android.support.annotation.NonNull;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.requesters.SecureUrlLocator;
import com.base.abstraction.annotations.interfaces.Address;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.cached.ServerMessage;
import com.base.usecases.annotations.Mock;
import com.base.usecases.events.RequestMessage;
import com.base.usecases.events.ResponseMessage;
import com.base.presentation.repos.base.Repository;
import com.base.presentation.repos.json.JsonRequest;
import com.base.presentation.repos.json.JsonResponse;
import com.base.usecases.requesters.base.EntityGateway;
import com.base.usecases.requesters.base.EntityRequester;
import com.base.usecases.requesters.server.ssl.AuthorizedHttpsRequester;
import com.entities.cached.ServerImage;
import com.google.gson.reflect.TypeToken;

/**
 * Created by Wafaa on 11/24/2016.
 */
@Address(R.id.addressNotificationGroupRepository)
public class NotificationListRepository extends Repository {


    @Mock(value = R.id.requestCancelPickupRequest, statusCode = 400)
    ServerMessage cancelServerMessage;
    @Mock(R.id.requestRating)
    ServerMessage RatingServerMessage;
    @Mock(R.id.requestTrackedRequest)
    ServerMessage serverMessage;

    private JsonRequest serverPostRequestCommand;

    @Override
    public void preInitialize() {
        EntityRequester requester = new AuthorizedHttpsRequester(new SecureUrlLocator());
        EntityGateway server = new EntityGateway(requester, this);
        serverPostRequestCommand = new JsonRequest(server);
    }

    @NonNull
    @Override
    protected CommandExecutor<Long, RequestMessage> createOnRequestCommands() {
        CommandExecutor<Long, RequestMessage> commandExecutor = new CommandExecutor<>();
        commandExecutor.put((long) R.id.requestCancelPickupRequest, serverPostRequestCommand);
        commandExecutor.put((long) R.id.requestRating, serverPostRequestCommand);
        commandExecutor.put((long) R.id.requestTrackedRequest, serverPostRequestCommand);
        return commandExecutor;
    }

    @NonNull
    @Override
    protected CommandExecutor<Long, ResponseMessage> createOnResponseCommands() {
        CommandExecutor<Long, ResponseMessage> commandExecutor = new CommandExecutor<>();
        commandExecutor.put((long) R.id.requestCancelPickupRequest, onCancelRequestResponse());
        commandExecutor.put((long) R.id.requestRating, onRatingRequestResponse());
        commandExecutor.put((long) R.id.requestTrackedRequest, onTrackedRequestResponse());
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

    private JsonResponse<ServerMessage> onRatingRequestResponse() {
        return new JsonResponse<ServerMessage>(this) {

            @Override
            protected TypeToken<ServerMessage> getTypeToken() {
                return new TypeToken<ServerMessage>() {
                };
            }
        };
    }

    private JsonResponse<ServerMessage> onTrackedRequestResponse() {
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
    }
}
