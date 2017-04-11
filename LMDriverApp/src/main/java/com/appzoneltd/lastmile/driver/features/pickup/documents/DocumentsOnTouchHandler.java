package com.appzoneltd.lastmile.driver.features.pickup.documents;

import com.appzoneltd.lastmile.driver.R;
import com.appzoneltd.lastmile.driver.features.pickup.model.PickupProcessModel;
import com.base.abstraction.annotations.interfaces.Executable;
import com.base.abstraction.system.AppResources;
import com.base.presentation.base.presentation.PresenterOnTouchesHandler;
import com.base.presentation.listeners.OnTouchParams;

import es.guiguegon.gallerymodule.GalleryHelper;

class DocumentsOnTouchHandler extends PresenterOnTouchesHandler
        <DocumentsPresenter, DocumentsViewModel, PickupProcessModel> {

    @Executable(R.id.screen_pickup_process_documents_images_customer_id_image_image_view)
    void onCustomerIdImageClicked(OnTouchParams p) {
        int reqCode = AppResources.integer(R.integer.requestCodeOpenCameraForCustomerId);
        openGallery(reqCode);
    }

    @Executable(R.id.screen_pickup_process_documents_images_credit_card_image_image_view)
    void onCreditCardImageClicked(OnTouchParams p) {
        int reqCode = AppResources.integer(R.integer.requestCodeOpenCameraForCreditCard);
        openGallery(reqCode);
    }

    private void openGallery(int reqCode) {
        getHostActivity().startActivityForResult(new GalleryHelper()
                .setMultiselection(false)
                .setMaxSelectedItems(1)
                .setShowVideos(false)
                .getCallingIntent(getHostActivity()), reqCode);
    }
}
