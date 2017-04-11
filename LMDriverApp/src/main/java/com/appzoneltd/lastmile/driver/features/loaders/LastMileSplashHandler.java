package com.appzoneltd.lastmile.driver.features.loaders;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.appzoneltd.lastmile.driver.R;
import com.appzoneltd.lastmile.driver.Flow;
import com.base.abstraction.references.CheckedReference;
import com.base.abstraction.system.Preferences;
import com.base.presentation.base.abstracts.features.AbstractActivity;
import com.base.presentation.base.loaders.SplashHandler;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * the {@link SplashHandler} implementer
 * <p>
 * Created by Ahmed Adel on 11/22/2016.
 */
class LastMileSplashHandler implements SplashHandler {

    private Disposable disposable;
    private final CheckedReference<AbstractActivity<?>> activityCheckedReference;

    LastMileSplashHandler(AbstractActivity<?> activity) {
        this.activityCheckedReference = new CheckedReference<AbstractActivity<?>>(activity);
    }

    @Override
    public void start() {
        disposable = Observable.timer(2, TimeUnit.SECONDS).subscribe(onTimerComplete());
    }

    @NonNull
    private Consumer<Long> onTimerComplete() {
        return new Consumer<Long>() {

            @Override
            public void accept(Long aLong) throws Exception {
                if (Preferences.getInstance().hasKey(R.string.PREFS_KEY_TOKEN)) {
                    startMainScreen();
                } else {
                    startLoginScreen();
                }
            }
        };
    }


    private void startMainScreen() {
        AbstractActivity<?> activity = activityCheckedReference.get();
        Intent intent = new Intent(activity, Flow.MainActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }


    private void startLoginScreen() {
        AbstractActivity<?> activity = activityCheckedReference.get();
        Intent intent = new Intent(activity, Flow.LoginActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }


    @Override
    public int getContentView() {
        return R.layout.screen_splash;
    }

    @Override
    public void onDestroy() {
        if (disposable != null) {
            if (!disposable.isDisposed()) {
                disposable.dispose();
            }
            disposable = null;
        }

    }
}
