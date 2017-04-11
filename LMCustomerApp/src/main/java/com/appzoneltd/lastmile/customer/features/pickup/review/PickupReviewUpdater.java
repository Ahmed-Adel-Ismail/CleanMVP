package com.appzoneltd.lastmile.customer.features.pickup.review;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.features.pickup.models.PickupModel;
import com.base.abstraction.system.AppResources;
import com.base.presentation.base.presentation.PresenterUpdater;
import com.base.presentation.views.validators.StringValidator;
import com.base.presentation.views.validators.ValidStringGenerator;

import java.util.Date;

/**
 * A {@link PresenterUpdater} for {@link PickupReviewPresenter}
 * <p>
 * Created by Ahmed Adel on 10/17/2016.
 */
class PickupReviewUpdater extends
        PresenterUpdater<PickupReviewPresenter, PickupReviewViewModel, PickupModel> {


    @Override
    public void onUpdateViewModel() {

        Bitmap firstBmp = getModel().getPackage().getFirstPhotoBitmap();
        Bitmap secondBmp = getModel().getPackage().getSecondPhotoBitmap();
        String paymentAt = getModel().getPackage().getPaymentType().label;
        String estimateCost = null;
        String shipmentService = getModel().getRecipient().getServiceValue()
                + ", " + getModel().getRecipient().getServiceTypeValue();
        String pickupTime = getPickupTime();
        String pickupAddress = getModel().getPickupFormattedAddress();
        String pickupDisplayedAddress = getModel().getPickupDisplayedAddress();
        String nickname = getModel().getPackage().getNickname();
        String packageType = getModel().getPackage().getPackageTypeText();
        String packageWeight = getWeightAsString();
        String whatsInside = getModel().getPackage().getDescription();
        String additionalService = createAdditionalServiceText();
        String recipientName = getModel().getRecipient().getName();
        String recipientPhoneNumber = getModel().getRecipient().getPhoneNumber();
        String recipientAddress = getModel().getRecipient().getAddress();
        String recipientNotes = getModel().getRecipient().getNotes();

        getViewModel().setTopLeftBitmap(firstBmp);
        getViewModel().setTopRightBitmap(secondBmp);
        getViewModel().setPaymentAt(paymentAt);
        getViewModel().setEstimateCost(estimateCost);
        getViewModel().setShipmentType(shipmentService);
        getViewModel().setPickupTime(pickupTime);
        getViewModel().setAddress(pickupAddress);
        getViewModel().setPickupDisplayedAddress(pickupDisplayedAddress);
        getViewModel().setNickname(nickname);
        getViewModel().setPackageType(packageType);
        getViewModel().setPackageWeight(packageWeight);
        getViewModel().setWhatsInside(whatsInside);
        getViewModel().setAdditionalService(additionalService);
        getViewModel().setRecipientName(recipientName);
        getViewModel().setRecipientPhoneNumber(recipientPhoneNumber);
        getViewModel().setRecipientAddress(recipientAddress);
        getViewModel().setRecipientNotes(recipientNotes);

        invalidateNicknameVisibility();
        invalidateBriefVisibility();
        invalidateAdditionalServicesVisibility();
        invalidateNotesVisibility();

        getViewModel().invalidateViews();
    }

    private String getPickupTime() {
        String pickupTime;
        if (getModel().getSchedule().isScheduled()) {
            pickupTime = createScheduledPickupTime();
        } else {
            pickupTime = createPickupNowTime();
        }
        return pickupTime;
    }

    @NonNull
    private String createScheduledPickupTime() {
        Date pickupDate = getModel().getSchedule().getDate();
        String formattedDate = PickupModel.getDateFormatter().format(pickupDate);
        return formattedDate + ", " + getModel().getSchedule().getSelectedTime();
    }

    @NonNull
    private String createPickupNowTime() {
        return AppResources.string(R.string.now);
    }


    @NonNull
    private String getWeightAsString() {
        int weight;
        if (getModel().getPackage().isBoxSelected()) {
            weight = getModel().getPackage().getBoxWeight();
        } else {
            weight = getModel().getPackage().getExpectedWeight();
        }
        return String.valueOf(weight
                + " " + AppResources.string(R.string.pickup_review_package_weight));

    }

    private void invalidateNicknameVisibility() {
        String nickName = getModel().getPackage().getNickname();
        nickName = new ValidStringGenerator().execute(nickName);
        if (new StringValidator().execute(nickName)) {
            getViewModel().setNicknameVisibility(false);
        } else {
            getViewModel().setNicknameVisibility(true);
        }
    }

    private void invalidateBriefVisibility() {
        String desc = new ValidStringGenerator().execute(getModel().getPackage().getDescription());
        if (new StringValidator().execute(desc)) {
            getViewModel().setBriefVisibility(false);
        } else {
            getViewModel().setBriefVisibility(true);
        }
    }

    private void invalidateAdditionalServicesVisibility() {
        String additionalServiceText = createAdditionalServiceText();
        additionalServiceText = new ValidStringGenerator().execute(additionalServiceText);
        if (new StringValidator().execute(additionalServiceText)) {
            getViewModel().setAdditionalServicesVisibility(false);
        } else {
            getViewModel().setAdditionalServicesVisibility(true);
        }
    }

    private void invalidateNotesVisibility() {
        String notes = getModel().getRecipient().getNotes();
        notes = new ValidStringGenerator().execute(notes);
        if (new StringValidator().execute(notes)) {
            getViewModel().setNotesVisibility(false);
        } else {
            getViewModel().setNotesVisibility(true);
        }
    }

    private String createAdditionalServiceText() {
        String service = "";
        if (getModel().getPackage().hasPackagingBox()) {
            service = getModel().getPackage().getAdditionalServices();
        }
        if (getModel().getPackage().isWrapAndLabel()) {
            if (service.isEmpty()) {
                service = String.format(AppResources.string
                                (R.string.pickup_review_additional_service_txt),
                        getModel().getPackage().getLabelMessage());
            } else {
                service = service + ", " + String.format(AppResources.string
                                (R.string.pickup_review_additional_service_txt),
                        getModel().getPackage().getLabelMessage());
            }
        }
        return service;
    }
}
