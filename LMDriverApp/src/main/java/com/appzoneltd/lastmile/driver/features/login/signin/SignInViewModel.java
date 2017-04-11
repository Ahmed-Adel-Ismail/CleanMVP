package com.appzoneltd.lastmile.driver.features.login.signin;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appzoneltd.lastmile.driver.R;
import com.base.abstraction.annotations.interfaces.Executable;
import com.base.abstraction.system.AppResources;
import com.base.presentation.annotations.interfaces.EditTextWatcher;
import com.base.presentation.annotations.interfaces.Sync;
import com.base.presentation.base.presentation.ViewModel;
import com.base.presentation.base.presentation.WatcherViewModel;
import com.base.presentation.references.Property;

/**
 * the {@link ViewModel} for the Sign In screen
 * <p>
 * Created by Ahmed Adel on 12/1/2016.
 */
class SignInViewModel extends WatcherViewModel {

    @Sync("username")
    final Property<String> username = new Property<>();

    @Sync("password")
    final Property<String> password = new Property<>();

    boolean invalidUsername;
    boolean invalidPassword;
    boolean progress;

    TextView.OnEditorActionListener onPasswordActionListener;


    @Executable(R.id.screen_login_username_edit_text_layout)
    void usernameLayout(TextInputLayout view) {
        if (invalidUsername) {
            view.setError(AppResources.string(R.string.screen_login_username_not_correct));
        } else {
            view.setError(null);
        }
    }

    @EditTextWatcher(R.id.screen_login_username_edit_text)
    void usernameEditText(Editable editable) {
        username.set(editable.toString());
        invalidUsername = false;
        invalidateView(R.id.screen_login_username_edit_text_layout);
    }


    @Executable(R.id.screen_login_password_edit_text_layout)
    void passwordLayout(TextInputLayout view) {
        if (invalidPassword) {
            view.setError(AppResources.string(R.string.screen_login_password_not_correct));
        } else {
            view.setError(null);
        }
    }


    @EditTextWatcher(R.id.screen_login_password_edit_text)
    void passwordEditText(Editable editable) {
        password.set(editable.toString());
        invalidPassword = false;
        invalidateView(R.id.screen_login_password_edit_text_layout);
    }

    @Executable(R.id.screen_login_password_edit_text)
    void invalidatePasswordEditText(TextInputEditText editText) {
        editText.setOnEditorActionListener(onPasswordActionListener);
    }


    @Executable(R.id.screen_login_main_progress_view)
    void progress(ProgressBar progressBar) {
        progressBar.setVisibility((progress) ? View.VISIBLE : View.GONE);
    }


}