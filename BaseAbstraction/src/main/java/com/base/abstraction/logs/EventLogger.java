package com.base.abstraction.logs;

import android.support.annotation.NonNull;

import com.base.abstraction.commands.Command;
import com.base.abstraction.events.Event;
import com.base.abstraction.events.Message;
import com.base.abstraction.system.App;
import com.base.abstraction.system.AppResources;

/**
 * a class that logs {@link Event} instances
 * <p>
 * Created by Ahmed Adel on 10/30/2016.
 */
public class EventLogger implements Command<Event, Void> {

    private Class<?> hostClass;
    private String logMessage;

    /**
     * create an {@link EventLogger} instance
     *
     * @param hostClass  the {@link Class} that will be used as a {@code TAG} for logging
     * @param logMessage the log message that will be displayed before the {@link Event} details,
     *                   notice that it will be printed as follows :<br>
     *                   logMessage(eventName - eventMessage) : eventSenderAddress
     */
    public EventLogger(Class<?> hostClass, String logMessage) {
        this.hostClass = hostClass;
        this.logMessage = logMessage;
    }

    @Override
    public final Void execute(Event event) {
        if (App.getInstance().isDebugging()) {
            try {
                String senderAddress = readSenderAddress(event);
                String messageName = readMessageName(event);
                String eventName = readEventName(event.getId());
                logInfo(eventName, messageName, senderAddress);
            } catch (Throwable e) {
                Logger.getInstance().error(hostClass, "[low priority] " + e.getMessage());
            }
        }
        return null;
    }


    @NonNull
    private String readSenderAddress(Event event) {
        int senderAddress = (int) event.getSenderActorAddress();
        return (senderAddress > 0) ? " : " + AppResources.resourceEntryName(senderAddress) : "";
    }

    @NonNull
    private String readMessageName(Event event) {
        Message message = event.getMessage();
        return (message != null && message.getId() > 0) ? " - " + AppResources.resourceEntryName((int) message.getId()) : "";
    }

    private String readEventName(long id) {
        return AppResources.resourceEntryName((int) id);
    }

    private void logInfo(String eventName, String messageName, String senderAddress) {
        SystemLogger.getInstance().info(hostClass,
                getMessage(logMessage, senderAddress, messageName, eventName));
    }


    /**
     * get the {@code String} to be logged, you can override this method to change how
     * the {@link Event} is logged
     *
     * @param logMessage         the log message at the start
     * @param eventName          the name of the event
     * @param eventMessage       the name of the event-message
     * @param eventSenderAddress the address of the sender of the event
     * @return the {@code String} that will be displayed
     */
    protected String getMessage(String logMessage,
                                String eventName,
                                String eventMessage,
                                String eventSenderAddress) {
        return logMessage + "(" + eventName + eventMessage + ")" + eventSenderAddress + "";
    }
}
