package com.base.usecases.requesters.base;

import com.base.abstraction.commands.Command;
import com.base.abstraction.events.Event;
import com.base.usecases.callbacks.Callback;
import com.base.usecases.callbacks.CallbackDispatcher;
import com.base.usecases.callbacks.CallbackHolder;

/**
 * An interface implemented by any Class that will be used in requesting something and
 * notifying back ... usually used with {@link Callback} interfaces
 * <p/>
 * Created by Ahmed Adel on 9/21/2016.
 *
 * @see Callback
 * @see CallbackHolder
 * @see CallbackDispatcher
 */
public interface Requester extends Command<Event, Void> {

    /**
     * do the request from server (or similar source like database) in this method
     * s
     *
     * @param event the {@link Event} that holds the request
     *              {@link com.base.abstraction.events.Message}
     */
    Void execute(Event event);
}
