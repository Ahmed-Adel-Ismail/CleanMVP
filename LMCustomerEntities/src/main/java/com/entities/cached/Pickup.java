package com.entities.cached;

import java.io.Serializable;

/**
 * Created by Wafaa on 6/16/2016.
 */
public class Pickup implements Serializable {

    private Long pickupRequestId;
    private String requestType;
    private String requestTime;
    private Long requesterId;
    private String pickupLongitude;
    private String pickupLatitude;
    private String pickupWaselLocation;
    private String pickupFormatedAddress;
    private String timeFrom;
    private String timeTo;
    private String pickupDate;
    private Long recipientId;
    private String recipientName;
    private String recipientMobile;
    private String recipientLongitude;
    private String recipientLatitude;
    private String recipientWaselLocation;
    private String recipientFormatedAddress;
    private String recipientAdditionalInfo;
    private String additionalServices;
    private String labelingText;
    private String paymentType;
    private String paymentMethod;
    private String description;
    private Long packageId;


    public void setAdditionalServices(String additionalServices) {
        this.additionalServices = additionalServices;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public void setLabelingText(String labelingText) {
        this.labelingText = labelingText;
    }


    public void setPackageId(long packageId) {
        this.packageId = packageId;
    }


    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }


    public void setPickupDate(String pickupDate) {
        this.pickupDate = pickupDate;
    }


    public void setPickupFormatedAddress(String pickupFormatedAddress) {
        this.pickupFormatedAddress = pickupFormatedAddress;
    }

    public void setPickupWaselLocation(String pickupWaselLocation) {
        this.pickupWaselLocation = pickupWaselLocation;
    }


    public void setRecipientAdditionalInfo(String recipientAdditionalInfo) {
        this.recipientAdditionalInfo = recipientAdditionalInfo;
    }


    public void setRecipientFormatedAddress(String recipientFormatedAddress) {
        this.recipientFormatedAddress = recipientFormatedAddress;
    }

    public void setRecipientId(long recipientId) {
        this.recipientId = recipientId;
    }


    public void setRecipientMobile(String recipientMobile) {
        this.recipientMobile = recipientMobile;
    }


    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }


    public void setRecipientWaselLocation(String recipientWaselLocation) {
        this.recipientWaselLocation = recipientWaselLocation;
    }


    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }


    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }


    public void setRequesterId(long requesterId) {
        this.requesterId = requesterId;
    }


    public void setUserInfo(UserInfo userInfo) {
        setRequesterId(userInfo.getUserId());
    }


    public void setPickupLongitude(String pickupLongitude) {
        this.pickupLongitude = pickupLongitude;
    }


    public void setPickupLatitude(String pickupLatitude) {
        this.pickupLatitude = pickupLatitude;
    }


    public void setTimeFrom(String timeFrom) {
        this.timeFrom = timeFrom;
    }


    public void setTimeTo(String timeTo) {
        this.timeTo = timeTo;
    }


    public void setRecipientLongitude(String recipientLongitude) {
        this.recipientLongitude = recipientLongitude;
    }

    public void setRecipientLatitude(String recipientLatitude) {
        this.recipientLatitude = recipientLatitude;
    }

}
