package com.base.abstraction.actors.base;

import com.base.abstraction.events.Event;

/**
 * implement this interface if your Class will act as an Actor, this design is still ongoing for
 * now, so not all implementers are "actual" actors, ongoing development for the Actor Model
 * architecture
 * <p>
 * Created by Ahmed Adel on 10/27/2016.
 */
public interface Actor extends MailboxClient<Event>, ActorAddressee {
}
