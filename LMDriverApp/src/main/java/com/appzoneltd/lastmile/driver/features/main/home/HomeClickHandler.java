package com.appzoneltd.lastmile.driver.features.main.home;

import android.view.View;

import com.appzoneltd.lastmile.driver.Flow;
import com.appzoneltd.lastmile.driver.R;
import com.appzoneltd.lastmile.driver.features.main.model.MainModel;
import com.base.abstraction.annotations.interfaces.Executable;
import com.base.abstraction.events.Event;
import com.base.abstraction.events.EventBuilder;
import com.base.presentation.exceptions.references.ConsumeException;
import com.base.abstraction.logs.Logger;
import com.base.abstraction.system.AppResources;
import com.base.presentation.base.presentation.PresenterClicksHandler;
import com.base.presentation.requests.ActionType;
import com.base.presentation.requests.ActivityActionRequest;

class HomeClickHandler extends PresenterClicksHandler<HomePresenter, HomeViewModel, MainModel> {

    @Executable(R.id.screen_home_floating_action_button_frame_layout)
    void onFloatingActionButtonClick(View view) {

        if (!getHostActivity().getSystemServices().isGPSProviderEnabled()) {
            showGpsRequiredError();
        }

        if (getModel().navigating.isTrue()) {
            stopNavigationAndStartPickupProcess();
        } else if (isNavigationStartingAvailable()) {
            requestPickupProcessId();
        } else {
            Logger.getInstance().error(getClass(), "pickup process ID requested");
        }
    }

    private void showGpsRequiredError() {
        getViewModel().snackBarText.set(AppResources.string(R.string.screen_home_gps_required));
        invalidateViews();
    }

    private void requestPickupProcessId() {
        getViewModel().progressBarVisible.set(true);
        invalidateViews();
        getModel().pickupProcessId.request();
    }

    private boolean isNavigationStartingAvailable() {
        return !getModel().pickupProcessId.isRequesting()
                && getModel().pickupProcessId.isEmpty();
    }

    private void stopNavigationAndStartPickupProcess() {
        getModel().navigating.set(false);
        updateViewModel();
        getViewModel().invalidateViews();
        startPickupProcessActivity();
    }

    private void startPickupProcessActivity() throws ConsumeException {

        ActivityActionRequest request = new ActivityActionRequest(ActionType.START_ACTIVITY);
        request.action(Flow.PickupProcessActivity.class);
        Event event = new EventBuilder(R.id.startActivityAction, request).execute(this);
        getHostActivity().startActivityActionRequest(event);
        getModel().pickupProcessId.set(null);
    }

    @Executable(R.id.screen_home_recenter_button)
    void onRecenterButtonClick(View view){
        getViewModel().reCenter();
    }
}
