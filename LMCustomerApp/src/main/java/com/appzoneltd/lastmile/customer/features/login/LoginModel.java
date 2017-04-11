package com.appzoneltd.lastmile.customer.features.login;

import android.support.annotation.NonNull;

import com.appzoneltd.lastmile.customer.R;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.abstraction.events.Event;
import com.base.abstraction.events.Message;
import com.base.abstraction.logs.Logger;
import com.base.abstraction.serializers.JsonSaver;
import com.base.abstraction.system.Preferences;
import com.base.presentation.models.Model;
import com.base.presentation.repos.base.Repository;
import com.base.usecases.events.RequestMessage;
import com.base.usecases.events.ResponseMessage;
import com.base.usecases.requesters.server.ssl.params.ParametersGenerator;
import com.base.usecases.requesters.server.ssl.params.ParametersGroup;
import com.entities.cached.AuthLogin;
import com.entities.cached.UserInfo;


/**
 * A Class for the login feature
 * <p>
 * Created by Wafaa on 10/19/2016.
 */

class LoginModel extends Model {

    private String userName;
    private String password;


    LoginModel() {

    }


    @NonNull
    @Override
    protected Repository createRepository() {
        return new LoginRepository();
    }

    @NonNull
    @Override
    protected CommandExecutor<Long, Message> createOnViewsUpdatedCommands() {
        CommandExecutor<Long, Message> commandExecutor = new CommandExecutor<>();
        Command<Message, Void> command = createRequestOnLoginCommand();
        commandExecutor.put((long) R.id.requestLogin, command);
        command = createOnRequestUserInfoCommand();
        commandExecutor.put((long) R.id.requestUserInfo, command);
        return commandExecutor;
    }

    private Command<Message, Void> createRequestOnLoginCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message p) {

                AuthLogin loginParams = new AuthLogin();
                loginParams.setUsername(userName);
                loginParams.setPassword(password);

                ParametersGenerator parametersGenerator = new ParametersGenerator();
                ParametersGroup parameters = parametersGenerator.execute(loginParams);

                RequestMessage requestMessage = new RequestMessage.Builder()
                        .parametersGroup(parameters).build();

                requestFromRepository(R.id.requestLogin, requestMessage);
                return null;
            }
        };
    }

    private Command<Message, Void> createOnRequestUserInfoCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message p) {
                requestFromRepository(R.id.requestUserInfo, new RequestMessage.Builder().build());
                return null;
            }
        };
    }


    @NonNull
    @Override
    protected CommandExecutor<Long, ResponseMessage> createOnRepositoryUpdatedCommands() {
        CommandExecutor<Long, ResponseMessage> commandExecutor = new CommandExecutor<>();
        Command<ResponseMessage, Void> command = createOnLoginResponseCommand();
        commandExecutor.put((long) R.id.requestLogin, command);
        command = createOnUserInfoResponseCommand();
        commandExecutor.put((long) R.id.requestUserInfo, command);
        return commandExecutor;
    }

    void setPassword(String password) {
        this.password = password;
    }

    void setUserName(String userName) {
        this.userName = userName;
    }


    private Command<ResponseMessage, Void> createOnLoginResponseCommand() {
        return new Command<ResponseMessage, Void>() {
            @Override
            public Void execute(ResponseMessage message) {
                if (message.isSuccessful()) {
                    requestUserInfoService();
                    new TokensHandler().execute(message).onComplete(onTokensHandled());
                }
                notifyOnRepositoryResponse(message);
                return null;
            }
        };
    }

    private void requestUserInfoService() {
        Event event = new Event.Builder(R.id.requestUserInfo).build();
        execute(event);
    }

    @NonNull
    private Command<Event, Void> onTokensHandled() {
        return new Command<Event, Void>() {
            @Override
            public Void execute(Event event) {
                Logger.getInstance().error(LoginModel.class, "onTokensHandled() : " + event);
                boolean success = isSuccessfulResponse(event);
                if (!success) {
                    Preferences.getInstance().remove(R.string.PREFS_KEY_TOKEN);
                }
                return null;
            }
        };

    }

    private boolean isSuccessfulResponse(Event event) {
        return event != null && ((ResponseMessage) event.getMessage()).isSuccessful();
    }

    private Command<ResponseMessage, Void> createOnUserInfoResponseCommand() {
        return new Command<ResponseMessage, Void>() {
            @Override
            public Void execute(ResponseMessage message) {
                if (message.isSuccessful()) {
                    saveUserInfo((UserInfo) message.getContent());
                }
                notifyOnRepositoryResponse(message);
                return null;
            }

            private void saveUserInfo(UserInfo userInfo) {
                new JsonSaver<UserInfo>(R.string.PREFS_KEY_USER_INFO).execute(userInfo);
            }
        };
    }

    @Override
    public void onClear() {
        userName = null;
        password = null;
    }
}
