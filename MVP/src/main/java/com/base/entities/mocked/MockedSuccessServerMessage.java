package com.base.entities.mocked;

import com.base.entities.cached.ServerMessage;

/**
 * a mocked Object that represents a successful server message
 * <p>
 * Created by Ahmed Adel on 12/4/2016.
 */
public class MockedSuccessServerMessage extends ServerMessage {

    public MockedSuccessServerMessage() {
        message = "processed successfully";
        messageType = MessageType.SUCCESS;
        property = "-2";
    }
}
