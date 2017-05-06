package com.base.abstraction.actors.registries;

import android.support.annotation.NonNull;

import com.base.abstraction.messaging.Mailbox;
import com.base.abstraction.commands.Command;
import com.base.abstraction.events.Event;
import com.base.abstraction.logs.Logger;

/**
 * a {@link Mailbox} that does nothing but printing the tasks received, this acts as a {@code null}
 * {@link Mailbox}
 * <p>
 * Created by Ahmed Adel on 10/27/2016.
 */
class EmptyMailbox extends Mailbox<Event> {

    /**
     * create a {@link Mailbox} to manage tasks handling
     */
    EmptyMailbox() {
        super(createEmptyCommand());
    }

    @NonNull
    private static Command<Event, Void> createEmptyCommand() {
        return new Command<Event, Void>() {
            @Override
            public Void execute(Event event) {
                Logger.getInstance().error(EmptyMailbox.class, "no Mailbox to handle : " + event);
                return null;
            }
        };
    }
}
