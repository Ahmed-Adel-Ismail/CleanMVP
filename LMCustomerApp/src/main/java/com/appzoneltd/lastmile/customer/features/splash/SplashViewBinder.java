package com.appzoneltd.lastmile.customer.features.splash;

import android.os.Bundle;

import com.appzoneltd.lastmile.customer.abstracts.LastMileViewBinder;
import com.base.presentation.base.abstracts.features.Feature;
import com.base.presentation.base.abstracts.features.ViewBinder;
import com.base.presentation.models.NullModel;

/**
 * The {@link ViewBinder} for Splash screen
 * <p/>
 * Created by Ahmed Adel on 9/8/2016.
 */
class SplashViewBinder extends LastMileViewBinder<NullModel> {

    private SplashHandler splashHandler;

    public SplashViewBinder(Feature<NullModel> feature) {
        super(feature);
        splashHandler = new SplashHandler(this);
    }

    @Override
    public void initializeAfterBindingViews(Bundle savedInstanceState) {
        splashHandler.start();
    }

    @Override
    public int getContentView() {
        return splashHandler.getContentView();
    }

    @Override
    public void onDestroy() {
        if (splashHandler != null) {
            splashHandler.onDestroy();
        }
        splashHandler = null;
        super.onDestroy();
    }
}
