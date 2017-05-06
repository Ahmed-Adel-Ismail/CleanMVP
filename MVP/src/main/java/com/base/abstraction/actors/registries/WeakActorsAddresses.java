package com.base.abstraction.actors.registries;

import com.base.abstraction.actors.base.Actor;
import com.base.abstraction.messaging.AbstractMailbox;
import com.base.abstraction.events.Event;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * a {@link ActorsAddresses} for classes that are related to {@link android.content.Context},
 * like {@link android.app.Activity} and {@link android.app.Fragment}
 * <p>
 * Created by Ahmed Adel on 10/27/2016.
 */
class WeakActorsAddresses extends ActorsAddresses {

    private final HashMap<Long, WeakReference<Actor>> map;

    WeakActorsAddresses() {
        this.map = new HashMap<>();
    }

    @Override
    public Actor put(Long address, Actor actor) {
        if (actor == null) {
            actor = nullActor;
        }
        map.put(address, new WeakReference<>(actor));
        return actor;
    }

    @Override
    public AbstractMailbox<Event> get(Long address) {
        WeakReference<Actor> actorRef = map.get(address);
        Actor actor;
        if (actorRef == null || (actor = actorRef.get()) == null) {
            actor = put(address, nullActor);
        }
        return actor.getMailbox();
    }


}
