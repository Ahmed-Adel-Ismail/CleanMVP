package com.base.presentation.failures;

import android.support.annotation.NonNull;

import com.base.abstraction.messaging.AbstractMailbox;
import com.base.abstraction.api.usecases.RequestUrlLocator;
import com.base.abstraction.events.Event;
import com.base.abstraction.exceptions.NotSavedInPreferencesException;
import com.base.abstraction.exceptions.failures.OAuth2Failure;
import com.base.abstraction.exceptions.failures.SessionExpiredFailure;
import com.base.abstraction.failures.FailureHandler;
import com.base.abstraction.logs.Logger;
import com.base.abstraction.serializers.JsonLoader;
import com.base.abstraction.serializers.JsonSaver;
import com.base.abstraction.system.App;
import com.base.entities.auth.AbstractRefreshToken;
import com.base.entities.cached.Token;
import com.base.presentation.repos.base.RepositoriesGroup;
import com.base.presentation.repos.base.Repository;
import com.base.abstraction.R;
import com.base.usecases.api.IntegrationHandler;
import com.base.usecases.callbacks.Callback;
import com.base.usecases.events.RequestEventBuilder;
import com.base.usecases.events.RequestEventBuilderParams;
import com.base.usecases.events.ResponseMessage;
import com.base.usecases.requesters.server.ssl.params.ParametersGenerator;


/**
 * a {@link FailureHandler} for the {@link OAuth2Failure}
 * <p>
 * Created by Ahmed Adel on 10/24/2016.
 */
public class OAuth2Handler extends FailureHandler implements Callback {

    private RequestUrlLocator requestUrlLocator;
    private AbstractMailbox<Event> clientMailbox;

    public OAuth2Handler() {
        IntegrationHandler useCasesApi = App.getInstance().getIntegrationHandler();
        this.requestUrlLocator = useCasesApi.getHttpRequestUrlLocator();
    }

    @Override
    public Void execute(Event event) {
        this.clientMailbox = event.getMessage().getContent();
        try {
            loadOldRefreshTokenThenRequestNewOne();
        } catch (NotSavedInPreferencesException e) {
            throw new SessionExpiredFailure("no saved refresh token ... new session required");
        }
        return null;
    }

    private void loadOldRefreshTokenThenRequestNewOne() {
        Event event = createRequestEvent();
        Repository repository = createRepository();
        repository.execute(event);
    }

    private Event createRequestEvent() throws NotSavedInPreferencesException {
        JsonLoader<Token> loader = new JsonLoader<>(R.string.PREFS_KEY_TOKEN);
        Token token = loader.execute(Token.class);
        IntegrationHandler useCasesApi = App.getInstance().getIntegrationHandler();
        AbstractRefreshToken refreshToken = useCasesApi.createRefreshableToken();
        refreshToken.setRefresh_token(token.getRefresh_token());
        RequestEventBuilderParams p = new RequestEventBuilderParams(R.id.requestRefreshToken);
        p.parametersGroup = new ParametersGenerator().execute(refreshToken);
        return new RequestEventBuilder().execute(p);
    }


    @NonNull
    private Repository createRepository() {
        Repository repository = new AuthRepository(requestUrlLocator);
        repository.initialize(this);
        return repository;
    }


    @Override
    public final void onCallback(Event event) {
        ResponseMessage responseMessage = event.getMessage();
        if (responseMessage.isSuccessful()) {
            saveNewTokenAndResumeClientMailbox(responseMessage);
        } else {
            throw new SessionExpiredFailure("authorization failed");
        }
    }

    private void saveNewTokenAndResumeClientMailbox(ResponseMessage responseMessage) {
        try {
            saveNewToken(responseMessage);
            resumeClientMailbox();
        } catch (NotSavedInPreferencesException e) {
            throw new SessionExpiredFailure("token received could not be saved");
        }
    }

    private void saveNewToken(ResponseMessage responseMessage) {
        JsonSaver<Token> saver = new JsonSaver<>(R.string.PREFS_KEY_TOKEN);
        saver.execute((Token) responseMessage.getContent());
    }

    private void resumeClientMailbox() {
        Event lastEvent = clientMailbox.getTaskHistory().poll();
        Logger.getInstance().error(getClass(), "repeating event after self-healing : " + lastEvent);
        clientMailbox.pause().push(lastEvent).resume();
        RepositoriesGroup.setHealingRepository(null);
    }

    @Override
    public long getActorAddress() {
        return R.id.addressAuthHandler;
    }
}
