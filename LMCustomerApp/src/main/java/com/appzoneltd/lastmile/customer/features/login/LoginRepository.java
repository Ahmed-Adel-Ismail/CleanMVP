package com.appzoneltd.lastmile.customer.features.login;

import android.support.annotation.NonNull;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.requesters.SecureUrlLocator;
import com.base.abstraction.annotations.interfaces.Address;
import com.base.abstraction.commands.CommandWrapper;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.abstraction.serializers.JsonSaver;
import com.base.cached.Token;
import com.base.usecases.events.RequestMessage;
import com.base.usecases.events.ResponseMessage;
import com.base.presentation.repos.base.Repository;
import com.base.presentation.repos.json.JsonRequest;
import com.base.presentation.repos.json.JsonResponse;
import com.base.usecases.requesters.base.EntityGateway;
import com.base.usecases.requesters.server.ssl.HttpsRequester;
import com.base.usecases.requesters.server.ssl.headers.HttpHeaderAcceptedLanguage;
import com.base.usecases.requesters.server.ssl.headers.HttpHeaderAuthorizationToken;
import com.base.usecases.requesters.server.ssl.headers.HttpHeaderContentType;
import com.entities.cached.UserInfo;
import com.google.gson.reflect.TypeToken;

/**
 * The {@link Repository} for Login Feature
 * <p>
 * Created by Wafaa on 10/19/2016.
 */
@Address(R.id.addressLoginRepository)
public class LoginRepository extends Repository {


    private HttpsRequester requester;
    private EntityGateway server;
    private JsonRequest loginRequestCommand;
    private CommandWrapper<RequestMessage, Void> userInfoRequestCommand;

    @Override
    public void preInitialize() {
        requester = new HttpsRequester(new SecureUrlLocator());
        requester.header(new HttpHeaderAcceptedLanguage());
        server = new EntityGateway(requester, this);
        loginRequestCommand = new JsonRequest(server);
        userInfoRequestCommand = new CommandWrapper<>();
    }


    @NonNull
    @Override
    protected CommandExecutor<Long, RequestMessage> createOnRequestCommands() {
        CommandExecutor<Long, RequestMessage> commandExecutor = new CommandExecutor<>();
        commandExecutor.put((long) R.id.requestLogin, loginRequestCommand);
        commandExecutor.put((long) R.id.requestUserInfo, userInfoRequestCommand);
        return commandExecutor;
    }


    @NonNull
    @Override
    protected CommandExecutor<Long, ResponseMessage> createOnResponseCommands() {
        CommandExecutor<Long, ResponseMessage> commandExecutor = new CommandExecutor<>();
        commandExecutor.put((long) R.id.requestLogin, onLoginResponse());
        commandExecutor.put((long) R.id.requestUserInfo, onUserInfoResponse());
        return commandExecutor;
    }

    private JsonResponse<Token> onLoginResponse() {
        return new JsonResponse<Token>(this) {

            @Override
            protected ResponseMessage onResponseReceived(ResponseMessage msg) {
                if (msg.isSuccessful()) {
                    updateUserInfoRequestCommandDelegate(msg);
                }
                return msg;
            }

            private void updateUserInfoRequestCommandDelegate(ResponseMessage msg) {

                Token token = msg.getContent();
                new JsonSaver<Token>(R.string.PREFS_KEY_TOKEN).execute(token);

                requester.header(new HttpHeaderAuthorizationToken());
                requester.header(new HttpHeaderContentType());
                userInfoRequestCommand.setCommand(new JsonRequest(server));
            }


            @Override
            protected TypeToken<Token> getTypeToken() {
                return new TypeToken<Token>() {
                };
            }
        };
    }

    private JsonResponse<UserInfo> onUserInfoResponse() {
        return new JsonResponse<UserInfo>(this) {

            @Override
            protected TypeToken<UserInfo> getTypeToken() {
                return new TypeToken<UserInfo>() {
                };
            }
        };
    }

    @Override
    public void onClear() {
        loginRequestCommand = null;
        if (userInfoRequestCommand != null) {
            userInfoRequestCommand.clear();
            userInfoRequestCommand = null;
        }

    }


}
