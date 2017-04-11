package com.appzoneltd.lastmile.driver.subfeatures.pickups;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.appzoneltd.lastmile.driver.R;
import com.appzoneltd.lastmile.driver.services.pickups.OnDemandPickupService;
import com.base.abstraction.annotations.interfaces.Executable;
import com.base.abstraction.annotations.interfaces.Load;
import com.base.abstraction.commands.executors.Executor;
import com.base.abstraction.system.App;
import com.base.abstraction.system.AppResources;

/**
 * a {@link Executor} that handles actions done by notifications broadcasts
 * <p>
 * Created by Ahmed Adel on 2/19/2017.
 */
@Load
public class OnDemandPickupActionsHandler extends Executor<Intent> {

    @Executable(R.string.ACTION_ON_DEMAND_NOTIFICATION_ACCEPT)
    void onDemandNotificationAccept(Intent intent) {
        hideNotificationAndStartIntent(intent, R.string.ACTION_ON_DEMAND_NOTIFICATION_ACCEPT);
    }


    @Executable(R.string.ACTION_ON_DEMAND_NOTIFICATION_REJECT)
    void onDemandNotificationReject(Intent intent) {
        hideNotificationAndStartIntent(intent, R.string.ACTION_ON_DEMAND_NOTIFICATION_REJECT);
    }

    private void hideNotificationAndStartIntent(Intent intent, long value) {
        hideNotification(intent);
        Intent serviceIntent = new Intent(App.getInstance(), OnDemandPickupService.class);
        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            bundle = new Bundle();
        }
        String key = AppResources.string(R.string.INTENT_KEY_ON_DEMAND_NOTIFICATION_ACTION);
        bundle.putLong(key, value);
        serviceIntent.putExtras(bundle);
        App.getInstance().startService(serviceIntent);
    }

    private void hideNotification(Intent intent) {
        String tag = intent.getStringExtra(AppResources.string(R.string.INTENT_KEY_NOTIFICATION_TAG));
        int id = AppResources.integer(R.integer.onDemandPickupNotificationId);
        NotificationManager manager = (NotificationManager) App.getInstance()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(tag, id);

    }

}
