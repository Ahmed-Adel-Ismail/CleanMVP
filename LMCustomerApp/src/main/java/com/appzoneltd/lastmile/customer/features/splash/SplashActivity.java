package com.appzoneltd.lastmile.customer.features.splash;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.appzoneltd.lastmile.customer.abstracts.LastMileActivity;
import com.appzoneltd.lastmile.customer.interfaces.ActivitySplashable;
import com.base.presentation.annotations.interfaces.Splash;
import com.base.presentation.base.abstracts.features.ViewBinder;
import com.base.presentation.models.NullModel;

@Splash
public class SplashActivity extends LastMileActivity<NullModel> implements ActivitySplashable {


    @Override
    protected ViewBinder createLastMileViewBinder(Bundle savedInstanceState) {
        return new SplashViewBinder(this);
    }


    @NonNull
    @Override
    protected NullModel createModel() {
        NullModel model = new NullModel();
        model.initialize(this);
        return model;
    }
}
