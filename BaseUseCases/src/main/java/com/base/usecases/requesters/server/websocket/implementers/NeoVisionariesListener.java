package com.base.usecases.requesters.server.websocket.implementers;

import android.support.annotation.NonNull;

import com.base.abstraction.commands.Command;
import com.base.abstraction.logs.Logger;
import com.base.usecases.requesters.server.websocket.ActiveWebSocket;
import com.base.usecases.requesters.server.websocket.SocketMessage;
import com.base.usecases.requesters.server.websocket.SocketMode;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFrame;
import com.neovisionaries.ws.client.WebSocketListener;
import com.neovisionaries.ws.client.WebSocketState;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * the listener for {@link NeoVisionariesWebSocket} class
 * <p>
 * Created by Ahmed Adel on 1/29/2017.
 */
class NeoVisionariesListener implements
        WebSocketListener,
        WebSocketCallbacks<NeoVisionariesListener> {

    private Command<Exception, ?> onOpen;
    private Command<Exception, ?> onError;
    private Command<String, ?> onMessages;
    private Command<SocketMessage, ?> onClose;


    @Override
    public void onStateChanged(WebSocket w, WebSocketState s) throws Exception {
        Logger.getInstance().info(ActiveWebSocket.class, "onStateChanged() " + w.getURI() + " : " + s);
        if (WebSocketState.CLOSED.equals(s)) {
            if (onClose != null) {
                invokeOnClose(false, null);
            }
        }
    }

    @Override
    public void onConnected(WebSocket w, Map<String, List<String>> h) throws Exception {
        Logger.getInstance().info(ActiveWebSocket.class, "onConnected() " + w.getURI() + " : " + h);
        if (onOpen != null) {
            onOpen.execute(null);
        }
    }

    @Override
    public void onConnectError(WebSocket w, WebSocketException c) throws Exception {
        Logger.getInstance().info(ActiveWebSocket.class, "onConnected() " + w.getURI() + " : " +
                c.getMessage());
        Logger.getInstance().exception(c);
        if (onOpen != null) {
            onOpen.execute(c);
        }
    }

    @Override
    public void onDisconnected(WebSocket w, WebSocketFrame serverCloseFrame,
                               WebSocketFrame clientCloseFrame, boolean remote) throws Exception {

        Logger.getInstance().info(ActiveWebSocket.class, "onDisconnected() " +
                w.getURI() +
                " : [" + logServerCloseFrame(serverCloseFrame) +
                ", " + logClientCloseFrame(clientCloseFrame) +
                ", closedByServer=" + remote + "]");


        if (onClose != null) {
            invokeOnClose(remote, remote ? serverCloseFrame : clientCloseFrame);
        }
    }

    @NonNull
    private String logServerCloseFrame(WebSocketFrame serverCloseFrame) {
        return serverCloseFrame != null
                ? "serverReason=" + serverCloseFrame.getCloseReason()
                + ", serverCode=" + serverCloseFrame.getCloseCode()
                : "";
    }

    @NonNull
    private String logClientCloseFrame(WebSocketFrame clientCloseFrame) {
        return clientCloseFrame != null
                ? "clientReason=" + clientCloseFrame.getCloseReason()
                + ", clientCode=" + clientCloseFrame.getCloseCode()
                : "";

    }



    @SuppressWarnings("WrongConstant")
    private void invokeOnClose(boolean remote, WebSocketFrame frame) {
        SocketMessage socketMessage = new SocketMessage();
        socketMessage.setMode(SocketMode.CLOSE);
        if (frame != null) {
            socketMessage.setCloseCode(frame.getCloseCode());
            socketMessage.setContent(frame.getPayloadText());
        }
        socketMessage.setClosedRemotely(remote);
        onClose.execute(socketMessage);
    }

    @Override
    public void onFrame(WebSocket w, WebSocketFrame f) throws Exception {
        Logger.getInstance().info(ActiveWebSocket.class, "onFrame() " + w.getURI() + " : " +
                f);
    }

    @Override
    public void onContinuationFrame(WebSocket w, WebSocketFrame f) throws Exception {
        Logger.getInstance().info(ActiveWebSocket.class, "onContinuationFrame() " + w.getURI()
                + " : " + f);
    }

    @Override
    public void onTextFrame(WebSocket w, WebSocketFrame f) throws Exception {
        Logger.getInstance().info(ActiveWebSocket.class, "onTextFrame() " + w.getURI()
                + " : " + f);
    }

    @Override
    public void onBinaryFrame(WebSocket w, WebSocketFrame f) throws Exception {
        Logger.getInstance().info(ActiveWebSocket.class, "onBinaryFrame() " + w.getURI()
                + " : " + f);
    }

    @Override
    public void onCloseFrame(WebSocket w, WebSocketFrame f) throws Exception {
        Logger.getInstance().info(ActiveWebSocket.class, "onCloseFrame() " + w.getURI()
                + " : " + f);
    }

    @Override
    public void onPingFrame(WebSocket w, WebSocketFrame f) throws Exception {
        Logger.getInstance().info(ActiveWebSocket.class, "onPingFrame() " + w.getURI()
                + " : " + f);
    }

    @Override
    public void onPongFrame(WebSocket w, WebSocketFrame f) throws Exception {
        Logger.getInstance().info(ActiveWebSocket.class, "onPongFrame() " + w.getURI()
                + " : " + f);
    }

    @Override
    public void onBinaryMessage(WebSocket w, byte[] b) throws Exception {
        Logger.getInstance().info(ActiveWebSocket.class, "onBinaryMessage() " + w.getURI()
                + " : " + Arrays.toString(b));
    }

    @Override
    public void onSendingFrame(WebSocket w, WebSocketFrame f) throws Exception {
        Logger.getInstance().info(ActiveWebSocket.class, "onSendingFrame() " + w.getURI()
                + " : " + f);
    }

    @Override
    public void onFrameSent(WebSocket w, WebSocketFrame f) throws Exception {
        Logger.getInstance().info(ActiveWebSocket.class, "onFrameSent() " + w.getURI()
                + " : " + f);
    }

    @Override
    public void onFrameUnsent(WebSocket w, WebSocketFrame f) throws Exception {
        Logger.getInstance().info(ActiveWebSocket.class, "onFrameUnsent() " + w.getURI()
                + " : " + f);
    }

    @Override
    public void onError(WebSocket w, WebSocketException c) throws Exception {
        Logger.getInstance().error(ActiveWebSocket.class, "onError() " + w.getURI()
                + " : " + c.getMessage());
        Logger.getInstance().exception(c);
        if (onError != null) {
            onError.execute(c);
        }
    }

    @Override
    public void onFrameError(WebSocket w, WebSocketException c, WebSocketFrame f) throws Exception {
        Logger.getInstance().info(ActiveWebSocket.class, "onFrameError() " + w.getURI()
                + " : " + c.getMessage() + ", frame : " + f);
        Logger.getInstance().exception(c);
    }


    @Override
    public void onMessageError(WebSocket w, WebSocketException c, List<WebSocketFrame> f) throws Exception {
        Logger.getInstance().info(ActiveWebSocket.class, "onMessageError() " + w.getURI()
                + " : " + c.getMessage() + ", frames : " + f);
        Logger.getInstance().exception(c);
    }

    @Override
    public void onMessageDecompressionError(WebSocket w, WebSocketException c, byte[] compressed) throws Exception {
        Logger.getInstance().info(ActiveWebSocket.class, "onMessageDecompressionError() " + w.getURI()
                + " : " + c.getMessage() + ", compressed : " + Arrays.toString(compressed));
        Logger.getInstance().exception(c);
    }

    @Override
    public void onTextMessage(WebSocket w, String m) {
        Logger.getInstance().error(ActiveWebSocket.class, "onTextMessage() " + w.getURI() + " : " + m);
        if (onMessages != null) {
            onMessages.execute(m);
        }
    }

    @Override
    public void onTextMessageError(WebSocket w, WebSocketException c, byte[] b) throws Exception {
        Logger.getInstance().error(ActiveWebSocket.class, "onTextMessageError() " + w.getURI() +
                " : " + c.getMessage());
        Logger.getInstance().exception(c);
    }

    @Override
    public void onSendError(WebSocket w, WebSocketException c, WebSocketFrame f) throws Exception {
        Logger.getInstance().error(ActiveWebSocket.class, "onSendError() " + w.getURI() +
                " : " + c.getMessage());
        Logger.getInstance().exception(c);
    }

    @Override
    public void onUnexpectedError(WebSocket w, WebSocketException c) throws Exception {
        Logger.getInstance().error(ActiveWebSocket.class, "onUnexpectedError() " + w.getURI() +
                " : " + c.getMessage());
        Logger.getInstance().exception(c);
    }

    @Override
    public void handleCallbackError(WebSocket w, Throwable c) throws Exception {
        Logger.getInstance().error(ActiveWebSocket.class, "handleCallbackError() " + w.getURI() +
                " : " + c.getMessage());
        Logger.getInstance().exception(c);
    }

    @Override
    public void onSendingHandshake(WebSocket w, String req, List<String[]> headers) throws Exception {
        Logger.getInstance().error(ActiveWebSocket.class, "onSendingHandshake() " + w.getURI() +
                " : " + req + " - " + headers);
    }

    @Override
    public NeoVisionariesListener onClose(Command<SocketMessage, ?> onClose) {
        this.onClose = onClose;
        return this;
    }

    @Override
    public NeoVisionariesListener onOpen(Command<Exception, ?> onOpen) {
        this.onOpen = onOpen;
        return this;
    }

    @Override
    public NeoVisionariesListener onMessages(Command<String, ?> onMessages) {
        this.onMessages = onMessages;
        return this;
    }

    @Override
    public NeoVisionariesListener onError(Command<Exception, ?> onError) {
        this.onError = onError;
        return this;
    }
}
