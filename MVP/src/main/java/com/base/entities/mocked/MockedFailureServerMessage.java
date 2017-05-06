package com.base.entities.mocked;

import com.base.entities.cached.ServerMessage;

/**
 * a mocked Object that represents a Failure Message
 * <p>
 * Created by Ahmed Adel on 12/4/2016.
 */
public class MockedFailureServerMessage extends ServerMessage {

    public MockedFailureServerMessage() {
        message = "connection error";
        messageType = MessageType.ERROR;
        property = "-1";
    }
}
