package com.base.presentation.notifications;

import android.support.annotation.NonNull;

import com.base.abstraction.api.usecases.RequestUrlLocator;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.abstraction.system.App;
import com.base.abstraction.system.Behaviors;
import com.base.entities.cached.ServerMessage;
import com.base.entities.mocked.MockedFailureServerMessage;
import com.base.entities.mocked.MockedSuccessServerMessage;
import com.base.abstraction.R;
import com.base.usecases.api.IntegrationHandler;
import com.base.usecases.events.RequestMessage;
import com.base.usecases.events.ResponseMessage;
import com.base.presentation.repos.base.Repository;
import com.base.presentation.repos.json.JsonRequest;
import com.base.presentation.repos.json.JsonResponse;
import com.base.usecases.requesters.base.EntityGateway;
import com.base.usecases.requesters.base.EntityRequester;
import com.base.usecases.requesters.server.mocks.MockRequester;
import com.base.usecases.requesters.server.mocks.MockedEntitiesRegistry;
import com.base.usecases.requesters.server.mocks.MockedEntity;
import com.base.usecases.requesters.server.ssl.AuthorizedHttpsRequester;
import com.google.gson.reflect.TypeToken;

import static com.base.usecases.events.ResponseMessage.HTTP_OK;

/**
 * a {@link Repository} for updating firebase related data with server
 * <p>
 * Created by Ahmed Adel on 11/14/2016.
 */
class PushNotificationsRepository extends Repository {

    private EntityGateway server;

    @Override
    public void preInitialize() {
        EntityRequester requester;
        if (App.getInstance().isBehaviorAccepted(Behaviors.MOCKING)) {
            requester = createCustomMockRequester();
        } else {
            requester = createAuthorizedRequester();
        }
        server = new EntityGateway(requester, this);

    }

    @NonNull
    private MockRequester createCustomMockRequester() {
        return new MockRequester() {
            {
                MockedEntitiesRegistry reg = new MockedEntitiesRegistry();
                MockedEntity mockedEntity = new MockedEntity();
                mockedEntity.setStatusCode(HTTP_OK);
                mockedEntity.setRequestId(R.id.requestRegisterFirebaseToken);
                mockedEntity.setSuccessResponse(new MockedSuccessServerMessage());
                mockedEntity.setErrorResponse(new MockedFailureServerMessage());
                reg.put((long) R.id.requestRegisterFirebaseToken, mockedEntity);
                putAllMockedEntitiesRegistry(reg);
            }
        };
    }

    @NonNull
    private EntityRequester createAuthorizedRequester() {
        IntegrationHandler useCasesApi = App.getInstance().getIntegrationHandler();
        RequestUrlLocator requestUrlLocator = useCasesApi.getHttpRequestUrlLocator();
        return new AuthorizedHttpsRequester(requestUrlLocator);
    }


    @NonNull
    @Override
    protected CommandExecutor<Long, RequestMessage> createOnRequestCommands() {
        CommandExecutor<Long, RequestMessage> commandExecutor = new CommandExecutor<>();
        commandExecutor.put((long) R.id.requestRegisterFirebaseToken, new JsonRequest(server));
        return commandExecutor;
    }

    @NonNull
    @Override
    protected CommandExecutor<Long, ResponseMessage> createOnResponseCommands() {
        CommandExecutor<Long, ResponseMessage> commandExecutor = new CommandExecutor<>();
        Command<ResponseMessage, Void> command = createOnRegisterFirebaseTokenResponse();
        commandExecutor.put((long) R.id.requestRegisterFirebaseToken, command);
        return commandExecutor;
    }

    private Command<ResponseMessage, Void> createOnRegisterFirebaseTokenResponse() {
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
        server.clear();
        server = null;
    }

    @Override
    public final long getActorAddress() {
        return R.id.addressRepositoryPushNotification;
    }


}
