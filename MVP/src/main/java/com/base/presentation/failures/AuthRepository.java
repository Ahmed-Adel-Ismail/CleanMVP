package com.base.presentation.failures;

import android.support.annotation.NonNull;

import com.base.abstraction.api.usecases.RequestUrlLocator;
import com.base.abstraction.commands.CommandWrapper;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.abstraction.exceptions.failures.SessionExpiredFailure;
import com.base.entities.cached.Token;
import com.base.presentation.repos.base.Repository;
import com.base.abstraction.R;
import com.base.usecases.events.RequestMessage;
import com.base.usecases.events.ResponseMessage;
import com.base.presentation.repos.json.JsonRequest;
import com.base.presentation.repos.json.JsonResponse;
import com.base.usecases.requesters.base.EntityGateway;
import com.base.usecases.requesters.base.EntityRequester;
import com.base.usecases.requesters.server.ssl.HttpsRequester;
import com.google.gson.reflect.TypeToken;

/**
 * a {@link Repository} for authentication
 * <p>
 * Created by Ahmed Adel on 10/24/2016.
 */
class AuthRepository extends Repository {

    private CommandWrapper<RequestMessage, Void> jsonRequestWrapper;

    @Override
    public void preInitialize() {
        this.jsonRequestWrapper = new CommandWrapper<>();
    }

    AuthRepository(@NonNull RequestUrlLocator requestUrlLocator) {
        JsonRequest jsonRequest = createJsonRequest(requestUrlLocator);
        jsonRequestWrapper.setCommand(jsonRequest);
    }

    private JsonRequest createJsonRequest(RequestUrlLocator requestUrlLocator) {
        EntityRequester requester = new HttpsRequester(requestUrlLocator);
        EntityGateway server = new EntityGateway(requester, this);
        return new JsonRequest(server);
    }

    @NonNull
    @Override
    protected CommandExecutor<Long, RequestMessage> createOnRequestCommands() {
        CommandExecutor<Long, RequestMessage> commandExecutor = new CommandExecutor<>();
        commandExecutor.put((long) R.id.requestRefreshToken, jsonRequestWrapper);
        return commandExecutor;
    }


    @NonNull
    @Override
    protected CommandExecutor<Long, ResponseMessage> createOnResponseCommands() {
        CommandExecutor<Long, ResponseMessage> commandExecutor = new CommandExecutor<>();
        commandExecutor.put((long) R.id.requestRefreshToken, onRefreshTokenResponse());
        return commandExecutor;
    }

    private JsonResponse<Token> onRefreshTokenResponse() {
        return new JsonResponse<Token>(this) {

            @Override
            protected ResponseMessage onResponseReceived(ResponseMessage responseMessage) {
                if (!responseMessage.isSuccessful()) {
                    throw new SessionExpiredFailure("failed to get token from server");
                }
                return responseMessage;
            }

            @Override
            protected TypeToken<Token> getTypeToken() {
                return new TypeToken<Token>() {
                };
            }
        };
    }

    @Override
    public void onClear() {
        jsonRequestWrapper = null;
    }

    @Override
    public final long getActorAddress() {
        return R.id.addressRepositoryAuth;
    }
}
