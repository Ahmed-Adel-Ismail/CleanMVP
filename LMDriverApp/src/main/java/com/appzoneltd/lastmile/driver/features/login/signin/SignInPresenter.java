package com.appzoneltd.lastmile.driver.features.login.signin;

import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.appzoneltd.lastmile.driver.R;
import com.appzoneltd.lastmile.driver.features.login.model.LoginModel;
import com.base.abstraction.annotations.interfaces.Load;
import com.base.presentation.annotations.interfaces.OnClickHandler;
import com.base.presentation.base.presentation.Presenter;
import com.base.usecases.annotations.ResponsesHandler;

/**
 * the {@link Presenter} for the sign in feature
 * <p>
 * Created by Ahmed Adel on 12/1/2016.
 */
@Load
@OnClickHandler(SignInClicksHandler.class)
@ResponsesHandler(SignInResponsesHandler.class)
class SignInPresenter extends Presenter<SignInPresenter, SignInViewModel, LoginModel> {

    @Override
    public void initialize(SignInViewModel viewModel) {
        super.initialize(viewModel);
        getViewModel().onPasswordActionListener = createOnPasswordActionListener();
    }

    @NonNull
    private TextView.OnEditorActionListener createOnPasswordActionListener() {
        return new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                return handlePasswordAction(actionId);
            }
        };
    }

    private boolean handlePasswordAction(int actionId) {

        getHostActivity().getSystemServices().hideKeyboard();

        boolean handled = !getHostActivity().hasLockedInteractions()
                && (actionId == EditorInfo.IME_ACTION_DONE);

        if (handled) {
            getViewModel().invokeOnClick(R.id.screen_login_sign_in_button);
        }

        return handled;
    }


}
