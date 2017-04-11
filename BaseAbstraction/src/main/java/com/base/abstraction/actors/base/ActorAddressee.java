package com.base.abstraction.actors.base;

import com.base.abstraction.events.Event;

/**
 * an interface implemented by Classes that will get {@link Event} instances
 * <p>
 * Created by Ahmed Adel on 9/22/2016.
 */
public interface ActorAddressee {

    /**
     * get the address of this Actor Class, you can declare an id in a resources file
     * and return it as a result of this method
     * <p>
     * this method will be invoked to check if the current {@link ActorAddressee} is accepted to
     * get the {@link Event} or not, you can give the same ID to multiple
     * {@link ActorAddressee} Classes if they will be treated the same way (which will cause the
     * last one to be the current Actor presented in this address)
     *
     * @return the address of the actor
     */
    long getActorAddress();
}
