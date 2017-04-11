package com.base.presentation.notifications;

import com.base.abstraction.commands.Command;
import com.base.abstraction.concurrency.Future;
import com.base.abstraction.events.Event;
import com.base.abstraction.events.Message;
import com.base.abstraction.exceptions.TestException;
import com.base.abstraction.logs.Logger;
import com.base.abstraction.system.App;
import com.base.cached.RemoteMessageData;
import com.base.interfaces.TypedValuable;
import com.base.presentation.R;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * a class to handle push notifications with the default manner, it returns a {@link Future}
 * that will set it's result to {@code true} if the foreground activity was notified,
 * or will set it's result to {@code false} if no foreground Activity was found,
 * which will need the client class to show a notification
 * <p>
 * Created by Ahmed Adel on 12/19/2016.
 */
public class PushNotificationMessageHandler implements Command<RemoteMessage, Future<Message>> {

    private TypedValuable<Long, String> eventIdGenerator;

    /**
     * set the {@link TypedValuable} that returns an {@link Event} Id based on the
     * {@code String} passed, this {@code String} will be retrieved from
     * {@link RemoteMessageData#getType()}
     *
     * @param generator the {@link TypedValuable} that will generate an {@link Event} id from
     *                  {@link RemoteMessageData#getType()}
     * @return {@code this} instance for chaining
     */
    public PushNotificationMessageHandler eventIdGenerator(TypedValuable<Long, String> generator) {
        this.eventIdGenerator = generator;
        return this;
    }

    @Override
    public Future<Message> execute(RemoteMessage remoteMessage) {
        Future<Message> future = new Future<>();
        try {

            Message message = createEventMessage(remoteMessage);

            if (App.getInstance().isInForeground()) {
                notifyForegroundActivity(message);
            }

            future.setResult(message);


        } catch (Throwable e) {
            Logger.getInstance().exception(e);
            future.setException(e);
        }
        return future;
    }

    private Message createEventMessage(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        RemoteMessageData remoteMessageData = new RemoteMessageData(data);
        return new Message.Builder()
                .id(createEventId(remoteMessageData))
                .content(remoteMessageData)
                .build();
    }

    protected long createEventId(RemoteMessageData remoteMessageData) {
        long eventId = 0;

        if (eventIdGenerator != null) {
            eventId = generateEventId(remoteMessageData, eventId);
        }

        if (eventId == 0) {
            eventId = R.id.onNotificationReceived;
        }

        return eventId;
    }

    private long generateEventId(RemoteMessageData remoteMessageData, long eventId) {
        try {
            eventId = eventIdGenerator.getType(remoteMessageData.getType());
        } catch (UnsupportedOperationException e) {
            Logger.getInstance().exception(e);
        } catch (Throwable e) {
            new TestException().execute(e);
        }
        return eventId;
    }

    private void notifyForegroundActivity(Message message) {
        Event event = new Event.Builder(message.getId())
                .message(message)
                .receiverActorAddresses(R.id.addressActivity)
                .build();

        App.getInstance().getActorSystem().send(event);
    }


}
