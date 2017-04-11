package com.appzoneltd.lastmile.customer.firebase;

import android.support.annotation.NonNull;

import com.appzoneltd.lastmile.customer.R;
import com.base.interfaces.TypedValuable;

/**
 * Created by Wafaa on 2/15/2017.
 */

enum  NotificationEventIdGenerator implements TypedValuable<Long, String> {

    FACTORY(0, null)
    , BUSY(R.id.onNotifiedDriverBusy , "busy")
    , ASSIGNED(R.id.onNotifiedDriverAssigned , "assigned")
    , ARRIVED(R.id.onNotifiedDriverArrived , "arrived")
    , ON_HIS_WAY(R.id.onNotifiedDriverOnHisWay , "onHisWay")
    , INVOICE(R.id.onNotifiedInvoice, "INVOICE")
    , TRACKING_NUMBER(R.id.onNotifiedTrackingNumber , "trackingNumber")
    , RATING(R.id.onNotifiedDriverRating , "rating")
    , CANCELLED(R.id.onNotifiedRequestCancelled , "cancelled");


    NotificationEventIdGenerator(long eventId, String value) {
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
        for (NotificationEventIdGenerator p : NotificationEventIdGenerator.values()) {
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
}
