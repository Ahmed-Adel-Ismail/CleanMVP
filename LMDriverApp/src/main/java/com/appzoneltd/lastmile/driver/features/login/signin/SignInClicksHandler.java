package com.appzoneltd.lastmile.driver.features.login.signin;

import android.widget.Button;

import com.appzoneltd.lastmile.driver.R;
import com.appzoneltd.lastmile.driver.features.login.model.LoginModel;
import com.base.abstraction.annotations.interfaces.Executable;
import com.base.presentation.base.presentation.PresenterClicksHandler;
import com.base.presentation.validators.EmailValidator;
import com.base.presentation.validators.MobileValidator;
import com.base.presentation.validators.PasswordValidator;

/**
 * the {@link PresenterClicksHandler} for Sign in feature
 * <p>
 * Created by Ahmed Adel on 12/1/2016.
 */
class SignInClicksHandler extends PresenterClicksHandler
        <SignInPresenter, SignInViewModel, LoginModel> {

    @Executable(R.id.screen_login_sign_in_button)
    void signIn(Button button) {

        SignInViewModel viewModel = getViewModel();
        LoginModel model = getModel();
        viewModel.invalidUsername = !isValidUserName(model.username.get());
        viewModel.invalidPassword = !isValidPassword(model.password.get());
        if (!viewModel.invalidPassword && !viewModel.invalidUsername) {
            requestSignIn(viewModel);
        }
        invalidateViews();
    }

    private boolean isValidUserName(String username) {
        return new EmailValidator().execute(username) || new MobileValidator().execute(username);
    }

    private Boolean isValidPassword(String password) {
        return new PasswordValidator().execute(password);
    }


    private void requestSignIn(SignInViewModel viewModel) {
        viewModel.progress = true;
        getHostActivity().lockInteractions();
        getModel().loginToken.request();

    }
}