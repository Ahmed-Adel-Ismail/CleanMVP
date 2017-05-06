package com.base.presentation.notifications;

import com.base.abstraction.commands.Command;
import com.base.abstraction.concurrency.Future;
import com.base.abstraction.events.Event;
import com.base.abstraction.exceptions.TestException;
import com.base.abstraction.interfaces.Clearable;
import com.base.abstraction.logs.Logger;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * a class that holds the implementation of {@link FirebaseInstanceIdService#onTokenRefresh()}
 * <p>
 * Created by Ahmed Adel on 11/16/2016.
 */
public class OnFirebaseTokenRefresh implements Command<String, Future<Event>>, Clearable {


    private PushNotificationsTokenUploader uploader;

    @Override
    public Future<Event> execute(String logTag) {
        String token = FirebaseInstanceId.getInstance().getToken();
        Logger.getInstance().error(logTag, "token : " + token);
        if (token != null) {
            return uploadToken(token);
        } else {
            return updateResultAndLogFailure();
        }
    }

    private Future<Event> updateResultAndLogFailure() {
        Future<Event> future = new Future<>();
        future.setResult(null);
        new TestException().execute(new UnsupportedOperationException("invalid token received"));
        return future;
    }

    private Future<Event> uploadToken(String token) {
        uploader = new PushNotificationsTokenUploader();
        return uploader.execute(token);
    }

    @Override
    public void clear() {
        if (uploader != null) {
            uploader.clear();
            uploader = null;
        }
    }
}
