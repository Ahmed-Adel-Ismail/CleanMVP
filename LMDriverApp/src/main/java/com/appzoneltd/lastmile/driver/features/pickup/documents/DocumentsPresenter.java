package com.appzoneltd.lastmile.driver.features.pickup.documents;

import com.appzoneltd.lastmile.driver.R;
import com.appzoneltd.lastmile.driver.features.pickup.model.PickupProcessModel;
import com.base.abstraction.annotations.interfaces.Executable;
import com.base.abstraction.annotations.interfaces.Load;
import com.base.abstraction.events.Message;
import com.base.presentation.annotations.interfaces.OnClickHandler;
import com.base.presentation.annotations.interfaces.OnTouchesHandler;
import com.base.presentation.base.presentation.Presenter;
import com.base.presentation.requests.ActivityResult;

import es.guiguegon.gallerymodule.GalleryActivity;
import es.guiguegon.gallerymodule.model.GalleryMedia;

@Load
@OnTouchesHandler(DocumentsOnTouchHandler.class)
@OnClickHandler(DocumentsOnClickHandler.class)
class DocumentsPresenter extends
        Presenter<DocumentsPresenter, DocumentsViewModel, PickupProcessModel> {

    @Executable(R.id.onActivityResult)
    void onActivityResult(Message message) {
        ActivityResult result = message.getContent();
        if (result.hasRequestCode(R.integer.requestCodeOpenCameraForCustomerId) && result.isResultCodeOk()) {
            drawCustomerIdImage(result);
        } else if (result.hasRequestCode(R.integer.requestCodeOpenCameraForCreditCard) && result.isResultCodeOk()) {
            drawCreditCardImage(result);
        }
    }

    private void drawCreditCardImage(ActivityResult result) {
        String imagePath = retrieveImagePath(result);
        getModel().creditCardImagePath.set(imagePath);
        onUpdateViewModel();
        invalidateViews();
    }

    private void drawCustomerIdImage(ActivityResult result) {
        String imagePath = retrieveImagePath(result);
        getModel().customerIdImagePath.set(imagePath);
        onUpdateViewModel();
        invalidateViews();
    }


    private String retrieveImagePath(ActivityResult result) {
        GalleryMedia media = (GalleryMedia) result.data.getParcelableArrayListExtra(
                GalleryActivity.RESULT_GALLERY_MEDIA_LIST).get(0);
        return media.mediaUri();
    }


}
