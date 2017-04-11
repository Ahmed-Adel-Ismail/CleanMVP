package com.base.abstraction.system;

import com.base.abstraction.messaging.AbstractMailbox;
import com.base.abstraction.messaging.Mailbox;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.params.Args2;
import com.base.abstraction.events.Event;

/**
 * a class that is responsible for sending Events to {@link com.base.abstraction.actors.base.Actor}
 * classes throw there {@link Mailbox} address
 * <p>
 * Created by Ahmed Adel on 11/20/2016.
 */
public class MailboxSender implements Command<Args2<Long, Event>, AbstractMailbox<Event>> {

    /**
     * send an {@link Event} to the {@link Mailbox} with the address that initialized this instance
     *
     * @param args2 the {@link Args2} object, the first should be the address of the
     *             {@link com.base.abstraction.actors.base.Actor}, the second should be the
     *             {@link Event} to be sent to it
     * @return the {@link Mailbox} that received the {@link Event}
     */
    @Override
    public AbstractMailbox<Event> execute(Args2<Long, Event> args2) {
        return App.getInstance().getActorSystem().get(args2.first()).execute(args2.second());
    }
}
