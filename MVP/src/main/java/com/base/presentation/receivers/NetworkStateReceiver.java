package com.base.presentation.receivers;

import android.content.Context;
import android.content.Intent;

import com.base.abstraction.events.Event;
import com.base.abstraction.events.Message;
import com.base.abstraction.R;
import com.base.presentation.base.abstracts.features.Feature;
import com.base.presentation.base.abstracts.system.AbstractReceiver;

/**
 * An {@link AbstractReceiver} that listens on the network state and notify the waiting
 * classes with a {@link NetworkStateReceiver.ConnectedNetworks} instance holding the
 * required data
 * <p/>
 * Created by Ahmed Adel on 9/18/2016.
 */
public class NetworkStateReceiver extends AbstractReceiver {

    private ConnectedNetworks connectedNetworks;

    public NetworkStateReceiver(Feature feature) {
        super(feature, "android.net.conn.CONNECTIVITY_CHANGE");
    }


    @Override
    public void onReceive(Context context, Intent intent) {

        boolean wifi = getFeature().getSystemServices().isWifiConnected();
        boolean mobile = getFeature().getSystemServices().isMobileNetworkConnected();
        boolean connected = wifi || mobile;

        ConnectedNetworks newConnectedNetworks = new ConnectedNetworks(connected, wifi, mobile);
        if (!newConnectedNetworks.equals(connectedNetworks)) {
            updateConnectedNetworksAndNotifyObservers(newConnectedNetworks);
        }
    }

    private void updateConnectedNetworksAndNotifyObservers(ConnectedNetworks newConnectedNetworks) {
        connectedNetworks = newConnectedNetworks;
        Message message = new Message.Builder().content(newConnectedNetworks).build();
        Event event = new Event.Builder(R.id.onNetworkStateChanged).message(message)
                .senderActorAddress(R.id.addressBroadcastReceiver).build();
        notifyObservers(event);
    }

    @Override
    public void onClear() {
        connectedNetworks = null;
    }

    /**
     * a Class that holds the states of the available networks and it's availability
     */
    public static class ConnectedNetworks {
        /**
         * check the wifi network connectivity
         */
        final boolean wifi;
        /**
         * check if the mobile data / network is available
         */
        final boolean mobile;
        /**
         * check if any network is available on the device
         */
        public final boolean any;

        ConnectedNetworks(boolean any, boolean wifi, boolean mobile) {
            this.any = any;
            this.wifi = wifi;
            this.mobile = mobile;
        }

        @Override
        public boolean equals(Object o) {
            boolean sameInstance = (o != null && o instanceof ConnectedNetworks);
            if (sameInstance) {
                ConnectedNetworks cn = (ConnectedNetworks) o;
                sameInstance = (wifi == cn.wifi) && (mobile == cn.mobile) && (any == cn.any);
            }
            return sameInstance;
        }
    }
}
