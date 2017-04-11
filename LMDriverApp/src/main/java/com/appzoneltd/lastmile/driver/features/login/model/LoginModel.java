package com.appzoneltd.lastmile.driver.features.login.model;

import android.support.annotation.NonNull;

import com.appzoneltd.lastmile.driver.R;
import com.base.abstraction.commands.Command;
import com.base.abstraction.serializers.JsonSaver;
import com.base.cached.Token;
import com.base.presentation.annotations.interfaces.JsonRequest;
import com.base.presentation.annotations.interfaces.Repository;
import com.base.presentation.annotations.interfaces.Sync;
import com.base.presentation.models.Model;
import com.base.presentation.references.Entity;
import com.base.presentation.references.Property;
import com.base.usecases.events.RequestMessage;
import com.base.usecases.events.ResponseMessage;
import com.base.usecases.requesters.server.ssl.params.ParametersGenerator;
import com.base.usecases.requesters.server.ssl.params.ParametersGroup;
import com.entities.requesters.LoginRequest;

import java.io.Serializable;

/**
 * the {@link Model} for the login feature
 * <p>
 * Created by Ahmed Adel on 11/21/2016.
 */
@Repository(LoginRepository.class)
public class LoginModel extends Model {

    @Sync("username")
    public final Property<String> username = new Property<>();

    @Sync("password")
    public final Property<String> password = new Property<>();

    @JsonRequest(R.id.requestLogin)
    public final Entity<Token> loginToken = new Entity<>();

    public LoginModel() {
        loginToken
                .onRequestMessage(trimUserNameAndPassword())
                .onNext(saveToken())
                .onComplete(requestLoadingActivityOrders());
    }

    @NonNull
    private Command<Serializable, RequestMessage> trimUserNameAndPassword() {
        return new Command<Serializable, RequestMessage>() {
            @Override
            public RequestMessage execute(Serializable p) {
                LoginRequest loginRequest = new LoginRequest();
                loginRequest.setUsername(username.get().trim().toLowerCase());
                loginRequest.setPassword(password.get().trim());

                ParametersGenerator parametersGenerator = new ParametersGenerator();
                ParametersGroup parameters = parametersGenerator.execute(loginRequest);

                return new RequestMessage.Builder().parametersGroup(parameters).build();
            }
        };
    }

    @NonNull
    private Command<ResponseMessage, Boolean> saveToken() {
        return new Command<ResponseMessage, Boolean>() {
            @Override
            public Boolean execute(ResponseMessage responseMessage) {
                if (responseMessage.isSuccessful()) {
                    Token token = responseMessage.getContent();
                    new JsonSaver<Token>(R.string.PREFS_KEY_TOKEN).execute(token);
                }
                return true;
            }
        };
    }

    private Command<ResponseMessage, ?> requestLoadingActivityOrders() {
        return null;
    }

}
