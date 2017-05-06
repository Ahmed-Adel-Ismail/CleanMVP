package com.base.presentation.repos.json;

import com.base.abstraction.commands.Command;
import com.base.abstraction.events.Event;
import com.base.abstraction.events.Message;
import com.base.abstraction.interfaces.Clearable;
import com.base.usecases.events.RequestMessage;
import com.base.usecases.requesters.base.EntityGateway;
import com.google.gson.Gson;

/**
 * a {@link Command} to process requests from an {@link EntityGateway}, make sure to invoke
 * {@link JsonRequest#clear()} to cancel the callback of the requests and
 * free resources as soon as possible
 * <p>
 * Created by Ahmed Adel on 9/18/2016.
 */
public class JsonRequest implements
        Clearable,
        Command<RequestMessage, Void> {

    private EntityGateway entityGateway;

    public JsonRequest(EntityGateway entityGateway) {
        this.entityGateway = entityGateway;
    }

    @Override
    public final Void execute(RequestMessage message) {
        if (entityGateway != null) {
            Event requestEvent = getRequestEvent(message);
            entityGateway.execute(requestEvent);
        }
        return null;
    }

    @Override
    public void clear() {
        entityGateway = null;
    }

    /**
     * get the {@link Event} to be used to process a request from a data source
     *
     * @param message the {@link Message} that holds the request Object in it's
     *                {@link Message#getContent()}
     * @return the {@link Event} to be used
     */
    private Event getRequestEvent(RequestMessage message) {
        Object requestEntity = message.getContent();
        requestEntity = new Gson().toJson(requestEntity);
        RequestMessage.Builder messageBuilder = message.copyBuilder();
        messageBuilder.content(requestEntity);
        Message requestMessage = messageBuilder.build();
        Event.Builder eventBuilder = new Event.Builder(message.getId());
        eventBuilder.message(requestMessage);
        return eventBuilder.build();
    }

}