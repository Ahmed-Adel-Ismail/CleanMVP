package com.appzoneltd.lastmile.driver.features.loaders;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.base.abstraction.commands.Command;
import com.base.abstraction.concurrency.Future;
import com.base.abstraction.events.Event;
import com.base.abstraction.logs.Logger;
import com.base.abstraction.system.App;
import com.base.presentation.base.loaders.AbstractLoaderViewBinder;
import com.base.presentation.base.loaders.SplashHandler;
import com.base.presentation.notifications.PushNotificationsTokenUploader;
import com.google.firebase.iid.FirebaseInstanceId;

/**
 * the {@link AbstractLoaderViewBinder} for this application
 * <p>
 * Created by Ahmed Adel on 11/22/2016.
 */
public class LoaderViewBinder extends AbstractLoaderViewBinder {

    @Override
    public void initializeAfterBindingViews(Bundle savedInstanceState) {
        String token = FirebaseInstanceId.getInstance().getToken();
        Future<Event> future = new PushNotificationsTokenUploader().execute(token);
        future.onComplete(onTokenUploaded());
    }

    @NonNull
    private Command<Event, Void> onTokenUploaded() {
        return new Command<Event, Void>() {
            @Override
            public Void execute(Event event) {
                Logger.getInstance().error(LoaderViewBinder.class,
                        "onTokenUploaded() : first attempt done");

                App app = App.getInstance();
                app.getAppLoader().setFirebaseTokenRefreshed(true);

                proceed();
                return null;
            }
        };
    }

    @Override
    public SplashHandler createSplashHandler() {
        return new LastMileSplashHandler(getHostActivity());
    }
}
