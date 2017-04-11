package com.base.abstraction.actors.base;

import com.base.abstraction.messaging.AbstractMailbox;
import com.base.abstraction.messaging.Mailbox;

/**
 * an interface implemented by class that has a {@link Mailbox} in there
 * implementation to handle there tasks
 * <p>
 * Created by Ahmed Adel on 10/23/2016.
 */
public interface MailboxClient<Task> {

    /**
     * get the {@link AbstractMailbox} handling messages / tasks for this class
     *
     * @return the {@link AbstractMailbox} attached to this class
     */
    AbstractMailbox<Task> getMailbox();

}
