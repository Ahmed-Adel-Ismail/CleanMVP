package com.base.presentation.repos.json;

import android.support.annotation.NonNull;

import com.base.abstraction.commands.Command;
import com.base.usecases.callbacks.Callback;
import com.base.usecases.events.ResponseMessage;

/**
 * a class that parses the {@code Json} response received in the {@link ResponseMessage} Object
 * <p>
 * Created by Ahmed Adel on 12/24/2016.
 */
abstract class AbstractResponseMessageParser implements
        Command<ResponseMessage, ResponseMessage> {

    private Command<String, ?> errorParser;


    public void setErrorParser(Command<String, ?> errorParser) {
        this.errorParser = errorParser;
    }


    /**
     * get the {@link ResponseMessage} to be delivered back to the
     * {@link Callback}
     *
     * @param originalResponseMessage the {@link ResponseMessage} received from the data source
     * @return a {@link ResponseMessage} to be delivered back to the
     * {@link Callback}
     * @throws UnsupportedOperationException if there was no expected error type declared for this
     *                                       response
     */
    @Override
    public ResponseMessage execute(ResponseMessage originalResponseMessage)
            throws UnsupportedOperationException {
        boolean successful = originalResponseMessage.isSuccessful();
        ResponseMessage.Builder builder = new ResponseMessage.Builder();
        builder.id(originalResponseMessage.getId());
        builder.statusCode(originalResponseMessage.getStatusCode());
        builder.successful(successful);
        String responseString = originalResponseMessage.getContent();
        if (successful) {
            builder = readSuccessfulResponseObject(builder, responseString);
        } else if (errorParser != null) {
            builder.content(errorParser.execute(responseString));
        } else {
            throw new UnsupportedOperationException("no error parser declared for this response");
        }
        return builder.build();
    }


    @NonNull
    abstract ResponseMessage.Builder readSuccessfulResponseObject(
            ResponseMessage.Builder builder,
            String responseString);
}
