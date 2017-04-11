package com.appzoneltd.lastmile.driver.features.pickup.verification;

import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.appzoneltd.lastmile.driver.R;
import com.appzoneltd.lastmile.driver.features.pickup.model.PickupProcessModel;
import com.appzoneltd.lastmile.driver.subfeatures.ServerImageDrawer;
import com.base.abstraction.annotations.interfaces.Executable;
import com.base.abstraction.commands.Command;
import com.base.abstraction.logs.Logger;
import com.base.abstraction.system.AppResources;
import com.base.presentation.base.abstracts.features.AbstractActivity;
import com.base.presentation.base.presentation.PresenterOnTouchesHandler;
import com.base.presentation.listeners.OnTouchParams;
import com.base.presentation.views.dialogs.EventDialog;
import com.base.presentation.views.dialogs.EventDialogBuilder;
import com.base.presentation.views.dialogs.EventDialogLayout;
import com.base.cached.ServerImage;
import com.entities.cached.pakage.PackageDetails;

import es.guiguegon.gallerymodule.GalleryHelper;

import static com.appzoneltd.lastmile.driver.features.pickup.verification.VerificationViewModel.IMAGE_INDEX_LEFT;
import static com.appzoneltd.lastmile.driver.features.pickup.verification.VerificationViewModel.IMAGE_INDEX_RIGHT;

class VerificationTouchesHandler extends PresenterOnTouchesHandler
        <VerificationPresenter, VerificationViewModel, PickupProcessModel> {

    @Executable(R.id.screen_pickup_process_verify_package_images_left_image_main_image)
    void onLeftImageClicked(OnTouchParams params) {

        VerificationImageTag tag = new VerificationImageTag();

        int requestCodeRes = R.integer.requestCodeOpenCameraForVerifyPackageLeftImage;
        tag.requestCode.set(AppResources.integer(requestCodeRes));

        PackageDetails packageDetails = getModel().packageDetails.get();
        ServerImage serverImage = packageDetails.getServerImageByIndex(IMAGE_INDEX_LEFT);
        tag.serverImage.set(serverImage);

        openDialog(tag);
    }

    @Executable(R.id.screen_pickup_process_verify_package_images_right_image_main_image)
    void onRightImageClicked(OnTouchParams params) {

        int requestCode = R.integer.requestCodeOpenCameraForVerifyPackageRightImage;
        requestCode = AppResources.integer(requestCode);
        try {
            VerificationImageTag tag = new VerificationImageTag();
            tag.requestCode.set(requestCode);

            PackageDetails packageDetails = getModel().packageDetails.get();
            ServerImage serverImage = packageDetails.getServerImageByIndex(IMAGE_INDEX_RIGHT);
            tag.serverImage.set(serverImage);

            openDialog(tag);
        } catch (UnsupportedOperationException e) {
            Logger.getInstance().error(getClass(), e);
            openGallery(requestCode);
        }

    }

    private void openGallery(int reqCode) {
        getHostActivity().startActivityForResult(new GalleryHelper()
                .setMultiselection(false)
                .setMaxSelectedItems(1)
                .setShowVideos(false)
                .getCallingIntent(getHostActivity()), reqCode);
    }


    private void openDialog(VerificationImageTag tag) {

        EventDialogLayout layout = new EventDialogLayout(R.layout.dialog_verify_package_change_image);
        layout.onInflate(imageDialogOnInflate());

        int changeBtnRes = R.string.screen_pickup_process_verify_package_image_dialog_button_change;
        int cancelBtnRes = R.string.screen_pickup_process_verify_package_image_dialog_button_cancel;

        EventDialogBuilder builder = new EventDialogBuilder(R.id.onVerifyPackageImageDialog);
        builder.setTag(tag);
        builder.setLayout(layout);
        builder.setPositiveText(changeBtnRes);
        builder.setNegativeText(cancelBtnRes);

        EventDialog eventDialog = new EventDialog(builder, getHostActivity());
        eventDialog.show();


    }

    @NonNull
    private Command<EventDialogLayout.Params, Void> imageDialogOnInflate() {
        return new Command<EventDialogLayout.Params, Void>() {
            @Override
            public Void execute(EventDialogLayout.Params p) {
                AbstractActivity activity = p.activity.get();
                int viewId = R.id.dialog_verify_package_change_image_image_view;
                ImageView imageView = (ImageView) p.dialogView.get().findViewById(viewId);
                VerificationImageTag tag = (VerificationImageTag) p.dialogBuilder.get().getTag();
                new ServerImageDrawer(activity, tag.serverImage.get()).execute(imageView);
                return null;
            }
        };
    }


}
