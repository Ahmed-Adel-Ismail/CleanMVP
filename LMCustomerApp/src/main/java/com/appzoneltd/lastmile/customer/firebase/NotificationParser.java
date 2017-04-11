package com.appzoneltd.lastmile.customer.firebase;

import com.base.abstraction.commands.Command;
import com.base.cached.RemoteMessageData;
import com.entities.Notification;

/**
 * Created by Wafaa on 11/20/2016.
 */

public class NotificationParser implements
        Command<RemoteMessageData, Notification> {

    @Override
    public Notification execute(RemoteMessageData data) {
        Notification notification = new Notification();
        notification.setNotificationItemTitle(data.getItemTitle());
        notification.setNotificationItemBody(data.getItemBody());
        notification.setNotificationTitle(data.getTitle());
        notification.setNotificationBody(data.getBody());
        notification.setType(data.getType());
        notification.setPayload(data.getPayload());
        long time = data.getTime();
        notification.setTime(time);
        return notification;
    }

}
