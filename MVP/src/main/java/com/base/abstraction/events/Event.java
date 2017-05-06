package com.base.abstraction.events;

import com.base.abstraction.actors.base.ActorAddressee;
import com.base.abstraction.interfaces.Immutable;
import com.base.abstraction.interfaces.Relationable;
import com.base.abstraction.system.ResourcesReader;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;


/**
 * an Object used between Classes as a way of communication / notification,
 * for every Event Producer, they should set a {@link #senderActorAddress}, and if this
 * event is targeting certain consumer(s), you should specify the consumer(s) ID
 * as well ... by default all events are consumable by
 * any Consumer,
 * and the producer ID has the default vale of {@link #SENDER_UNKNOWN}
 * <p/>
 * Created by Ahmed Adel on 8/31/2016.
 */
public final class Event implements Relationable, Immutable, Serializable {

    private static final long SENDER_UNKNOWN = 0;
    private final long id;
    private final Message message;
    private final long senderActorAddress;
    private List<Long> receiverActorAddresses;

    private Event(long id, long senderActorAddress, Message message,
                  List<Long> receiverActorAddresses) {
        this.id = id;
        this.senderActorAddress = senderActorAddress;
        this.message = message;
        this.receiverActorAddresses = receiverActorAddresses;

    }


    @Override
    public long getId() {
        return id;
    }

    public long getSenderActorAddress() {
        return senderActorAddress;
    }

    public long[] getReceiverActorAddresses() {
        if (receiverActorAddresses != null && !receiverActorAddresses.isEmpty()) {
            return getReceiverActorAddressesAsArray();
        } else {
            return null;
        }

    }

    private long[] getReceiverActorAddressesAsArray() {
        int receiversCounts = receiverActorAddresses.size();
        long[] longs = new long[receiversCounts];
        for (int i = 0; i < receiversCounts; i++) {
            longs[i] = receiverActorAddresses.get(i);
        }
        return longs;
    }

    public boolean isConsumable(long consumerAddress) {
        return receiverActorAddresses == null || receiverActorAddresses.contains(consumerAddress);
    }

    /**
     * get the available {@link Message} attached to this {@link Event}
     *
     * @param <T> the type of the {@link Message}
     * @return the {@link Message} instance casted to the expected type
     * @throws ClassCastException if the expected {@link Message}t ype is different
     *                            than the real {@link Message} type attached
     */
    @SuppressWarnings("unchecked")
    public <T extends Message> T getMessage() throws ClassCastException {
        return (T) message;
    }

    /**
     * check if the current {@link Event} can be used in serialization operations
     *
     * @return {@code true} if this {@link Event} {@link Message#isSerializable()}
     * returned {@code true}, or if there is no {@link Message} at all
     */
    public boolean isSerializable() {
        return message == null || message.isSerializable();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{id=");
        builder.append(new ResourcesReader().execute(id));
        builder.append(", senderActorAddress=");
        builder.append(new ResourcesReader().execute(senderActorAddress));
        builder.append(", message=");
        builder.append(message);
        builder.append("}");
        return builder.toString();
    }

    /**
     * the old way to build {@link Event}, you can still use this approach if your class
     * does not implement {@link ActorAddressee} interface
     *
     * @see EventBuilder
     */
    public static class Builder {

        private long id;
        private long senderActorAddress = SENDER_UNKNOWN;
        private final List<Long> receiverActorAddresses = new LinkedList<>();
        private Message message;

        public Builder(long id) {
            this.id = id;
        }

        public Builder senderActorAddress(long senderActorAddress) {
            this.senderActorAddress = senderActorAddress;
            return this;
        }

        public Builder receiverActorAddresses(long... receiverActorAddresses) {
            if (receiverActorAddresses != null && receiverActorAddresses.length > 0) {
                addReceiverActorAddresses(receiverActorAddresses);
            }
            return this;
        }

        private void addReceiverActorAddresses(long[] receiverActorAddresses) {
            for (long l : receiverActorAddresses) {
                this.receiverActorAddresses.add(l);
            }
        }

        public Builder message(Message message) {
            this.message = message;
            return this;
        }

        public Event build() {
            return new Event(id, senderActorAddress, message,
                    (receiverActorAddresses.isEmpty()) ? null : receiverActorAddresses);
        }

    }

}

