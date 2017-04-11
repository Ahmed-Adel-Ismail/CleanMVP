package com.appzoneltd.lastmile.driver.features.login.signin;

import android.content.Intent;

import com.appzoneltd.lastmile.driver.R;
import com.appzoneltd.lastmile.driver.features.login.model.LoginModel;
import com.appzoneltd.lastmile.driver.Flow;
import com.base.abstraction.annotations.interfaces.Executable;
import com.base.abstraction.events.Event;
import com.base.abstraction.events.EventBuilder;
import com.base.presentation.base.presentation.PresenterResponsesHandler;
import com.base.presentation.requests.ActionType;
import com.base.presentation.requests.ActivityActionRequest;
import com.base.usecases.events.ResponseMessage;

/**
 * the {@link PresenterResponsesHandler} for the sign in feature
 * <p>
 * Created by Ahmed Adel on 12/1/2016.
 */
class SignInResponsesHandler extends PresenterResponsesHandler
        <SignInPresenter, SignInViewModel, LoginModel> {

    @Executable(R.id.requestLogin)
    void login(ResponseMessage responseMessage) {

        getViewModel().progress = false;
        getHostActivity().unlockInteractions();

        if (responseMessage.isSuccessful()) {
            moveToMainActivity();
        } else {
            displayErrorMessages();
        }
    }

    private void moveToMainActivity() {
        invalidateViews();
        ActivityActionRequest request = new ActivityActionRequest(ActionType.START_ACTIVITY);
        request.action(Flow.MainActivity.class);
        request.flags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Event event = new EventBuilder(R.id.startActivityAction, request).execute(this);
        getFeature().startActivityActionRequest(event);
    }

    private void displayErrorMessages() {
        getViewModel().invalidPassword = true;
        getViewModel().invalidUsername = true;
        invalidateViews();
    }

}
