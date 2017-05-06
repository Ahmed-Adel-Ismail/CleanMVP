package com.base.notifications;

import com.base.entities.cached.RemoteMessageData;

import java.io.Serializable;

/**
 * a test the Object received from Push notifications
 * <p>
 * Created by Ahmed Adel on 12/19/2016.
 *
 * @deprecated used for testing only
 */
@Deprecated
public class RemoteMessage implements Serializable, Cloneable {

    private RemoteMessageData data;

    public RemoteMessageData getData() {
        return data;
    }

    public void setData(RemoteMessageData data) {
        this.data = data;
    }
}


