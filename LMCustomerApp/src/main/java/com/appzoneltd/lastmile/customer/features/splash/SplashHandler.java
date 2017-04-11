package com.appzoneltd.lastmile.customer.features.splash;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.features.login.LoginActivity;
import com.appzoneltd.lastmile.customer.features.main.host.MainActivity;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.abstraction.events.Message;
import com.base.abstraction.system.Preferences;
import com.base.presentation.base.abstracts.features.ViewBinder;
import com.base.presentation.base.presentation.Presenter;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * A Default Implementation for Splash Screen that can be used in Application starting points
 * (Classes hosted by Activities marked as
 * {@link com.appzoneltd.lastmile.customer.interfaces.ActivitySplashable}
 * <p/>
 * Created by Ahmed Adel on 9/8/2016.
 */
class SplashHandler extends Presenter {

    private Disposable disposable;
    private ViewBinder viewBinder;

    SplashHandler(ViewBinder viewBinder) {
        super(viewBinder);
        this.viewBinder = viewBinder;
    }

    @Override
    protected CommandExecutor<Long, Message> createCommandExecutor() {
        return null;
    }

    public void start() {
        viewBinder.addEventsSubscriber(this);
        disposable = Observable.timer(2, TimeUnit.SECONDS).subscribe(onTimerComplete());
    }

    @NonNull
    private Consumer<Long> onTimerComplete() {
        return new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                startNextActivity();
            }
        };
    }

    private void startNextActivity() {
        if (Preferences.getInstance().hasKey(R.string.PREFS_KEY_TOKEN)) {
            startMainActivity(getHostActivity());
        } else {
            startLoginActivity(getHostActivity());
        }
    }

    private void startLoginActivity(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    private void startMainActivity(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    public int getContentView() {
        return R.layout.activity_splash;
    }

    @Override
    public void onDestroy() {
        if (disposable != null) {
            if (!disposable.isDisposed()) {
                disposable.dispose();
            }
            disposable = null;
        }
        viewBinder = null;

    }
}
