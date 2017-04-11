package com.base.presentation.notifications;

import com.base.abstraction.commands.Command;
import com.base.abstraction.serializers.StringJsonParser;
import com.base.cached.RemoteMessageData;

import java.io.Serializable;

/**
 * a class that reads the Payload Object in the Notification Data ... found in
 * {@link RemoteMessageData#getPayload()}
 * <p>
 * Created by Ahmed Adel on 12/19/2016.
 */
public class PayloadReader<T extends Serializable> implements Command<RemoteMessageData, T> {

    private Class<T> klass;

    public PayloadReader(Class<T> klass) {
        this.klass = klass;
    }

    @Override
    public T execute(RemoteMessageData data) throws UnsupportedOperationException {
        String payloadString = data.getPayload();
        return new StringJsonParser<>(klass).execute(payloadString);
    }


}
