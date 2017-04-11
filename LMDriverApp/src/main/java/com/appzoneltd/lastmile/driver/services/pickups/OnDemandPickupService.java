package com.appzoneltd.lastmile.driver.services.pickups;

import android.content.Intent;

import com.appzoneltd.lastmile.driver.R;
import com.base.abstraction.annotations.interfaces.Address;
import com.base.abstraction.annotations.interfaces.Executable;
import com.base.abstraction.events.Message;
import com.base.abstraction.system.AppResources;
import com.base.presentation.base.services.AbstractService;
import com.base.presentation.base.services.ServiceStartCommandParams;
import com.entities.cached.pickup.OnDemandPickupRequestAssignedPayload;

/**
 * a service that handles On-Demand pickup-assigned notification response to
 * server
 * <p>
 * Created by Ahmed Adel on 2/19/2017.
 */
@Address(R.id.addressOnDemandPickupService)
public class OnDemandPickupService extends AbstractService<OnDemandPickupModel> {

    @Executable({R.id.onServiceStarted, R.id.onServiceStartedAgain})
    void onStart(Message message) {

        ServiceStartCommandParams p = message.getContent();
        Intent intent = p.getIntent();

        String key = AppResources.string(R.string.INTENT_KEY_ON_DEMAND_NOTIFICATION_ACTION);
        request(key, payload(intent).getPackageId(), actionId(intent, key));

    }

    private OnDemandPickupRequestAssignedPayload payload(Intent intent) throws ClassCastException {
        String key = AppResources.string(R.string.INTENT_KEY_ON_DEMAND_PAYLOAD);
        OnDemandPickupRequestAssignedPayload payload;
        payload = (OnDemandPickupRequestAssignedPayload) intent.getSerializableExtra(key);
        return payload;
    }

    private long actionId(Intent intent, String key) throws UnsupportedOperationException {
        long actionId = intent.getLongExtra(key, 0);
        if (actionId == 0) {
            throw new UnsupportedOperationException("no " + key + " set in the Intent");
        }
        return actionId;
    }

    private void request(String key, long packageId, long actionId)
            throws UnsupportedOperationException {

        if (actionId == R.string.ACTION_ON_DEMAND_NOTIFICATION_ACCEPT) {
            getModel().driverPickupAccept.request(packageId);
        } else if (actionId == R.string.ACTION_ON_DEMAND_NOTIFICATION_REJECT) {
            getModel().driverPickupRejection.request(packageId);
        } else {
            throw new UnsupportedOperationException(key + " : undefined");
        }
    }

}
