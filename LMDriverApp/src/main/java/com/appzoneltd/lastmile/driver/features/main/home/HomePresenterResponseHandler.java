package com.appzoneltd.lastmile.driver.features.main.home;

import com.appzoneltd.lastmile.driver.R;
import com.appzoneltd.lastmile.driver.features.main.model.MainModel;
import com.appzoneltd.lastmile.driver.subfeatures.ErrorMessage;
import com.base.abstraction.annotations.interfaces.Executable;
import com.base.presentation.base.presentation.PresenterResponsesHandler;
import com.base.usecases.events.ResponseMessage;


class HomePresenterResponseHandler extends
        PresenterResponsesHandler<HomePresenter, HomeViewModel, MainModel> {


    @Executable(R.id.requestDriverStartNavigation)
    void onPickupProcessIdResponse(ResponseMessage message) {
        getViewModel().progressBarVisible.asFalse();
        if (message.isSuccessful()) {
            startNavigation();
        } else {
            getViewModel().snackBarText.set(new ErrorMessage().execute(message));
        }
        invalidateViews();
    }

    private void startNavigation() {
        getModel().navigating.asTrue();
        updateViewModel();
    }


    @Executable(R.id.requestSubmitDriverAcceptedOnDemandPickup)
    void onDemandPickupResponse(ResponseMessage message) {
        if (!message.isSuccessful()) {
            getViewModel().snackBarText.set(new ErrorMessage().execute(message));
        }
    }


    @Executable({R.id.requestJobOrdersRoute , R.id.requestRouteEdges})
    void onRoutesResponse(ResponseMessage message) {
        invalidateViews();
    }

}
