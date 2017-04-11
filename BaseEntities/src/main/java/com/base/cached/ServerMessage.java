package com.base.cached;

import com.base.annotations.MockEntity;
import com.base.mocked.MockedFailureServerMessage;
import com.base.mocked.MockedSuccessServerMessage;

import java.io.Serializable;

/**
 * the Message Object received from server
 * <p/>
 * Created by Ahmed Adel on 10/5/2016.
 */
@MockEntity(value = MockedSuccessServerMessage.class, error = MockedFailureServerMessage.class)
public class ServerMessage implements Serializable {

    protected String message;
    protected MessageType messageType;
    protected String property;


    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public enum MessageType {

        SUCCESS("SUCCESS"),
        ERROR("ERROR"),
        WARNING("WARNING"),
        INFO("INFO");

        private final String messageType;

        MessageType(String messageType) {
            this.messageType = messageType;
        }

    }


}
