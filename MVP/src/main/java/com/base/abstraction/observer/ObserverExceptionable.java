package com.base.abstraction.observer;

import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.abstraction.events.Event;

/**
 * implement this interface if your Class is implementing the {@link Observer} interface and needs
 * a common way to handle {@link RuntimeException} thrown by the {@link Observer#onUpdate(Event)}
 * in a separate method that can be overriden by sub-classes
 * <p>
 * Created by Ahmed Adel on 9/4/2016.
 */
public interface ObserverExceptionable {

    /**
     * this method is invoked when an Exception is thrown in {@link Observer#onUpdate(Event)},
     * this means that one of the {@link CommandExecutor#execute(Object, Object)} threw an
     * {@link RuntimeException} Exception that requires an action
     *
     * @param event    the {@link Event} that caused the Exception
     * @param exception the {@link RuntimeException} thrown
     * @return {@code true} if this Exception is fatal so the flow should stop, else {@code false}
     * so the flow continues to execute the {@link Observable#notifyObservers(Event)}
     */
    boolean isOnUpdateThrownExceptionFatal(Event event, RuntimeException exception);
}
