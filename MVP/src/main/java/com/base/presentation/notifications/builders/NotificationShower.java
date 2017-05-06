package com.base.presentation.notifications.builders;

import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.content.Context;

import com.base.abstraction.commands.Command;
import com.base.abstraction.system.App;

import java.util.Random;

/**
 * a class to show the Notification based on the passed {@link Builder}
 * <p>
 * Created by Wafaa on 11/22/2016.
 */
public class NotificationShower implements Command<Notification, Long> {

    private int notificationId;
    private String notificationTag;


    public NotificationShower() {
        notificationId = new Random().nextInt() * -1;
    }

    public NotificationShower(String notificationTag, int notificationId) {
        this.notificationTag = notificationTag;
        this.notificationId = notificationId;
    }


    @Override
    public Long execute(Notification notification) {
        NotificationManager manager = (NotificationManager) App.getInstance()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(notificationTag, notificationId, notification);
        return (long) notificationId;
    }
}
