package com.appzoneltd.lastmile.customer.firebase;

import android.support.annotation.NonNull;

import com.base.abstraction.commands.Command;
import com.base.abstraction.events.Event;
import com.base.abstraction.logs.Logger;
import com.base.presentation.notifications.OnFirebaseTokenRefresh;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * the {@link InstanceIdService} for the application
 * <p>
 * Created by Ahmed Adel on 11/14/2016.
 */
public class InstanceIdService extends FirebaseInstanceIdService {

    private static final String TAG = InstanceIdService.class.getSimpleName();
    private OnFirebaseTokenRefresh onFirebaseTokenRefresh;

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        onFirebaseTokenRefresh = new OnFirebaseTokenRefresh();
        onFirebaseTokenRefresh.execute(TAG).onComplete(onComplete());

    }

    @NonNull
    private Command<Event, Void> onComplete() {
        return new Command<Event, Void>() {
            @Override
            public Void execute(Event event) {
                Logger.getInstance().error(TAG, event);
                if (onFirebaseTokenRefresh != null) {
                    onFirebaseTokenRefresh.clear();
                    onFirebaseTokenRefresh = null;
                }
                return null;
            }
        };
    }


}
