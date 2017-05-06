package com.base.presentation.notifications.builders;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import com.base.abstraction.commands.Command;
import com.base.abstraction.system.App;
import com.base.abstraction.system.AppResources;

/**
 * a class that build notification in notification area
 * <p>
 * Created by Wafaa on 11/22/2016.
 */

public class PushNotificationBuilder implements Command<PushNotificationTuple, NotificationCompat.Builder> {

    @Override
    public NotificationCompat.Builder execute(PushNotificationTuple tuple) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(App.getInstance())
                .setContentTitle(tuple.title)
                .setContentText(tuple.body);

        if (tuple.hasSmallIcon()) {
            builder.setSmallIcon(tuple.smallIcon);
        }

        if (tuple.hasLargeIcon()) {
            builder.setLargeIcon(decodeLargeIcon(tuple));
        }

        if (tuple.hasPositiveAction()) {
            builder.addAction(createAction(tuple.positiveAction));
        }

        if (tuple.hasNegativeAction()) {
            builder.addAction(createAction(tuple.negativeAction));
        }

        return builder.setContentIntent(tuple.contentPendingIntent);
    }

    private Bitmap decodeLargeIcon(PushNotificationTuple params) {
        return BitmapFactory.decodeResource(AppResources.getResources(),
                params.largeIcon);
    }

    @NonNull
    private NotificationCompat.Action createAction(NotificationAction action) {
        return new NotificationCompat.Action(action.icon, action.label, action.pendingIntent);
    }


}
