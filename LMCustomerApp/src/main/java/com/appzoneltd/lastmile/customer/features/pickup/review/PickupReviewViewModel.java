package com.appzoneltd.lastmile.customer.features.pickup.review;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.View;

import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.presentation.base.abstracts.features.ViewBinder;
import com.base.presentation.base.presentation.ValidatorViewModel;
import com.base.presentation.base.presentation.ViewModel;
import com.base.presentation.listeners.OnEventListener;

/**
 * a {@link ViewModel} to handle the UI of the Pickup Review screen
 * <p/>
 * Created by Ahmed Adel on 9/27/2016.
 */
public class PickupReviewViewModel extends ValidatorViewModel {

    private OnEventListener onEventListener;
    private Bitmap topLeftBitmap;
    private Bitmap topRightBitmap;
    private String paymentAt;
    private String estimateCost;
    private String shipmentType;
    private String pickupTime;
    private String address;
    private String pickupDisplayedAddress;
    private String nickname;
    private String packageType;
    private String packageWeight;
    private String whatsInside;
    private String additionalService;
    private String recipientName;
    private String recipientPhoneNumber;
    private String recipientAddress;
    private String recipientNotes;
    private boolean nicknameVisibility;
    private boolean briefVisibility;
    private boolean additionalServicesVisibility;
    private boolean notesVisibility;
    private boolean isRequesting = false;


    PickupReviewViewModel(ViewBinder viewBinder) {
        super(viewBinder);
        onEventListener = new OnEventListener(viewBinder);
    }

    @NonNull
    @Override
    protected CommandExecutor<Long, View> createInvalidateCommands() {
        CommandExecutor<Long, View> commandExecutor = new CommandExecutor<>();
        commandExecutor.putAll(new PickupReviewPreSubmitInvalidator(this));
        commandExecutor.putAll(new PickupReviewOptionalLayoutsInvalidator(this));
        commandExecutor.putAll(new PickupReviewPostSubmitInvalidator(this));
        return commandExecutor;
    }


    void setAdditionalService(String additionalService) {
        this.additionalService = additionalService;
    }


    void setAddress(String address) {
        this.address = address;
    }

    void setEstimateCost(String estimateCost) {
        this.estimateCost = estimateCost;
    }

    void setNickname(String nickname) {
        this.nickname = nickname;
    }

    void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    void setPackageWeight(String packageWeight) {
        this.packageWeight = packageWeight;
    }

    void setPaymentAt(String paymentAt) {
        this.paymentAt = paymentAt;
    }

    void setPickupTime(String pickupTime) {
        this.pickupTime = pickupTime;
    }

    void setRecipientAddress(String recipientAddress) {
        this.recipientAddress = recipientAddress;
    }

    void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    void setRecipientNotes(String recipientNotes) {
        this.recipientNotes = recipientNotes;
    }

    void setRecipientPhoneNumber(String recipientPhoneNumber) {
        this.recipientPhoneNumber = recipientPhoneNumber;
    }

    void setShipmentType(String shipmentType) {
        this.shipmentType = shipmentType;
    }

    void setTopLeftBitmap(Bitmap topLeftBitmap) {
        this.topLeftBitmap = topLeftBitmap;
    }

    void setTopRightBitmap(Bitmap topRightBitmap) {
        this.topRightBitmap = topRightBitmap;
    }

    void setWhatsInside(String whatsInside) {
        this.whatsInside = whatsInside;
    }

    void setNicknameVisibility(boolean nicknameVisibility) {
        this.nicknameVisibility = nicknameVisibility;
    }

    void setBriefVisibility(boolean briefVisibility) {
        this.briefVisibility = briefVisibility;
    }

    void setAdditionalServicesVisibility(boolean additionalServicesVisibility) {
        this.additionalServicesVisibility = additionalServicesVisibility;
    }

    void setNotesVisibility(boolean notesVisibility) {
        this.notesVisibility = notesVisibility;
    }

    String getAdditionalService() {
        return additionalService;
    }

    String getAddress() {
        return address;
    }

    String getEstimateCost() {
        return estimateCost;
    }

    String getNickname() {
        return nickname;
    }

    OnEventListener getOnEventListener() {
        return onEventListener;
    }

    String getPackageType() {
        return packageType;
    }

    String getPackageWeight() {
        return packageWeight;
    }

    String getPaymentAt() {
        return paymentAt;
    }

    String getPickupTime() {
        return pickupTime;
    }

    String getRecipientAddress() {
        return recipientAddress;
    }

    String getRecipientName() {
        return recipientName;
    }

    String getRecipientNotes() {
        return recipientNotes;
    }

    String getRecipientPhoneNumber() {
        return recipientPhoneNumber;
    }

    String getShipmentType() {
        return shipmentType;
    }

    Bitmap getTopLeftBitmap() {
        return topLeftBitmap;
    }

    Bitmap getTopRightBitmap() {
        return topRightBitmap;
    }

    String getWhatsInside() {
        return whatsInside;
    }

    boolean isNicknameVisibility() {
        return nicknameVisibility;
    }

    boolean isBriefVisibility() {
        return briefVisibility;
    }

    boolean isAdditionalServicesVisibility() {
        return additionalServicesVisibility;
    }

    boolean isNotesVisibility() {
        return notesVisibility;
    }

    public boolean isRequesting() {
        return isRequesting;
    }

    public void setRequesting(boolean requesting) {
        isRequesting = requesting;
    }

    public String getPickupDisplayedAddress() {
        return pickupDisplayedAddress;
    }

    public void setPickupDisplayedAddress(String pickupDisplayedAddress) {
        this.pickupDisplayedAddress = pickupDisplayedAddress;
    }

    @Override
    public void onDestroy() {

        topLeftBitmap = null;
        topRightBitmap = null;

        onEventListener = null;
        paymentAt = null;
        estimateCost = null;
        shipmentType = null;
        pickupTime = null;
        address = null;
        nickname = null;
        packageType = null;
        packageWeight = null;
        whatsInside = null;
        additionalService = null;
        recipientName = null;
        recipientPhoneNumber = null;
        recipientAddress = null;
        recipientNotes = null;
    }


}
