package com.appzoneltd.lastmile.driver.features.pickup.verification;

import com.appzoneltd.lastmile.driver.R;
import com.appzoneltd.lastmile.driver.features.pickup.model.PickupProcessModel;
import com.base.abstraction.annotations.interfaces.Executable;
import com.base.presentation.base.presentation.PresenterResponsesHandler;
import com.base.usecases.events.ResponseMessage;

class VerificationResponsesHandler extends PresenterResponsesHandler
        <VerificationPresenter, VerificationViewModel, PickupProcessModel> {

    @Executable(R.id.onPickupProcessModelRequestsCompleted)
    void requestPackageDetails(ResponseMessage message) {
        if (message.isSuccessful()) {
            updateViewModel();
            invalidateViews();
        }
    }

    @Executable(R.id.requestVerifyPackageDetails)
    void requestVerifyPackageDetails(ResponseMessage responseMessage) {
        if (!responseMessage.isSuccessful()) {
            getViewModel().submitting.asFalse();
            getViewModel().invalidateViews();
        }

    }

    @Executable(R.id.requestPickupInvoice)
    void requestPickupInvoice(ResponseMessage responseMessage) {
        if (responseMessage.isSuccessful()) {
            getViewModel().submitting.asFalse();
            getViewModel().invalidateViews();
        }
    }

    @Executable(R.id.requestFindImage)
    void onServerImageReceived(ResponseMessage responseMessage) {
        if (responseMessage.isSuccessful()) {
            updateViewModel();
            invalidateViews();
        }
    }

}
