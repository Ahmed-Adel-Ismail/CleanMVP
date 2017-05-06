package com.base.usecases.failures;

import com.base.abstraction.commands.Command;
import com.base.usecases.events.ResponseMessage;

/**
 * a class to generate local failure {@link ResponseMessage}
 * <p>
 * Created by Ahmed Adel on 10/18/2016.
 */
public class FailureMessageGenerator implements Command<Long, ResponseMessage> {

    @Override
    public ResponseMessage execute(Long eventId) {
        ResponseMessage.Builder builder = new ResponseMessage.Builder();
        builder.id(eventId);
        builder.statusCode(ResponseMessage.HTTP_NO_RESPONSE);
        builder.successful(false);
        return builder.build();
    }
}
