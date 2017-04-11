package com.base.presentation.base.loaders;

import android.os.Bundle;

import com.base.presentation.base.abstracts.features.Feature;
import com.base.presentation.base.abstracts.features.ViewBinder;

/**
 * a {@link ViewBinder} for Splash screen
 * <p>
 * Created by Ahmed Adel on 11/22/2016.
 */
public abstract class AbstractSplashViewBinder extends ViewBinder implements
        SplashHandlerClient {

    private SplashHandler splashHandler;


    @Override
    public void initialize(Feature<?> feature) {
        super.initialize(feature);
        splashHandler = createSplashHandler();
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
