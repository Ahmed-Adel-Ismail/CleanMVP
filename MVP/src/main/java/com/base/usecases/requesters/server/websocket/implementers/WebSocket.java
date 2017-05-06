package com.base.usecases.requesters.server.websocket.implementers;

/**
 * an interface that is implemented by classes that will implement connecting to WebSockets
 * <p>
 * Created by Ahmed Adel on 1/24/2017.
 */
public interface WebSocket extends WebSocketCallbacks<WebSocket> {

    boolean connectSocket() throws Exception;

    void disconnectSocket() throws Exception;

    void sendSocketMessage(String message);

}
