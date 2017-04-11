package com.appzoneltd.lastmile.customer.firebase;

import android.support.v4.app.NotificationCompat;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.features.notificationlist.host.NotificationActivity;
import com.base.abstraction.events.Event;
import com.base.abstraction.events.Message;
import com.base.abstraction.exceptions.CheckedReferenceClearedException;
import com.base.abstraction.exceptions.NotSavedInPreferencesException;
import com.base.abstraction.logs.Logger;
import com.base.abstraction.serializers.JsonListSaver;
import com.base.abstraction.serializers.JsonSetLoader;
import com.base.abstraction.system.App;
import com.base.abstraction.system.Preferences;
import com.base.cached.RemoteMessageData;
import com.base.interfaces.TypedValuable;
import com.base.presentation.notifications.builders.NotificationShower;
import com.base.presentation.notifications.builders.PendingIntentGenerator;
import com.base.presentation.notifications.builders.PushNotificationBuilder;
import com.base.presentation.notifications.builders.PushNotificationTuple;
import com.entities.Notification;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.TreeSet;

/**
 * Created by Wafaa on 11/14/2016.
 */
public class PushNotificationService extends FirebaseMessagingService {

    private TypedValuable<Long, String> generator = NotificationEventIdGenerator.FACTORY;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        parseRemoteMessageAndNotify(remoteMessage);
    }

    private void parseRemoteMessageAndNotify(RemoteMessage remoteMessage) {
        Logger.getInstance().error(getClass(), remoteMessage.getData());
        RemoteMessageData data = new RemoteMessageData(remoteMessage.getData());
        if (data.size() != 0) {
            notifyAndSave(data);
        }
    }

    private void notifyAndSave(RemoteMessageData data) {
        long typeId = generator.getType(data.getType());
        Notification notification = new NotificationParser().execute(data);
        if (notification != null) {
            Logger.getInstance().error(PushNotificationService.class + "[PushNotification: ",
                    notification.getPayload() + " ]");
            saveNotificationCounter();
            saveNewNotification(notification);
            try {
                renderNotification(notification);
            } catch (CheckedReferenceClearedException ex) {
                Logger.getInstance().exception(ex);
            }
            notifyNotificationReceivedEvent(typeId, notification);
        }
    }


    private TreeSet<Notification> saveNewNotification(Notification notification) {
        TreeSet<Notification> notifications;
        JsonSetLoader<Notification> loader =
                new JsonSetLoader<>(R.string.PREFS_KEY_NOTIFICATION_LIST);
        try {
            notifications = loader.execute(Notification.class);
        } catch (NotSavedInPreferencesException ex) {
            notifications = new TreeSet<>();
        }
        notification.setTimestamp(System.currentTimeMillis());
        notifications.add(notification);
        JsonListSaver<Notification> saver = new JsonListSaver<>(
                R.string.PREFS_KEY_NOTIFICATION_LIST);
        saver.execute(notifications);
        return notifications;
    }

    private void renderNotification(Notification notification) throws CheckedReferenceClearedException {
        if (!App.getInstance().isInForeground()) {
            NotificationCompat.Builder notificationBuilder = new PushNotificationBuilder()
                    .execute(createNotificationTuple(notification));
            new NotificationShower().execute(notificationBuilder.build());
        }
    }

    private PushNotificationTuple createNotificationTuple(Notification notification) {
        if (notification != null) {

            PushNotificationTuple.Builder builder = new PushNotificationTuple.Builder();
            builder.title(notification.getNotificationTitle());
            builder.body(notification.getNotificationBody());
            builder.smallIcon(R.drawable.notification_status);
            builder.largeIcon(R.mipmap.ic_launcher);
            builder.contentPendingIntent(PendingIntentGenerator.forActivity()
                    .execute(NotificationActivity.class));
            return builder.build();
        }
        return null;
    }

    private void saveNotificationCounter() {
        Long counter = Preferences.getInstance().load(R.string.PREFS_KEY_NOTIFICATION_COUNTER, 0L);
        Preferences.getInstance().save(R.string.PREFS_KEY_NOTIFICATION_COUNTER, ++counter);
    }

    private void notifyNotificationReceivedEvent(long typeId, Notification notification) {
        Message message = new Message.Builder().id(typeId).content(notification).build();
        Event event = new Event.Builder(R.id.onNotificationReceived)
                .message(message)
                .receiverActorAddresses(R.id.addressActivity)
                .build();
        App.getInstance().getActorSystem().send(event);
    }
}
