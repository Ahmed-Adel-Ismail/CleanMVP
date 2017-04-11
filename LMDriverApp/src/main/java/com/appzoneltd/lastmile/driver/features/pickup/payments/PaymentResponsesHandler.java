package com.appzoneltd.lastmile.driver.features.pickup.payments;

import com.appzoneltd.lastmile.driver.R;
import com.appzoneltd.lastmile.driver.features.pickup.model.PickupProcessModel;
import com.base.abstraction.annotations.interfaces.Executable;
import com.base.presentation.base.presentation.PresenterResponsesHandler;
import com.base.usecases.events.ResponseMessage;

class PaymentResponsesHandler extends
        PresenterResponsesHandler<PaymentPresenter, PaymentViewModel, PickupProcessModel> {

    @Executable(R.id.requestPickupInvoice)
    void requestPackageDetails(ResponseMessage message) {
        if (message.isSuccessful()) {
            updateViewModel();
            invalidateViews();
        }
    }

    @Executable(R.id.requestConfirmPickupInvoice)
    void requestConfirmPickupInvoice(ResponseMessage message) {
        getViewModel().progress.asFalse();
    }
}

