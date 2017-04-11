package com.appzoneltd.lastmile.driver.features.main.host;

import com.appzoneltd.lastmile.driver.R;
import com.appzoneltd.lastmile.driver.features.main.model.MainModel;
import com.base.abstraction.annotations.interfaces.Executable;
import com.base.presentation.base.presentation.PresenterResponsesHandler;
import com.base.usecases.events.ResponseMessage;

class MainPresenterResponsesHandler extends
        PresenterResponsesHandler<MainPresenter, MainViewModel, MainModel> {

    @Executable({
            R.id.requestSubmitDriverAcceptedOnDemandPickup,
            R.id.requestSubmitDriverRejectedOnDemandPickup})
    void onDemandPickupResponse(ResponseMessage message) {
        getViewModel().showRequestsProgressBar.set(false);
    }

}
