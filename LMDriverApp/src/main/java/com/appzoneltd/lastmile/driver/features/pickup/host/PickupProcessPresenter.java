package com.appzoneltd.lastmile.driver.features.pickup.host;

import com.appzoneltd.lastmile.driver.R;
import com.appzoneltd.lastmile.driver.features.pickup.model.PickupProcessModel;
import com.appzoneltd.lastmile.driver.features.pickup.payments.CancellationDialogTag;
import com.appzoneltd.lastmile.driver.features.pickup.verification.VerificationImageTag;
import com.base.abstraction.annotations.interfaces.Executable;
import com.base.abstraction.annotations.interfaces.Load;
import com.base.abstraction.events.Message;
import com.base.abstraction.logs.Logger;
import com.base.presentation.annotations.interfaces.OnClickHandler;
import com.base.presentation.base.presentation.Presenter;
import com.base.usecases.annotations.ResponsesHandler;
import com.entities.requesters.CancelRequest;

import es.guiguegon.gallerymodule.GalleryHelper;

/**
 * the {@link Presenter} of the pickup-process screen
 * <p>
 * Created by Ahmed Adel on 12/24/2016.
 */
@Load
@OnClickHandler(PickupProcessClicksHandler.class)
@ResponsesHandler(PickupProcessResponsesHandler.class)
class PickupProcessPresenter extends
        Presenter<PickupProcessPresenter, PickupProcessViewModel, PickupProcessModel> {

    @Executable(R.id.onResume)
    void onResume(Message message) {
        super.onResume();
        getModel().packageDetails.requestIfRequired();
    }

    @Executable(R.id.onPickupProcessCancelPaymentDialog)
    void onCancelReasonsPositiveClick(Message message) {
        if (message.getId() == R.id.onDialogPositiveClick) {
            CancellationDialogTag tag = message.getContent();
            try {
                startPickupCancellationRequest(tag);
            } catch (Throwable e) {
                Logger.getInstance().exception(e);
            }
        }
    }

    private void startPickupCancellationRequest(CancellationDialogTag tag) {
        int index = tag.selectedReasonIndex.get();
        CancelRequest request = new CancelRequest();
        request.setReasonId(getModel().cancellationReasons.get().getByIndex(index).getId());
        request.setPackageId(getModel().pickupProcessId.get().getPackageId());
        request.setDescription(tag.additionalNotes.get());
        request.setRequestId(getModel().pickupProcessId.get().getRequestId());
        getModel().pickupCancellation.request(request);
    }

    @Executable(R.id.onVerifyPackageImageDialog)
    void onVerifyPackageImageDialog(Message message) {
        if (message.getId() == R.id.onDialogPositiveClick) {
            VerificationImageTag tag = message.getContent();
            openGallery(tag.requestCode.get());
        }
    }

    private void openGallery(int reqCode) {
        getHostActivity().startActivityForResult(new GalleryHelper()
                .setMultiselection(false)
                .setMaxSelectedItems(1)
                .setShowVideos(false)
                .getCallingIntent(getHostActivity()), reqCode);
    }


}
