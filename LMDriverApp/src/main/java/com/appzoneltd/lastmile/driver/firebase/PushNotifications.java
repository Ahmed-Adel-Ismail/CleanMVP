package com.appzoneltd.lastmile.driver.firebase;

import android.support.annotation.NonNull;

import com.appzoneltd.lastmile.driver.R;
import com.appzoneltd.lastmile.driver.subfeatures.pickups.OnDemandPickupNotificationShower;
import com.base.abstraction.commands.Command;
import com.base.abstraction.events.Message;
import com.base.abstraction.logs.Logger;
import com.base.cached.RemoteMessageData;
import com.base.interfaces.TypedValuable;

/**
 * the types of the Push notifications holding the received in {@link RemoteMessageData},
 * every type holds the necessary data to manipulate the received notification,
 * it can be used to generat Event Ids related to the Notification, and has it's
 * {@link #execute(Object)} method that handles showing the notification in the system-tray
 * if it is possible
 * <p>
 * Created by Ahmed Adel on 12/28/2016.
 */
enum PushNotifications implements TypedValuable<Long, String>, Command<Message, Void> {

    /**
     * this is the factory Constant that generates other constants based on passed parameters
     * to it's methods
     */
    FACTORY(0, null) {
        @Override
        public Void execute(Message message) {
            Logger.getInstance().error(getClass(), "unhandled message : " + message);
            return null;
        }
    },
    ON_DEMAND_PICKUP(R.id.onNotifiedOnDemandPickup, "OnDemandPickupRequestAssigned") {
        @Override
        public Void execute(Message message) {
            return new OnDemandPickupNotificationShower().execute(message);
        }
    };


    PushNotifications(long eventId, String value) {
        this.eventId = eventId;
        this.value = value;
    }

    private final long eventId;
    private final String value;


    @Override
    public Long getType(String payloadType) {
        if (payloadType != null) {
            return retrieveEventId(payloadType);
        }
        throw new UnsupportedOperationException("no payload type mapped to @null");
    }

    private Long retrieveEventId(@NonNull String value) {
        for (PushNotifications p : PushNotifications.values()) {
            if (value.equals(p.value)) {
                return p.eventId;
            }
        }
        throw new UnsupportedOperationException("this value for payload type is not supported");
    }

    @Override
    public final String getValue() {
        return value;
    }

    public final PushNotifications getTypeById(long id) {
        for (PushNotifications type : values()) {
            if (type.eventId == id) return type;
        }
        return FACTORY;
    }

}
