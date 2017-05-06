package com.base.presentation.receivers;

import android.content.Context;
import android.content.Intent;

import com.base.abstraction.events.Event;
import com.base.abstraction.events.Message;
import com.base.abstraction.R;
import com.base.presentation.base.abstracts.features.Feature;
import com.base.presentation.base.abstracts.system.AbstractReceiver;

/**
 * A broadcast receiver that listens on the Location provider changes
 * <p/>
 * Created by Ahmed Adel on 9/18/2016.
 */
public class GPSStateReceiver extends AbstractReceiver {

    private Boolean lastGPSState;
    private boolean notifyOnRegister;

    /**
     * createNativeMethod a {@link GPSStateReceiver}
     *
     * @param feature          the {@link Feature} that will be used by the receiver
     * @param notifyOnRegister pass {@code true} if you want this Receiver to trigger an
     *                         {@link Event} indicating the state of the GPS every time it is
     *                         registered (in {@code onResume()})
     */
    public GPSStateReceiver(Feature feature, boolean notifyOnRegister) {
        super(feature, "android.location.PROVIDERS_CHANGED");
        this.notifyOnRegister = notifyOnRegister;
    }

    @Override
    protected void onRegister() {
        if (notifyOnRegister) {
            updateLastGPSStateAndNotify(getFeature().getSystemServices().isGPSProviderEnabled());
        }
    }


    @Override
    public final void onReceive(Context context, Intent intent) {
        boolean gpsState = getFeature().getSystemServices().isGPSProviderEnabled();
        if (lastGPSState == null || lastGPSState != gpsState) {
            updateLastGPSStateAndNotify(gpsState);
        }
    }

    private void updateLastGPSStateAndNotify(boolean gpsState) {
        lastGPSState = gpsState;
        Message message = new Message.Builder().content(gpsState).build();
        Event event = new Event.Builder(R.id.onGPSStateChanged).message(message)
                .senderActorAddress(R.id.addressBroadcastReceiver).build();
        notifyObservers(event);
    }

    @Override
    public void onClear() {
        lastGPSState = null;
    }
}
