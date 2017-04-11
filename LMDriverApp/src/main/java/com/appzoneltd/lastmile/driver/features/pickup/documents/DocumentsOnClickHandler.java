package com.appzoneltd.lastmile.driver.features.pickup.documents;

import android.support.annotation.NonNull;
import android.view.View;

import com.appzoneltd.lastmile.driver.R;
import com.appzoneltd.lastmile.driver.features.pickup.model.PickupProcessModel;
import com.appzoneltd.lastmile.driver.subfeatures.ImagesUploader;
import com.base.abstraction.annotations.interfaces.Executable;
import com.base.abstraction.commands.Command;
import com.base.abstraction.logs.Logger;
import com.base.cached.ServerImage;
import com.base.cached.ServerImagesGroup;
import com.base.presentation.base.presentation.PresenterClicksHandler;
import com.entities.cached.payment.PaymentMethod;
import com.entities.requesters.PackageDocuments;

class DocumentsOnClickHandler extends PresenterClicksHandler
        <DocumentsPresenter, DocumentsViewModel, PickupProcessModel> {

    @Executable(R.id.screen_pickup_process_documents_submit_button)
    void onDocumentsSubmitClick(View view) {
        if (getModel().customerIdImagePath.isEmpty()) {
            Logger.getInstance().error(getClass(), "empty @ customer id");
        } else if (isCreditCardPayment() && getModel().creditCardImagePath.isEmpty()) {
            Logger.getInstance().error(getClass(), "empty @ credit card id");
        } else {
            uploadImagesAndFinish();
        }
    }

    private boolean isCreditCardPayment() {
        return PaymentMethod.CREDIT_CARD.equals(getModel().packageDetails.get().getPaymentMethod());
    }

    private void uploadImagesAndFinish() {
        ServerImagesGroup imagesGroup = new ServerImagesGroup();
        ServerImage image = new ServerImage();
        image.setUri(getModel().customerIdImagePath.get());
        imagesGroup.put(0, image);

        if (!getModel().creditCardImagePath.isEmpty()) {
            image = new ServerImage();
            image.setUri(getModel().creditCardImagePath.get());
            imagesGroup.put(1, image);
        }

        getViewModel().progress.set(true);
        new ImagesUploader(getHostActivity(), getModel().imageUploader)
                .onComplete(onComplete())
                .onException(onException())
                .execute(imagesGroup);

    }

    @NonNull
    private Command<Throwable, Void> onException() {
        return new Command<Throwable, Void>() {
            @Override
            public Void execute(Throwable e) {
                getViewModel().progress.set(false);
                return null;
            }
        };
    }

    @NonNull
    private Command<ServerImagesGroup, Void> onComplete() {
        return new Command<ServerImagesGroup, Void>() {
            @Override
            public Void execute(ServerImagesGroup imagesGroup) {
                PackageDocuments docs = new PackageDocuments();
                docs.setPackageId(getModel().pickupProcessId.get().getPackageId());
                docs.setRequestId(getModel().pickupProcessId.get().getRequestId());
                try {
                    docs.setCustomerIdImageId(imagesGroup.getByIndex(0).getFileId());
                    docs.setCreditCardImageId(imagesGroup.getByIndex(1).getFileId());
                } catch (UnsupportedOperationException e) {
                    Logger.getInstance().error(DocumentsOnClickHandler.class, e.getMessage());
                }
                getModel().packageDocumentsConfirmation.request(docs);
                return null;
            }
        };
    }
}
