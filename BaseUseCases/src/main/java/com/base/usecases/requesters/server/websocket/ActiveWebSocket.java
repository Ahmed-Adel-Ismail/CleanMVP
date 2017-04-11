package com.base.usecases.requesters.server.websocket;

import android.support.annotation.NonNull;

import com.base.abstraction.commands.Command;
import com.base.abstraction.concurrency.FutureTask;
import com.base.abstraction.logs.Logger;
import com.base.usecases.requesters.server.base.HttpHeaders;
import com.base.usecases.requesters.server.websocket.implementers.NeoVisionariesWebSocket;
import com.base.usecases.requesters.server.websocket.implementers.WebSocket;
import com.base.usecases.requesters.server.websocket.implementers.WebSocketCallbacks;

import org.java_websocket.client.WebSocketClient;

import java.io.Serializable;

/**
 * a class that extends the {@link WebSocketClient} and handles opening, closing, and sending
 * messages to a server through a web-socket connection
 * it
 * <p>
 * Created by Ahmed Adel on 01/19/2017
 */
public class ActiveWebSocket implements WebSocketCallbacks<ActiveWebSocket>, Serializable {

    private SocketMode mode;
    private WebSocket webSocket;

    ActiveWebSocket(String uri, @NonNull HttpHeaders headers) {
        this.webSocket = new NeoVisionariesWebSocket(uri, headers);
    }

    ActiveWebSocket mode(SocketMode modes) {
        this.mode = modes;
        return this;
    }

    @Override
    public ActiveWebSocket onOpen(Command<Exception, ?> onOpen) {
        webSocket.onOpen(onOpen);
        return this;
    }

    @Override
    public ActiveWebSocket onMessages(Command<String, ?> onMessages) {
        webSocket.onMessages(onMessages);
        return this;
    }

    @Override
    public ActiveWebSocket onClose(Command<SocketMessage, ?> onClose) {
        webSocket.onClose(onClose);
        return this;
    }

    @Override
    public ActiveWebSocket onError(Command<Exception, ?> onError) {
        webSocket.onError(onError);
        return this;
    }


    FutureTask sendMessage(String message) {
        Logger.getInstance().error(getClass(), "sendMessage() : " + message);
        FutureTask task = new FutureTask();
        try {
            webSocket.sendSocketMessage(message);
            task.completed();
        } catch (Throwable e) {
            task.setException(e);
        }
        return task;

    }

    FutureTask openSocket() {
        Logger.getInstance().error(getClass(), "openSocket()");
        FutureTask task = new FutureTask();
        try {
            boolean connected = webSocket.connectSocket();
            if (connected) {
                task.completed();
            } else {
                task.setException(new UnsupportedOperationException("failed to open connection"));
            }
        } catch (Throwable e) {
            task.setException(e);
        }
        return task;
    }

    FutureTask closeSocket() {
        Logger.getInstance().error(getClass(), "closeSocket()");
        FutureTask task = new FutureTask();
        try {
            webSocket.disconnectSocket();
            task.completed();
        } catch (Throwable e) {
            task.setException(e);
        }
        return task;
    }

    boolean isCurrentMode(@NonNull SocketMode mode) {
        return mode.equals(this.mode);
    }

}
