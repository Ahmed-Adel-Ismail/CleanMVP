package com.appzoneltd.lastmile.driver.firebase;

import com.base.abstraction.commands.Command;
import com.base.abstraction.events.Message;
import com.base.abstraction.logs.Logger;
import com.base.presentation.notifications.PushNotificationMessageHandler;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


/**
 * a class to handle push notifications from FCM
 * <p>
 * Created by Ahmed Adel on 11/21/2016.
 */
public class PushNotificationService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Logger.getInstance().error(getClass(), "onMessageReceived() : " + remoteMessage);
        super.onMessageReceived(remoteMessage);
        new PushNotificationMessageHandler()
                .eventIdGenerator(PushNotifications.FACTORY)
                .execute(remoteMessage)
                .onComplete(onMessageHandled())
                .onException(onMessageHandledException());
    }


    private Command<Message, Void> onMessageHandled() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                PushNotifications.FACTORY.getTypeById(message.getId()).execute(message);
                return null;
            }
        };
    }


    private Command<Throwable, Void> onMessageHandledException() {
        return new Command<Throwable, Void>() {
            @Override
            public Void execute(Throwable e) {
                Logger.getInstance().error(PushNotificationService.class, e);
                return null;
            }
        };
    }


}
