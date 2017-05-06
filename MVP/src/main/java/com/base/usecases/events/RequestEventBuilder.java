package com.base.usecases.events;

import com.base.abstraction.commands.Command;
import com.base.abstraction.events.Event;

/**
 * create a {@link Event} to process a request from a data source
 * <p>
 * Created by Ahmed Adel on 10/25/2016.
 */
public class RequestEventBuilder implements Command<RequestEventBuilderParams, Event> {

    @Override
    public Event execute(RequestEventBuilderParams params) {
        RequestMessage.Builder messageBuilder = new RequestMessage.Builder();
        messageBuilder.id(params.requestId);
        messageBuilder.parametersGroup(params.parametersGroup);
        messageBuilder.pathVariablesGroup(params.pathVariableGroup);
        messageBuilder.content(params.entity);
        Event.Builder eventBuilder = new Event.Builder(params.requestId);
        eventBuilder.message(messageBuilder.build());
        return eventBuilder.build();
    }
}
