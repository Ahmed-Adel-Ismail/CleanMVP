package com.appzoneltd.lastmile.driver.firebase;

import android.support.annotation.NonNull;

import com.base.abstraction.commands.Command;
import com.base.abstraction.concurrency.ExecutionThread;
import com.base.abstraction.events.Event;
import com.base.abstraction.logs.Logger;
import com.base.presentation.notifications.OnFirebaseTokenRefresh;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * a class that extends {@link FirebaseInstanceIdService} to manage FCM loginToken
 * <p>
 * Created by Ahmed Adel on 11/21/2016.
 */
public class InstanceIdService extends FirebaseInstanceIdService {

    private OnFirebaseTokenRefresh onFirebaseTokenRefresh;

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        onFirebaseTokenRefresh = new OnFirebaseTokenRefresh();
        onFirebaseTokenRefresh.execute(InstanceIdService.class.getSimpleName())
                .onThread(ExecutionThread.CURRENT).onComplete(onComplete());

    }

    @NonNull
    private Command<Event, Void> onComplete() {
        return new Command<Event, Void>() {
            @Override
            public Void execute(Event event) {
                Logger.getInstance().error(InstanceIdService.class, "received event : " + event);
                if (onFirebaseTokenRefresh != null) {
                    onFirebaseTokenRefresh.clear();
                    onFirebaseTokenRefresh = null;
                }
                return null;
            }
        };
    }

}
