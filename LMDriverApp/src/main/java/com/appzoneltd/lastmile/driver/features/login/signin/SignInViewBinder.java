package com.appzoneltd.lastmile.driver.features.login.signin;

import android.view.View;

import com.appzoneltd.lastmile.driver.R;
import com.base.presentation.annotations.interfaces.BindLayout;
import com.base.presentation.annotations.interfaces.Presenter;
import com.base.presentation.base.abstracts.features.ViewBinder;

import butterknife.BindView;

/**
 * the {@link ViewBinder} for sign in feature
 * <p>
 * Created by Ahmed Adel on 11/23/2016.
 */
@BindLayout(R.layout.screen_login)
public class SignInViewBinder extends ViewBinder {

    @Presenter
    SignInPresenter signInPresenter;

    @BindView(R.id.screen_login_username_edit_text_layout)
    View usernameEditTextLayout;
    @BindView(R.id.screen_login_username_edit_text)
    View usernameEditText;
    @BindView(R.id.screen_login_password_edit_text_layout)
    View passwordEditTextLayout;
    @BindView(R.id.screen_login_password_edit_text)
    View passwordEditText;
    @BindView(R.id.screen_login_sign_in_button)
    View signInButton;
    @BindView(R.id.screen_login_forgot_password_text_view)
    View forgotPasswordTextView;
    @BindView(R.id.screen_login_create_account_text_view)
    View createAccountTextView;
    @BindView(R.id.screen_login_become_a_driver_button)
    View becomeADriverButton;
    @BindView(R.id.screen_login_main_progress_view)
    View mainProgressView;

}
