package com.base.presentation.notifications;

import android.text.TextUtils;

import com.base.abstraction.commands.Command;
import com.base.abstraction.concurrency.Future;
import com.base.abstraction.events.Event;
import com.base.abstraction.interfaces.Clearable;
import com.base.abstraction.logs.Logger;
import com.base.abstraction.system.Preferences;
import com.base.cached.PushNotificationTokens;
import com.base.presentation.repos.base.Repository;
import com.base.usecases.R;
import com.base.usecases.callbacks.Callback;
import com.base.usecases.events.RequestMessage;

/**
 * a class that refreshes the push notifications token with server
 * <p>
 * Created by Ahmed Adel on 11/14/2016.
 */
public class PushNotificationsTokenUploader implements
        Command<String, Future<Event>>,
        Clearable,
        Callback {

    private final Future<Event> future = new Future<>();
    private Repository repository;

    @Override
    public Future<Event> execute(String token) {
        if (!TextUtils.isEmpty(token) && Preferences.getInstance().hasKey(R.string.PREFS_KEY_TOKEN)) {
            uploadTokenToServer(token);
        } else {
            Logger.getInstance().error(getClass(),
                    "no token saved for the application yet ... login required");
            future.setResult(null);
        }
        return future;
    }

    private void uploadTokenToServer(String token) {
        Logger.getInstance().error(getClass(), "user logged in ... uploading token to server");
        Event event = createRequestEvent(token);
        repository = new PushNotificationsRepository();
        repository.initialize(this);
        Logger.getInstance().error(getClass(), "started uploading token : " + token);
        repository.execute(event);
    }


    private Event createRequestEvent(String token) {

        PushNotificationTokens p = new PushNotificationTokens();
        p.setFirebaseToken(token);
        RequestMessage.Builder requestMessageBuilder = new RequestMessage.Builder();
        requestMessageBuilder.id(R.id.requestRegisterFirebaseToken);
        requestMessageBuilder.content(p);
        RequestMessage requestMessage = requestMessageBuilder.build();
        Event.Builder eventBuilder = new Event.Builder(R.id.requestRegisterFirebaseToken);
        return eventBuilder.message(requestMessage).build();
    }

    @Override
    public void onCallback(Event event) {
        Logger.getInstance().error(getClass(), "uploading token finished, response : " + event);
        future.setResult(event);
    }

    @Override
    public void clear() {
        if (repository != null) {
            repository.clear();
            repository = null;
        }

    }
}
