package com.base.abstraction.exceptions.failures;

import com.base.abstraction.actors.base.MailboxClient;
import com.base.abstraction.messaging.AbstractMailbox;
import com.base.abstraction.messaging.Mailbox;
import com.base.abstraction.actors.registries.ActorSystem;
import com.base.abstraction.commands.Command;
import com.base.abstraction.events.Event;
import com.base.abstraction.events.Message;
import com.base.abstraction.exceptions.base.AbstractException;
import com.base.abstraction.failures.FailureHandler;
import com.base.abstraction.failures.UnhandledFailureException;
import com.base.abstraction.logs.Logger;
import com.base.abstraction.system.App;

/**
 * This {@link RuntimeException} is thrown when an event happens that may cause the whole
 * application to fail, to handle it, you will need to declare a {@link FailureHandler},
 * and add it to the {@link ActorSystem} in your
 * {@link App#getActorSystem()} method for your application
 * <p>
 * so generally, throw this exception if there is an operation that requires  a
 * {@link FailureHandler} class action ... the {@code thrower} of this {@link RuntimeException}
 * will get the {@link FailureHandler} mapped to it's {@link #getFailureHandlerAddress()} in the
 * {@link ActorSystem} to fix the issue ... if it did not find any {@link FailureHandler}
 * instances found, a {@link UnhandledFailureException} will be thrown (which should not happen
 * in a production environment
 * <p>
 * when this exception is handled, the handler should call
 * {@link #execute(Event)} and pass to it it's own {@link MailboxClient} as a
 * {@link Message#getContent() Message content} in an {@link Event} ... at this point the
 * {@link FailureHandler} will take responsibility to fix the
 * issue and will then {@link Mailbox#push(Object) push} the
 * last task back to the client's {@link Mailbox} so it will repeat
 * it's last {@link Mailbox#execute(Object)} again (after fixing the issue)
 * <p>
 * note that this {@link RuntimeException} logs itself by default in constructor (if
 * logging is enabled)
 * <p>
 * for all sub-classes, they should provide no-args (default) constructor
 * <p>
 * Created by Ahmed Adel on 10/23/2016.
 *
 * @see FailureHandler
 * @see UnhandledFailureException
 * @see ActorSystem
 */
public abstract class Failure extends AbstractException implements Command<Event, Void> {

    private boolean next;
    private Failure nextFailure;
    private AbstractMailbox<Event> failureHandlerMailbox;

    {
        long handlerAddress = getFailureHandlerAddress();
        this.failureHandlerMailbox = App.getInstance().getActorSystem().get(handlerAddress);
        this.next = true;
        this.nextFailure = this;
        Logger.getInstance().error(Failure.class.getSimpleName(), getClass());
    }

    /**
     * create a {@link Failure}
     */
    Failure() {
    }

    /**
     * create a {@link Failure}
     *
     * @param detailMessage the message to be displayed
     */
    Failure(String detailMessage) {
        super(detailMessage);
    }

    /**
     * handle the failure of this {@link Failure}
     *
     * @param event the {@link Event} that holds {@link MailboxClient} that will host the handling
     * @throws UnhandledFailureException if the type of tasks between {@link MailboxClient}
     *                                   and {@link FailureHandler} mismatched, or if there was no
     *                                   {@link FailureHandler} set by the {@code Object} that {@code threw} this
     *                                   {@link Failure}
     */
    public Void execute(Event event) throws UnhandledFailureException {

        while (next) {
            try {
                handleNextFailure(event);
            } catch (Failure e) {
                moveToNextFailure(e);
            } catch (Throwable e) {
                throw new UnhandledFailureException(e);
            }
        }
        return null;
    }

    private void handleNextFailure(Event event) {
        nextFailure.failureHandlerMailbox.execute(event);
        next = false;
        nextFailure = null;
    }


    private void moveToNextFailure(Failure e) {
        next = true;
        nextFailure = e;
    }

    /**
     * get the address of the {@link FailureHandler}
     *
     * @return the {@link FailureHandler} address in {@link App#getActorSystem()}
     */
    protected abstract long getFailureHandlerAddress();


}
