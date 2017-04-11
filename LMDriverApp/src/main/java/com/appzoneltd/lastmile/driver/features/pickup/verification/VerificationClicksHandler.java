package com.appzoneltd.lastmile.driver.features.pickup.verification;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.appzoneltd.lastmile.driver.R;
import com.appzoneltd.lastmile.driver.features.pickup.model.PickupProcessModel;
import com.base.abstraction.annotations.interfaces.Executable;
import com.base.abstraction.commands.Command;
import com.base.abstraction.concurrency.ExecutionThread;
import com.base.abstraction.logs.Logger;
import com.base.cached.ServerImagesGroup;
import com.base.cached.UploadImageResponse;
import com.base.presentation.base.presentation.PresenterClicksHandler;
import com.base.presentation.exceptions.subfeatures.NoImagesToUploadException;
import com.base.presentation.references.Entity;
import com.base.presentation.subfeatures.ImagesUploaderFuture;
import com.base.requesters.UploadImageParam;
import com.base.usecases.events.ResponseMessage;
import com.entities.cached.pakage.PackageDetails;

class VerificationClicksHandler extends PresenterClicksHandler
        <VerificationPresenter, VerificationViewModel, PickupProcessModel> {


    @Executable(R.id.screen_pickup_process_verify_package_submit_button)
    void onSubmitClick(View view) {
        getModel().packageDetailsValidator
                .validate()
                .onComplete(validationOnComplete())
                .onException(onException());
    }

    private Command<Entity<PackageDetails>, Void> validationOnComplete() {
        return new Command<Entity<PackageDetails>, Void>() {
            @Override
            public Void execute(Entity<PackageDetails> entity) {
                getViewModel().submitting.set(true);
                invalidateViews();
                uploadImagesThenVerify();
                return null;
            }
        };
    }

    private void uploadImagesThenVerify() {
        ServerImagesGroup serverImages = getModel().packageDetails.get().getServerImages();
        new ImagesUploaderFuture(getHostActivity()).execute(serverImages)
                .onThread(ExecutionThread.MAIN)
                .onComplete(imagesUploaderOnComplete())
                .onException(onException());
    }

    @NonNull
    private Command<UploadImageParam, Void> imagesUploaderOnComplete() {
        return new Command<UploadImageParam, Void>() {
            @Override
            public Void execute(UploadImageParam p) {
                getModel().imageUploader
                        .onNext(modelImageUploaderOnNext())
                        .onComplete(modelImageUploaderOnComplete())
                        .request(p);
                return null;
            }
        };
    }

    private Command<ResponseMessage, Boolean> modelImageUploaderOnNext() {
        return new Command<ResponseMessage, Boolean>() {
            @Override
            public Boolean execute(ResponseMessage responseMessage) {
                if (responseMessage.isSuccessful()) {
                    UploadImageResponse r = responseMessage.getContent();
                    try {
                        getModel().packageDetails.get().updateSelectedServerImage(r.getFileId());
                    } catch (Throwable e) {
                        Logger.getInstance().exception(e);
                    }
                }
                return true;
            }
        };
    }

    @NonNull
    private Command<ResponseMessage, Void> modelImageUploaderOnComplete() {
        return new Command<ResponseMessage, Void>() {
            @Override
            public Void execute(ResponseMessage responseMessage) {
                if (responseMessage.isSuccessful()) {
                    uploadImagesThenVerify();
                } else {
                    onException().execute(new RuntimeException("failed to upload image"));
                }
                return null;
            }
        };
    }

    private Command<Throwable, Void> onException() {
        return new Command<Throwable, Void>() {
            @Override
            public Void execute(Throwable e) {

                if (e instanceof NoImagesToUploadException) {
                    e = requestPackageDetailsVerification();
                    if (e == null) return null;
                }

                getViewModel().submitting.set(false);
                invalidateViews();
                Logger.getInstance().exception(e);

                return null;
            }
        };
    }

    @Nullable
    private Throwable requestPackageDetailsVerification() {
        Throwable e = null;
        try {
            getModel().packageDetails.get().updateImagesIdsFromServerImages();
            getModel().packageDetailsVerification.request();
        } catch (Throwable innerException) {
            e = innerException;
        }
        return e;
    }

}