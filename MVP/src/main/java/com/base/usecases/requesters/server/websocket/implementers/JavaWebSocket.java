package com.base.usecases.requesters.server.websocket.implementers;

import android.support.annotation.NonNull;

import com.base.abstraction.commands.Command;
import com.base.abstraction.logs.Logger;
import com.base.usecases.requesters.server.base.HttpHeaders;
import com.base.usecases.requesters.server.websocket.SocketMessage;
import com.base.usecases.requesters.server.ssl.SSLContextFactory;

import org.java_websocket.client.DefaultSSLWebSocketClientFactory;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_75;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

import javax.net.ssl.SSLContext;

/**
 * a {@link WebSocket} that uses {@link WebSocketClient} as it's implementer
 * <p>
 * Created by Ahmed Adel on 1/24/2017.
 *
 * @deprecated old library that did not work with sending and receiving messages through sockets
 */
@Deprecated
public class JavaWebSocket extends WebSocketClient implements WebSocket {

    private Command<Exception, ?> onOpen;
    private Command<Exception, ?> onError;
    private Command<String, ?> onMessages;
    private Command<SocketMessage, ?> onClose;

    public JavaWebSocket(String url, @NonNull HttpHeaders headers) throws URISyntaxException {
        super(new URI(url), new Draft_75(), headers.asMap(), 0);
        SSLContext sslContext = new SSLContextFactory().execute(null);
        setWebSocketFactory(new DefaultSSLWebSocketClientFactory(sslContext));
        Logger.getInstance().error(getClass(), "init<> : " + url + " : " + headers.asMap());
    }

    @Override
    public WebSocket onClose(Command<SocketMessage, ?> onClose) {
        this.onClose = onClose;
        return this;
    }

    @Override
    public WebSocket onError(Command<Exception, ?> onError) {
        return null;
    }

    @Override
    public WebSocket onOpen(Command<Exception, ?> onOpen) {
        this.onOpen = onOpen;
        return this;
    }

    @Override
    public WebSocket onMessages(Command<String, ?> onMessages) {
        this.onMessages = onMessages;
        return this;
    }

    @Override
    public boolean connectSocket() throws Exception {
        return connectBlocking();
    }

    @Override
    public void disconnectSocket() throws Exception {
        closeBlocking();
    }

    @Override
    public void sendSocketMessage(String message) {
        super.send(message);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Logger.getInstance().error(getClass(), "onOpen() : " + handshakedata);
        if (onOpen != null) {
            onOpen.execute(null);
        }
    }

    @Override
    public void onMessage(String message) {
        Logger.getInstance().error(getClass(), "onMessage() : " + message);
        if (onMessages != null) {
            onMessages.execute(message);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Logger.getInstance().error(getClass(), "onClose() : " + code + " [remote : " + remote + "]");
        if (onClose != null) {
            SocketMessage socketMessage = new SocketMessage();
            socketMessage.setCloseCode(code);
            socketMessage.setContent(reason);
            socketMessage.setClosedRemotely(remote);
            onClose.execute(socketMessage);
        }
    }

    @Override
    public void onError(Exception ex) {
        Logger.getInstance().error(getClass(), "onError() : " + ex);
        if (onError != null) {
            onError.execute(ex);
        }
    }
}
