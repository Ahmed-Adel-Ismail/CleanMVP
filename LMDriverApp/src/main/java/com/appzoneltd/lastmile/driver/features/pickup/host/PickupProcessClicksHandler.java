package com.appzoneltd.lastmile.driver.features.pickup.host;

import android.support.annotation.NonNull;
import android.view.View;

import com.appzoneltd.lastmile.driver.R;
import com.appzoneltd.lastmile.driver.features.pickup.model.PickupProcessModel;
import com.base.abstraction.annotations.interfaces.Executable;
import com.base.abstraction.commands.Command;
import com.base.abstraction.exceptions.propagated.ThrowableGroup;
import com.base.presentation.exceptions.references.validators.InvalidNullValueException;
import com.base.presentation.exceptions.references.validators.InvalidValueException;
import com.base.abstraction.logs.Logger;
import com.base.abstraction.system.AppResources;
import com.base.presentation.base.presentation.PresenterClicksHandler;
import com.base.presentation.references.Entity;
import com.entities.cached.pakage.PackageDetails;
import com.entities.cached.payment.PaymentMethod;

class PickupProcessClicksHandler extends
        PresenterClicksHandler<PickupProcessPresenter, PickupProcessViewModel, PickupProcessModel> {

    @Executable(R.id.screen_pickup_process_progress_failed_button)
    void onRetryLoadingButton(View v) {
        getModel().packageDetails.request();
        getViewModel().loadingState.set(PickupProcessLoadingState.LOADING);
        invalidateViews();
    }

    @Executable(R.id.screen_pickup_process_verify_package_submit_button)
    void onVerifyPackageSubmitClick(View view) {
        getModel().packageDetailsValidator
                .validate()
                .onComplete(verifyPackageOnComplete())
                .onException(verifyPackageOnException());
    }

    @NonNull
    private Command<Entity<PackageDetails>, Void> verifyPackageOnComplete() {
        return new Command<Entity<PackageDetails>, Void>() {
            @Override
            public Void execute(Entity<PackageDetails> p) {
                // do nothing
                return null;
            }
        };
    }

    private Command<Throwable, Void> verifyPackageOnException() {
        return new Command<Throwable, Void>() {
            @Override
            public Void execute(Throwable throwable) {
                ThrowableGroup throwableGroup = (ThrowableGroup) throwable;
                if (throwableGroup.contains(InvalidValueException.class)) {
                    displaySnackbar();
                } else if (throwableGroup.contains(InvalidNullValueException.class)) {
                    logException();
                }

                return null;
            }

            private void logException() {
                Logger.getInstance().error(PickupProcessClicksHandler.class
                        , "null @ packageDetailsValidator");
            }

            private void displaySnackbar() {
                int res = R.string.screen_pickup_process_verify_package_submit_error;
                getViewModel().snackbarText.set(AppResources.string(res));
                invalidateViews();
            }
        };
    }

    @Executable(R.id.screen_pickup_process_payment_buttons_submit)
    void onPaymentSubmitClick(View view) {
        getModel().pickupInvoiceConfirmation.request();
    }

    @Executable(R.id.screen_pickup_process_documents_submit_button)
    void onDocumentsSubmitClick(View view) {
        if (getModel().customerIdImagePath.isEmpty()) {
            showCustomerIdErrorSnackbar();
        } else if (isCreditCardPayment() && getModel().creditCardImagePath.isEmpty()) {
            showCreditCardErrorSnackbar();
        }
    }


    private void showCustomerIdErrorSnackbar() {
        int res = R.string.screen_pickup_process_documents_uploading_customer_id_required;
        getViewModel().snackbarText.set(AppResources.string(res));
        invalidateViews();
    }

    private boolean isCreditCardPayment() {
        return PaymentMethod.CREDIT_CARD.equals(getModel().packageDetails.get().getPaymentMethod());
    }

    private void showCreditCardErrorSnackbar() {
        int res = R.string.screen_pickup_process_documents_uploading_credit_card_required;
        getViewModel().snackbarText.set(AppResources.string(res));
        invalidateViews();
    }


}
