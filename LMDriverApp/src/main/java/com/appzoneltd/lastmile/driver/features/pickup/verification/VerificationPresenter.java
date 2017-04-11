package com.appzoneltd.lastmile.driver.features.pickup.verification;

import com.appzoneltd.lastmile.driver.R;
import com.appzoneltd.lastmile.driver.features.pickup.model.PickupProcessModel;
import com.base.abstraction.annotations.interfaces.Executable;
import com.base.abstraction.annotations.interfaces.Load;
import com.base.abstraction.events.Message;
import com.base.cached.ServerImage;
import com.base.presentation.annotations.interfaces.OnChecksChangedHandler;
import com.base.presentation.annotations.interfaces.OnClickHandler;
import com.base.presentation.annotations.interfaces.OnItemSelectedHandler;
import com.base.presentation.annotations.interfaces.OnTouchesHandler;
import com.base.presentation.base.presentation.Presenter;
import com.base.presentation.requests.ActivityResult;
import com.base.usecases.annotations.ResponsesHandler;

import es.guiguegon.gallerymodule.GalleryActivity;
import es.guiguegon.gallerymodule.model.GalleryMedia;

import static com.appzoneltd.lastmile.driver.features.pickup.verification.VerificationViewModel.IMAGE_INDEX_LEFT;
import static com.appzoneltd.lastmile.driver.features.pickup.verification.VerificationViewModel.IMAGE_INDEX_RIGHT;

@Load
@OnClickHandler(VerificationClicksHandler.class)
@OnTouchesHandler(VerificationTouchesHandler.class)
@OnChecksChangedHandler(VerificationCheckChangesHandler.class)
@OnItemSelectedHandler(VerificationItemSelectedHandler.class)
@ResponsesHandler(VerificationResponsesHandler.class)
class VerificationPresenter extends Presenter
        <VerificationPresenter, VerificationViewModel, PickupProcessModel> {

    private ImagesLoader imagesLoader = new ImagesLoader();


    @Executable(R.id.onResume)
    void onResume(Message message) {
        imagesLoader.execute(this);
        super.onResume();
    }

    @Executable(R.id.onActivityResult)
    void onActivityResult(Message message) {
        ActivityResult result = message.getContent();
        if (result.hasRequestCode(R.integer.requestCodeOpenCameraForVerifyPackageLeftImage)
                && result.isResultCodeOk()) {
            drawImage(IMAGE_INDEX_LEFT, result);
        } else if (result.hasRequestCode(R.integer.requestCodeOpenCameraForVerifyPackageRightImage)
                && result.isResultCodeOk()) {
            drawImage(IMAGE_INDEX_RIGHT, result);
        }
    }

    private void drawImage(int index, ActivityResult result) {
        getModel().packageDetails.get().addServerImage(index, createServerImage(result));
        onUpdateViewModel();
        invalidateViews();
    }


    private ServerImage createServerImage(ActivityResult result) {
        GalleryMedia media = (GalleryMedia) result.data.getParcelableArrayListExtra(
                GalleryActivity.RESULT_GALLERY_MEDIA_LIST).get(0);
        String imagePath = media.mediaUri();
        ServerImage serverImage = new ServerImage();
        serverImage.setUri(imagePath);
        return serverImage;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (imagesLoader != null) {
            imagesLoader.clear();
            imagesLoader = null;
        }
    }


}
