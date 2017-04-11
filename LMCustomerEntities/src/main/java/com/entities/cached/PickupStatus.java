package com.entities.cached;


import com.base.annotations.MockEntity;
import com.entities.mocks.MockedPickupStatus;

import java.io.Serializable;

/**
 * Created by Wafaa on 12/15/2016.
 */

@MockEntity(MockedPickupStatus.class)
public class PickupStatus implements Serializable {

    protected String nickname;
    protected String recipientName;
    protected String recipientAddress;
    protected NotificationTypes status;
    protected String latitude;
    protected String longitude;

    public PickupStatus() {

    }

    public PickupStatus(String nName, String rName, String rAddress, NotificationTypes status) {
        this.nickname = nName;
        this.recipientName = rName;
        this.recipientAddress = rAddress;
        this.status = status;
    }

    public String getNickname() {
        return nickname;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public String getRecipientAddress() {
        return recipientAddress;
    }

    public NotificationTypes getStatus() {
        return status;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public void setRecipientAddress(String recipientAddress) {
        this.recipientAddress = recipientAddress;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setStatus(NotificationTypes status) {
        this.status = status;
    }
}
