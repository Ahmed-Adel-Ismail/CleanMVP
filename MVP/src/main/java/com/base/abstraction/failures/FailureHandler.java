package com.base.abstraction.failures;

import com.base.abstraction.actors.base.Actor;
import com.base.abstraction.actors.base.MailboxClient;
import com.base.abstraction.messaging.AbstractMailbox;
import com.base.abstraction.messaging.Mailbox;
import com.base.abstraction.actors.registries.ActorSystem;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.WeakCommandWrapper;
import com.base.abstraction.events.Event;
import com.base.abstraction.exceptions.failures.Failure;

/**
 * a class that handles the failure through any point of the flow, when a failure happens (like
 * an exception is thrown or a server response that requires certain action before it proceeds),
 * the failed class should {@code throw} {@link Failure}, which is handled by this class
 * <p>
 * this class communicates with the rest of the application throw {@link Event}, where
 * it takes an {@link Event} that holds the  {@link MailboxClient} as it's
 * {@link com.base.abstraction.events.Message}, and takes control of
 * it's flow to handle the failure
 * <p>
 * Created by Ahmed Adel on 10/23/2016.
 *
 * @see Failure
 * @see ActorSystem
 */
public abstract class FailureHandler implements Command<Event, Void>, Actor {

    private final WeakCommandWrapper<Event, Void> wrapper;
    private final Mailbox<Event> mailbox;


    protected FailureHandler() {
        wrapper = new WeakCommandWrapper<>(this);
        mailbox = new Mailbox<>(wrapper);
    }

    @Override
    public AbstractMailbox<Event> getMailbox() {
        return mailbox;
    }
}
