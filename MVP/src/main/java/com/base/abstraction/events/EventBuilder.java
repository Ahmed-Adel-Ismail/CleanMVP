package com.base.abstraction.events;

import android.support.annotation.Nullable;

import com.base.abstraction.actors.base.ActorAddressee;
import com.base.abstraction.commands.Command;

/**
 * a class to create {@link Event} , it creates an {@link Event} with it's
 * {@link Event#getSenderActorAddress() Producer Id} as
 * this {@link ActorAddressee#getActorAddress()} passed to the {@link #execute(ActorAddressee)}
 * method
 * <p/>
 * Created by Ahmed Adel on 10/16/2016.
 */
public class EventBuilder implements Command<ActorAddressee, Event> {

    private long eventId;
    private Message message;
    private long[] receiverActorAddresses;

    /**
     * create an empty {@link Event} with just an id
     *
     * @param eventId the event id
     */
    public EventBuilder(long eventId) {
        this.eventId = eventId;
    }

    /**
     * create an detailed {@link Event}
     *
     * @param eventId                the id of the event
     * @param message                the {@link Message} of the event
     * @param receiverActorAddresses optional .. the target {@link ActorAddressee} classes
     */
    public EventBuilder(long eventId, Message message, long... receiverActorAddresses) {
        this(eventId);
        this.message = message;
        this.receiverActorAddresses = receiverActorAddresses;
    }

    /**
     * create an {@link Event} with a {@link Message} that holds no message id
     *
     * @param eventId                the id of the event
     * @param messageContent         the Object set as a content for the {@link Message}
     * @param receiverActorAddresses optional .. the target {@link ActorAddressee} classes
     */
    public EventBuilder(long eventId, Object messageContent, long... receiverActorAddresses) {
        this(eventId);
        if (messageContent != null) {
            message = new Message.Builder().content(messageContent).build();
        }
        this.receiverActorAddresses = receiverActorAddresses;
    }

    /**
     * create an {@link Event} with the passed {@link ActorAddressee#getActorAddress()} as the
     * {@link Event#getSenderActorAddress()} value ... it is save to pass any class (even Context
     * related classes) since this is a pure method
     *
     * @param actorAddressee the {@link ActorAddressee} of the invoker class, or {@code null}
     * @return the {@link Event} to be used
     */
    @Override
    public Event execute(@Nullable ActorAddressee actorAddressee) {
        long actorAddress = (actorAddressee != null) ? actorAddressee.getActorAddress() : 0;
        return new Event.Builder(eventId)
                .senderActorAddress(actorAddress)
                .message(message)
                .receiverActorAddresses(receiverActorAddresses)
                .build();
    }

}
