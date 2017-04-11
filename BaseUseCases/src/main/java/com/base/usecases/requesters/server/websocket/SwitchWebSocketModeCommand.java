package com.base.usecases.requesters.server.websocket;

import com.base.abstraction.commands.Command;
import com.base.usecases.requesters.server.ssl.SSLContextFactory;

import org.java_websocket.client.DefaultSSLWebSocketClientFactory;

import javax.net.ssl.SSLContext;

/**
 * Created by Wafaa on 12/22/2016.
 */
class SwitchWebSocketModeCommand implements Command<WebSocketState, WebSocketState> {

    @Override
    public WebSocketState execute(WebSocketState webSocketState) {
        if (webSocketState != null) {
            if (webSocketState.getModes() == SocketMode.OPEN) {
                WebSocket webSocket = webSocketState.getWebSocket();
                SSLContext sslContext = new SSLContextFactory().execute(null);
                webSocket.setWebSocketFactory(new DefaultSSLWebSocketClientFactory(sslContext));
                webSocket.connect();
            } else {
                webSocketState.getWebSocket().close();
            }
        }
        return webSocketState;
    }
}
