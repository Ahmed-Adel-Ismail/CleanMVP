package com.base.usecases.requesters.server.websocket.implementers;

import com.base.abstraction.commands.Command;
import com.base.abstraction.logs.Logger;
import com.base.usecases.requesters.server.base.HttpHeaders;
import com.base.usecases.requesters.server.websocket.SocketCloseCode;
import com.base.usecases.requesters.server.websocket.SocketMessage;
import com.base.usecases.requesters.server.ssl.SSLContextFactory;
import com.neovisionaries.ws.client.OpeningHandshakeException;
import com.neovisionaries.ws.client.StatusLine;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketExtension;
import com.neovisionaries.ws.client.WebSocketFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * an implementer for {@link WebSocket} interface that uses NewVisionaries library
 * <p>
 * Created by Ahmed Adel on 1/24/2017.
 */
public class NeoVisionariesWebSocket implements WebSocket {


    private static final long PING_PONG_INTERVAL_MILLIS = 20 * 1000;
    private WebSocketFactory webSocketFactory;
    private String url;
    private HttpHeaders headers;
    private com.neovisionaries.ws.client.WebSocket webSocket;
    private NeoVisionariesListener listener;


    public NeoVisionariesWebSocket(String url, HttpHeaders headers) {
        this.url = url;
        this.headers = headers;
        this.listener = new NeoVisionariesListener();
        this.webSocketFactory = new WebSocketFactory()
                .setSSLContext(new SSLContextFactory().execute(null))
                .setConnectionTimeout(0);
    }

    @Override
    public boolean connectSocket() throws Exception {
        if (webSocket == null) {
            return initializeAndConnectWebSocket();
        } else {
            throw new UnsupportedOperationException("socket already connected");
        }
    }

    private boolean initializeAndConnectWebSocket() {
        boolean connected = false;
        try {
            connected = doConnectWebSocket();
        } catch (OpeningHandshakeException e) {
            logOpeningHandshakeException(e);
            throw new UnsupportedOperationException(e);
        } catch (WebSocketException e) {
            Logger.getInstance().error(getClass(), "Failed to connect after creating socket");
            throw new UnsupportedOperationException(e);
        } catch (Throwable e) {
            throw new UnsupportedOperationException(e);
        } finally {
            if (!connected) {
                webSocket = null;
            }
        }
        return true;
    }

    private boolean doConnectWebSocket() throws Exception {
        webSocket = createWebSocketWithHeaders(createWebSocket());
        webSocket.connectable().call();
        return true;
    }

    private com.neovisionaries.ws.client.WebSocket createWebSocket() throws IOException {
        return webSocketFactory.createSocket(url)
                .addListener(listener)
                .addExtension(WebSocketExtension.PERMESSAGE_DEFLATE)
                .setPongInterval(PING_PONG_INTERVAL_MILLIS)
                .setPingInterval(PING_PONG_INTERVAL_MILLIS);
    }


    private com.neovisionaries.ws.client.WebSocket createWebSocketWithHeaders(
            com.neovisionaries.ws.client.WebSocket webSocket) {

        if (headers == null) {
            return webSocket;
        }

        for (Map.Entry<String, String> entry : headers.asMap().entrySet()) {
            webSocket.addHeader(entry.getKey(), entry.getValue());
        }

        return webSocket;
    }


    private void logOpeningHandshakeException(OpeningHandshakeException e) {
        StatusLine sl = e.getStatusLine();
        Logger.getInstance().error(getClass(), "=== Status Line ===");
        Logger.getInstance().error(getClass(), "HTTP Version  = " + sl.getHttpVersion());
        Logger.getInstance().error(getClass(), "Status Code   = " + sl.getStatusCode());
        Logger.getInstance().error(getClass(), "Reason Phrase = " + sl.getReasonPhrase());

        Map<String, List<String>> headers = e.getHeaders();
        Logger.getInstance().error(getClass(), "=== HTTP Headers ===");
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            logOpeningHandshakeExceptionHeader(entry);
        }
    }

    private void logOpeningHandshakeExceptionHeader(Map.Entry<String, List<String>> entry) {

        String headerName = entry.getKey();
        List<String> headerValues = entry.getValue();

        if (headerValues == null || headerValues.size() == 0) {
            Logger.getInstance().error(getClass(), headerName);
            return;
        }

        for (String value : headerValues) {
            Logger.getInstance().error(getClass(), headerName + " : " + value);
        }
    }


    @Override
    public void disconnectSocket() throws Exception {
        if (webSocket != null) {
            webSocket.disconnect(SocketCloseCode.NORMAL);
        } else {
            webSocket = null;
            throw new UnsupportedOperationException("socket already closed");
        }
    }


    @Override
    public void sendSocketMessage(String message) {
        if (webSocket != null) {
            webSocket.sendText(message);
        } else {
            throw new UnsupportedOperationException("socket not initialized / connected");
        }
    }

    @Override
    public WebSocket onOpen(Command<Exception, ?> onOpen) {
        listener.onOpen(onOpen);
        return this;
    }

    @Override
    public WebSocket onMessages(Command<String, ?> onMessages) {
        listener.onMessages(onMessages);
        return this;
    }

    @Override
    public WebSocket onClose(Command<SocketMessage, ?> onClose) {
        listener.onClose(onClose);
        return this;
    }

    @Override
    public WebSocket onError(Command<Exception, ?> onError) {
        listener.onError(onError);
        return this;
    }
}
