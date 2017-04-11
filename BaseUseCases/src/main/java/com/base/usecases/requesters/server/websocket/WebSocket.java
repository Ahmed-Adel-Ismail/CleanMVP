package com.base.usecases.requesters.server.websocket;

import com.base.abstraction.logs.Logger;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

import io.reactivex.subjects.Subject;

/**
 * a class {@link WebSocketClient} to handle communication with a web socket
 * <p>
 * Created by Wafaa on 12/21/2016.
 */

class WebSocket extends WebSocketClient{

    private Subject<String> observable;

    WebSocket(URI serverURI, Subject<String> observable) {
        super(serverURI);
        this.observable = observable;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Logger.getInstance().info(getClass() , "Web Sockets is opened");
    }

    @Override
    public void onMessage(String message) {
        observable.onNext(message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        observable.onComplete();
    }

    @Override
    public void onError(Exception ex) {
        observable.onError(ex);
    }

}
