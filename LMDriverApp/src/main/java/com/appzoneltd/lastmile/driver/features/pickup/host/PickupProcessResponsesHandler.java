package com.appzoneltd.lastmile.driver.features.pickup.host;

import com.appzoneltd.lastmile.driver.R;
import com.appzoneltd.lastmile.driver.features.pickup.model.PickupProcessModel;
import com.appzoneltd.lastmile.driver.features.pickup.states.StateVerifying;
import com.appzoneltd.lastmile.driver.subfeatures.ErrorMessage;
import com.base.abstraction.annotations.interfaces.Executable;
import com.base.abstraction.exceptions.references.states.StateIsMovingToNullException;
import com.base.presentation.base.presentation.PresenterResponsesHandler;
import com.base.usecases.events.ResponseMessage;

class PickupProcessResponsesHandler extends PresenterResponsesHandler
        <PickupProcessPresenter, PickupProcessViewModel, PickupProcessModel> {

    @Executable({
            R.id.requestAllPackageTypes,
            R.id.requestAllPackageLabels,
            R.id.requestAllCancellationReasons,
            R.id.requestPackageDetails})
    void requestAllPackageTypes(ResponseMessage message) {
        if (message.isSuccessful()) {
            getViewModel().loadingState.set(PickupProcessLoadingState.LOADING);
            getViewModel().progress.update();
        } else {
            getViewModel().loadingState.set(PickupProcessLoadingState.LOADING_ERROR);
        }
        invalidateViews();
    }


    @Executable(R.id.onPickupProcessModelRequestsCompleted)
    void onPickupProcessModelRequestsCompleted(ResponseMessage message) {
        if (message.isSuccessful()) {
            getViewModel().loadingState.set(PickupProcessLoadingState.IN_PROCESS);
            getViewModel().pickupState.set(new StateVerifying());
        } else {
            getViewModel().loadingState.set(PickupProcessLoadingState.LOADING_ERROR);
        }
        invalidateViews();
    }

    @Executable({R.id.requestFindImage, R.id.requestUploadImage})
    void onServerImageReceived(ResponseMessage responseMessage) {
        if (!responseMessage.isSuccessful()) {
            getViewModel().snackbarText.set(new ErrorMessage().execute(responseMessage));
            invalidateViews();
        }

    }

    @Executable(R.id.requestCancelRequest)
    void onCancelPickup(ResponseMessage responseMessage) {
        if (responseMessage.isSuccessful()) {
            getHostActivity().finish();
        } else {
            getViewModel().snackbarText.set(new ErrorMessage().execute(responseMessage));
            invalidateViews();
        }
    }

    @Executable({
            R.id.requestPickupInvoice,
            R.id.requestConfirmPickupInvoice,
            R.id.requestConfirmPackageDocuments})
    void onSubmitClicksResponses(ResponseMessage responseMessage) {
        if (responseMessage.isSuccessful()) {
            updatePickupProcessState();
        } else {
            getViewModel().snackbarText.set(new ErrorMessage().execute(responseMessage));
        }
        invalidateViews();
    }


    @Executable(R.id.requestVerifyPackageDetails)
    void onVerifyPackage(ResponseMessage responseMessage) {
        if (!responseMessage.isSuccessful()) {
            getViewModel().snackbarText.set(new ErrorMessage().execute(responseMessage));
        }
        invalidateViews();
    }


    private void updatePickupProcessState() {
        try {
            getViewModel().pickupState.next();
        } catch (StateIsMovingToNullException e) {
            getHostActivity().finish();
        }
    }

}
