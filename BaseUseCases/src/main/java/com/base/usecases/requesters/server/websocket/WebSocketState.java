package com.base.usecases.requesters.server.websocket;

/**
 * Created by Ahmed Adel on 1/22/2017.
 */

class WebSocketState {

    private SocketMode modes;
    private WebSocket webSocket;

    public SocketMode getModes() {
        return modes;
    }

    public void setModes(SocketMode modes) {
        this.modes = modes;
    }

    public WebSocket getWebSocket() {
        return webSocket;
    }

    public void setWebSocket(WebSocket webSocket) {
        this.webSocket = webSocket;
    }


}
