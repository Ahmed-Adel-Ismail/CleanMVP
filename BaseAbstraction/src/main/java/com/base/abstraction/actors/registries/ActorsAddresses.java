package com.base.abstraction.actors.registries;

import com.base.abstraction.actors.base.Actor;
import com.base.abstraction.messaging.AbstractMailbox;
import com.base.abstraction.aggregates.KeyAggregateAddable;
import com.base.abstraction.aggregates.KeyAggregateGettable;
import com.base.abstraction.events.Event;

import java.lang.ref.WeakReference;

/**
 * a Class that holds the addresses to {@link Actor} implementers, it is safe to store a
 * {@link android.content.Context} since all the values are stored in as {@link WeakReference}
 * <p>
 * Created by Ahmed Adel on 10/27/2016.
 */
abstract class ActorsAddresses implements
        KeyAggregateAddable<Long, Actor>,
        KeyAggregateGettable<Long, AbstractMailbox<Event>> {

    final NullActor nullActor;

    ActorsAddresses() {
        nullActor = new NullActor();
    }
}
