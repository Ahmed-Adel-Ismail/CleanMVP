package com.appzoneltd.lastmile.driver.features.pickup.verification;

import com.appzoneltd.lastmile.driver.R;
import com.appzoneltd.lastmile.driver.features.pickup.model.PickupProcessModel;
import com.base.abstraction.annotations.interfaces.Executable;
import com.base.presentation.base.presentation.PresenterCheckChangesHandler;
import com.base.presentation.listeners.OnCheckedChangedParams;
import com.entities.cached.pakage.PackageDetails;
import com.entities.cached.pakage.PackageLabel;
import com.entities.cached.payment.PaymentMethod;
import com.entities.cached.payment.PaymentType;

/**
 * a handler for the {@link R.id#onCheckedChanged} updates for the verification screen
 * <p>
 * Created by Ahmed Adel on 12/31/2016.
 */
class VerificationCheckChangesHandler extends PresenterCheckChangesHandler
        <VerificationPresenter, VerificationViewModel, PickupProcessModel> {

    private String originalWrappingText;

    @Executable(R.id.screen_pickup_process_verify_package_additional_services_details_check_box_one)
    void additionalServicesWrappingView(OnCheckedChangedParams p) {
        PackageDetails packageDetails = getModel().packageDetails.get();
        if (p.checked.isTrue()) {
            packageDetails.setWrappingLabel(originalWrappingText);
        } else {
            originalWrappingText = packageDetails.getWrappingLabel();
            packageDetails.setWrappingLabel(null);

        }
        updateViewModel();
    }

    @Executable(R.id.screen_pickup_process_verify_package_additional_services_details_check_box_two)
    void additionalServicesBoxingView(OnCheckedChangedParams p) {
        getModel().packageDetails.get().setBoxing(p.checked.isTrue());
        updateViewModel();
    }

    @Executable({
            R.id.screen_pickup_process_verify_package_labels_details_check_box_one,
            R.id.screen_pickup_process_verify_package_labels_details_check_box_two,
            R.id.screen_pickup_process_verify_package_labels_details_check_box_three,
            R.id.screen_pickup_process_verify_package_labels_details_check_box_four,
            R.id.screen_pickup_process_verify_package_labels_details_check_box_five,
            R.id.screen_pickup_process_verify_package_labels_details_check_box_six,
            R.id.screen_pickup_process_verify_package_labels_details_check_box_seven,
            R.id.screen_pickup_process_verify_package_labels_details_check_box_eight})
    void onPackageLabelCheckBoxChanged(OnCheckedChangedParams p) {
        PackageDetails packageDetails = getModel().packageDetails.get();
        PackageLabel label = (PackageLabel) p.tag.get();
        if (p.checked.isTrue()) {
            packageDetails.addPackageLabel(label);
        } else {
            packageDetails.removePackageLabel(label);
        }
        updateViewModel();
    }

    @Executable({
            R.id.screen_pickup_process_verify_package_payment_at_details_check_box_one,
            R.id.screen_pickup_process_verify_package_payment_at_details_check_box_two})
    void onPaymentTypeCheckBoxChanged(OnCheckedChangedParams p) {
        PackageDetails packageDetails = getModel().packageDetails.get();
        PaymentType paymentType = (PaymentType) p.tag.get();
        if (p.checked.isTrue()) {
            packageDetails.setPaymentType(paymentType);
            updateViewModel();
            invalidateViews();
        }
    }

    @Executable({
            R.id.screen_pickup_process_verify_package_buy_with_details_check_box_one,
            R.id.screen_pickup_process_verify_package_buy_with_details_check_box_two})
    void onPaymentMethodCheckBoxChanged(OnCheckedChangedParams p) {
        PackageDetails packageDetails = getModel().packageDetails.get();
        PaymentMethod paymentMethod = (PaymentMethod) p.tag.get();
        if (p.checked.isTrue()) {
            packageDetails.setPaymentMethod(paymentMethod);
            updateViewModel();
            invalidateViews();
        }
    }


    @Override
    public void clear() {
        super.clear();
        originalWrappingText = null;
    }


}
