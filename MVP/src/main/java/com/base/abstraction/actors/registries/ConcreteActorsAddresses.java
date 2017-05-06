package com.base.abstraction.actors.registries;

import com.base.abstraction.actors.base.Actor;
import com.base.abstraction.messaging.AbstractMailbox;
import com.base.abstraction.events.Event;

import java.util.HashMap;

/**
 * a {@link ActorsAddresses} for classes that can be held in memory ... like non-context related
 * classes
 * <p>
 * Created by Ahmed Adel on 10/27/2016.
 */
class ConcreteActorsAddresses extends ActorsAddresses {

    private final HashMap<Long, Actor> map;

    ConcreteActorsAddresses() {
        this.map = new HashMap<>();
    }

    @Override
    public Actor put(Long address, Actor actor) {
        if (actor == null) {
            actor = nullActor;
        }
        map.put(address, actor);
        return actor;
    }

    @Override
    public AbstractMailbox<Event> get(Long address) {
        Actor actor = map.get(address);
        if (actor == null) {
            actor = put(address, nullActor);
        }
        return actor.getMailbox();
    }

}
