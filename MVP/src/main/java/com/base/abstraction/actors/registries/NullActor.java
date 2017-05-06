package com.base.abstraction.actors.registries;

import com.base.abstraction.R;
import com.base.abstraction.actors.base.Actor;
import com.base.abstraction.messaging.AbstractMailbox;
import com.base.abstraction.events.Event;

/**
 * a class that represents a {@code null} {@link Actor} value
 * <p>
 * Created by Ahmed Adel on 10/27/2016.
 */
class NullActor implements Actor {

    private final EmptyMailbox emptyMailbox = new EmptyMailbox();

    @Override
    public AbstractMailbox<Event> getMailbox() {
        return emptyMailbox;
    }

    @Override
    public long getActorAddress() {
        return R.id.addressNullActor;
    }

}
