package com.base.presentation.commands;

import com.base.abstraction.commands.Command;
import com.base.abstraction.events.Event;
import com.base.abstraction.events.Message;
import com.base.abstraction.interfaces.Clearable;
import com.base.abstraction.observer.Observer;
import com.base.abstraction.R;
import com.base.usecases.events.ResponseMessage;

/**
 * A class that handles any {@link Command} that cannot be executed unless a certain request
 * is made and it's response is received
 * <p/>
 * <u>how does this work:</u><br>
 * first you need to create a {@link RequesterCommand} that will handle the request / response
 * logic, and will maintain the state of the request value or failure ... this
 * {@link RequesterCommand} should be added to the
 * {@link com.base.abstraction.events.Event Events} triggered when a response is received
 * <br>
 * then you can preInitialize one (or multiple) instances of this class, which will contain
 * the code that should not be executed unless a successful response is received
 * <br>
 * you can then use this instance as a normal {@link Command} any where
 * <br>
 * you will need to implement {@link #onExecute(Message)}, where you should put your normal
 * implementation for the {@link Command}
 * <br>
 * optionally you can {@code Override} {@link #onRequesting(Message)} if you want to do
 * any logic while the request is being processed by the {@link RequesterCommand}
 * <p/>
 * Created by Ahmed Adel on 10/4/2016.
 */
public abstract class RequestBasedCommand<Return> implements
        Command<Message, Return>,
        Clearable,
        Observer {


    private boolean cleared;
    private RequesterCommand requesterCommand;

    /**
     * createNativeMethod a {@link RequestBasedCommand} that is listening to the passed
     * {@link RequesterCommand}
     *
     * @param requesterCommand the {@link RequesterCommand} to maintain the state of this
     *                         {@link RequestBasedCommand}
     */
    protected RequestBasedCommand(RequesterCommand requesterCommand) {
        this.requesterCommand = requesterCommand;
        requesterCommand.addObserver(this);
    }


    @Override
    public final Return execute(Message p) {
        if (cleared) {
            return null;
        }
        if (requesterCommand.isSuccessfulResponse()) {
            return onExecute(p);
        } else if (requesterCommand.isRequesting()) {
            return onRequesting(p);
        } else {
            requesterCommand.startRequest();
        }
        return null;
    }

    /**
     * executed whenever the {@link com.base.abstraction.events.Event} is triggered, same as
     * {@link Command#execute(Object)}, where this method is executed only if a
     * successful response is received once before by the {@link RequesterCommand}
     *
     * @param message the parameter received in {@link Command#execute(Object)}
     * @return the value to return in {@link Command#execute(Object)}
     */
    protected abstract Return onExecute(Message message);


    /**
     * this method is executed when the {@link Command#execute(Object)} is invoked while
     * the {@link RequesterCommand} is currently busy processing a request
     *
     * @param p the parameter passed to {@link Command#execute(Object)}
     * @return the value expected
     */
    protected Return onRequesting(Message p) {
        // template method
        return null;
    }

    protected Return onFailure(Message message) {
        // template method
        return null;
    }

    @Override
    public void onUpdate(Event event) throws RuntimeException {
        if (event.getId() == R.id.onRequesterCommandEvent) {
            ResponseMessage message = event.getMessage();
            if (message.isSuccessful()) {
                execute(message);
            } else {
                onFailure(message);
            }
        }
    }

    @Override
    public long getActorAddress() {
        return R.id.eventCategoryRequestBasedCommand;
    }


    @Override
    public void clear() {
        cleared = true;
        requesterCommand.removeObserver(this);
    }
}
