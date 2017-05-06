package com.base.usecases.requesters.server.mocks;

import com.base.abstraction.annotations.interfaces.Behavior;
import com.base.abstraction.events.Event;
import com.base.abstraction.logs.Logger;
import com.base.abstraction.system.AppResources;
import com.base.abstraction.system.Behaviors;
import com.base.usecases.annotations.HttpRequestUrlLocator;
import com.base.usecases.annotations.Mock;
import com.base.usecases.events.RequestMessage;
import com.base.usecases.events.ResponseMessage;
import com.base.usecases.exceptions.NoMockedResponseException;
import com.base.usecases.requesters.base.EntityRequester;

/**
 * a class that always returns successful responses, if any instance variable in the
 * Repository is annotated with {@link Mock},
 * this requester is replaced with the
 * any {@link EntityRequester} when {@link Behaviors#MOCKING} is selected
 * for the application in the {@link Behavior} annotation
 * <p>
 * Created by Ahmed Adel on 11/20/2016.
 */
@HttpRequestUrlLocator
public class MockRequester extends EntityRequester {

    private static final long SLEEP_MILLIS = 1000;
    private EntityRequester originalRequester;

    public void setOriginalRequester(EntityRequester requester) {
        this.originalRequester = requester;
    }

    @Override
    public Void execute(Event event) {

        Logger.getInstance().error(MockRequester.class, "mock request id : " +
                AppResources.resourceEntryName((int) event.getId()));

        RequestMessage message = event.getMessage();
        if (message != null) {
            logRequestMessage(message);
        }

        try {
            Thread.sleep(SLEEP_MILLIS);
        } catch (InterruptedException e) {
            Logger.getInstance().error(getClass(), e);
        }

        try {
            long eventId = event.getId();
            MockedEntity responseEntity = getMockedEntitiesRegistry().get(eventId);
            ResponseMessageMocker responseMessageMocker = new ResponseMessageMocker(eventId);
            ResponseMessage responseMessage = responseMessageMocker.execute(responseEntity);
            Event.Builder eventBuilder = new Event.Builder(eventId).message(responseMessage);
            notifyCallback(eventBuilder.build());
        } catch (NoMockedResponseException e) {
            logAndUseOriginalRequester(event, e);
        }

        return null;
    }

    private void logRequestMessage(RequestMessage message) {
        Object o;
        if ((o = message.getPathVariablesGroup()) != null) {
            Logger.getInstance().error(MockRequester.class, "mock request path variables :" + o);
        }

        if ((o = message.getParametersGroup()) != null) {
            Logger.getInstance().error(MockRequester.class, "mock request parameters :" + o);
        }

        if ((o = message.getContent()) != null) {
            Logger.getInstance().error(MockRequester.class, "mock request body :" + o);
        }
    }

    private void logAndUseOriginalRequester(Event event, NoMockedResponseException e) {
        Logger.getInstance().error(getClass(), e);
        Logger.getInstance().error(getClass(), "processing original request");
        if (originalRequester != null) {
            useOriginalRequester(event);
        }
    }

    private void useOriginalRequester(Event event) {
        try {
            if (originalRequester.getCallback() == null) {
                originalRequester.setCallback(getCallback());
            }
            originalRequester.execute(event);
        } catch (Throwable e) {
            Logger.getInstance().exception(e);
        }
    }
}
