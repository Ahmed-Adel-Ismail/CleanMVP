package com.appzoneltd.lastmile.customer.features.splash;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.abstracts.LastMileViewBinder;
import com.appzoneltd.lastmile.customer.interfaces.ActivitySplashable;
import com.appzoneltd.lastmile.customer.system.LastMileApp;
import com.base.abstraction.commands.Command;
import com.base.abstraction.concurrency.ExecutionThread;
import com.base.abstraction.concurrency.Future;
import com.base.abstraction.events.Event;
import com.base.presentation.base.abstracts.features.Feature;
import com.base.presentation.base.abstracts.features.ViewBinder;
import com.base.presentation.models.Model;
import com.base.presentation.notifications.PushNotificationsTokenUploader;
import com.google.firebase.iid.FirebaseInstanceId;

/**
 * A Class responsible for displaying the Loader / Splash
 * {@link ViewBinder} instead of the original
 * {@link ViewBinder} when needed
 * <p/>
 * Created by Ahmed Adel on 9/7/2016.
 */
public class LoaderViewBinder<T extends Model> extends LastMileViewBinder<T> {

    private SplashHandler splashHandler;

    public LoaderViewBinder(Feature<T> feature) {
        super(feature);
        this.splashHandler = new SplashHandler(this);
    }

    @Override
    public void initializeAfterBindingViews(Bundle savedInstanceState) {
        String token = FirebaseInstanceId.getInstance().getToken();
        Future<Event> future = new PushNotificationsTokenUploader().execute(token);
        future.onThread(ExecutionThread.MAIN).onComplete(onTokenUploaded());
    }

    @NonNull
    private Command<Event, Void> onTokenUploaded() {
        return new Command<Event, Void>() {
            @Override
            public Void execute(Event event) {
                LastMileApp app = LastMileApp.getInstance();
                app.getAppLoader().setFirebaseTokenRefreshed(true);
                if (getHostActivity() instanceof ActivitySplashable) {
                    splashHandler.start();
                } else {
                    restartCurrentActivity();
                }
                return null;
            }
        };
    }


    private void restartCurrentActivity() {
        getHostActivity().recreate();
    }

    @Override
    public int getContentView() {
        if (getHostActivity() instanceof ActivitySplashable) {
            return splashHandler.getContentView();
        } else {
            return R.layout.activity_splash;
        }

    }

    @Override
    public void onDestroy() {
        if (splashHandler != null) {
            splashHandler.onDestroy();
            splashHandler = null;
        }
        super.onDestroy();
    }
}
