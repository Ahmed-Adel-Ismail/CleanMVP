package com.appzoneltd.lastmile.customer.features.main.models;

import android.support.annotation.NonNull;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.requesters.SecureUrlLocator;
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
import com.base.usecases.requesters.server.ssl.AuthorizedHttpsRequester;
import com.google.gson.reflect.TypeToken;

/**
 * Created by Wafaa on 12/29/2016.
 */

@Address(R.id.addressMainRepository)
public class MainRepository extends Repository {

    @Mock(R.id.requestRating)
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
        commandExecutor.put((long) R.id.requestRating, serverPostRequestCommand);
        return commandExecutor;
    }

    @NonNull
    @Override
    protected CommandExecutor<Long, ResponseMessage> createOnResponseCommands() {
        CommandExecutor<Long, ResponseMessage> commandExecutor = new CommandExecutor<>();
        commandExecutor.put((long) R.id.requestRating, onRatingResponse());
        return commandExecutor;
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
    }

}
