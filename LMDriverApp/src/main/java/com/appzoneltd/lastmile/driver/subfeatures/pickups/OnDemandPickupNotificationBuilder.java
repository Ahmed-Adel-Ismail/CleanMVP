package com.appzoneltd.lastmile.driver.subfeatures.pickups;


import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.NotificationCompat;

import com.appzoneltd.lastmile.driver.R;
import com.base.abstraction.commands.Command;
import com.base.abstraction.events.Message;
import com.base.abstraction.system.App;
import com.base.abstraction.system.AppResources;
import com.base.presentation.notifications.builders.NotificationAction;
import com.base.presentation.notifications.builders.PushNotificationBuilder;
import com.base.presentation.notifications.builders.PushNotificationTuple;
import com.base.presentation.views.dialogs.EventDialogBuilder;

import java.io.Serializable;

/**
 * a class to generate a push-notification for On Demand pickup assigned
 * <p>
 * Created by Ahmed Adel on 2/19/2017.
 */
class OnDemandPickupNotificationBuilder implements Command<Message, Notification> {

    private String notificationTag;

    OnDemandPickupNotificationBuilder(String notificationTag) {
        this.notificationTag = notificationTag;
    }

    @Override
    public Notification execute(Message message) {

        PushNotificationTuple tuple = createPushNotificationTuple(message);

        return new PushNotificationBuilder()
                .execute(tuple)
                .setTicker(tuple.title)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(tuple.body))
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_MAX)
                .build();
    }

    @NonNull
    private PushNotificationTuple createPushNotificationTuple(Message message) {
        EventDialogBuilder dialog = new OnDemandPickupDialogBuilder().execute(message);
        return new PushNotificationTuple.Builder()
                .title(dialog.getTitle())
                .body(dialog.getMessage())
                .smallIcon(R.drawable.notification_status)
                .largeIcon(R.mipmap.ic_launcher)
                .positiveAction(positiveAction(dialog.getPositiveText(), dialog.getTag()))
                .negativeAction(negativeAction(dialog.getNegativeText(), dialog.getTag()))
                .build();
    }


    private NotificationAction positiveAction(String positiveText, Object payload) {
        return new NotificationAction.Builder()
                .icon(R.drawable.on_demand_accept_action)
                .label(positiveText)
                .pendingIntent(pendingIntent(R.string.ACTION_ON_DEMAND_NOTIFICATION_ACCEPT, payload))
                .build();

    }

    private NotificationAction negativeAction(String negativeText, Object payload) {
        return new NotificationAction.Builder()
                .icon(R.drawable.on_demand_reject_action)
                .label(negativeText)
                .pendingIntent(pendingIntent(R.string.ACTION_ON_DEMAND_NOTIFICATION_REJECT, payload))
                .build();
    }

    @NonNull
    private PendingIntent pendingIntent(@StringRes int action, Object payload) {
        Intent intent = new Intent(AppResources.string(action));
        intent.putExtras(bundle((Serializable) payload));
        int requestCode = (int) (Math.random() * 1000);
        return PendingIntent.getBroadcast(App.getInstance(), requestCode, intent, 0);

    }

    private Bundle bundle(Serializable payload) {
        Bundle bundle = new Bundle();
        bundle.putString(AppResources.string(R.string.INTENT_KEY_NOTIFICATION_TAG), notificationTag);
        bundle.putSerializable(AppResources.string(R.string.INTENT_KEY_ON_DEMAND_PAYLOAD), payload);
        return bundle;
    }


}
