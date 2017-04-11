package com.base.presentation.repos.json;

import android.support.annotation.NonNull;

import com.base.abstraction.commands.Command;
import com.base.cached.ServerMessage;
import com.base.usecases.events.ResponseMessage;
import com.google.gson.reflect.TypeToken;

/**
 * a class that parses the {@code Json} response received in the {@link ResponseMessage} Object
 * <p>
 * Created by Ahmed Adel on 12/24/2016.
 */
abstract class AbstractResponseMessageParser implements
        Command<ResponseMessage, ResponseMessage> {

    /**
     * get the {@link ResponseMessage} to be delivered back to the
     * {@link com.base.usecases.callbacks.Callback}
     *
     * @param originalResponseMessage the {@link ResponseMessage} received from the data source
     * @return a {@link ResponseMessage} to be delivered back to the
     * {@link com.base.usecases.callbacks.Callback}
     */
    @Override
    public ResponseMessage execute(ResponseMessage originalResponseMessage) {
        boolean successful = originalResponseMessage.isSuccessful();
        ResponseMessage.Builder builder = new ResponseMessage.Builder();
        builder.id(originalResponseMessage.getId());
        builder.statusCode(originalResponseMessage.getStatusCode());
        builder.successful(successful);
        String responseString = originalResponseMessage.getContent();
        if (successful) {
            builder = readResponseObject(builder, responseString);
        } else {
            builder = readServerMessage(builder, responseString);
        }
        return builder.build();
    }

    private ResponseMessage.Builder readServerMessage(ResponseMessage.Builder builder,
                                                      String responseString) {
        TypeToken<ServerMessage> serverMessageToken = createServerMessageTypeToken();
        ServerMessage serverMessage;
        serverMessage = new JsonParser<>(serverMessageToken).execute(responseString);
        builder.content(serverMessage);
        return builder;
    }

    @NonNull
    private TypeToken<ServerMessage> createServerMessageTypeToken() {
        return new TypeToken<ServerMessage>() {
        };
    }

    @NonNull
    abstract ResponseMessage.Builder readResponseObject(
            ResponseMessage.Builder builder,
            String responseString);
}
