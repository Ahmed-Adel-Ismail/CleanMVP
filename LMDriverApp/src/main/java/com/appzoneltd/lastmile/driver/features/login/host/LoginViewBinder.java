package com.appzoneltd.lastmile.driver.features.login.host;

import android.widget.FrameLayout;

import com.appzoneltd.lastmile.driver.R;
import com.base.presentation.annotations.interfaces.BindLayout;
import com.base.presentation.annotations.interfaces.Presenter;
import com.base.presentation.base.abstracts.features.ViewBinder;

import butterknife.BindView;

/**
 * the {@link ViewBinder} for the Login feature's host
 * <p>
 * Created by Ahmed Adel on 11/21/2016.
 */
@BindLayout(R.layout.screen_host)
public class LoginViewBinder extends ViewBinder {

    @Presenter
    LoginPresenter presenter;

    @BindView(R.id.screen_host_container_id)
    FrameLayout containerLayout;

}
