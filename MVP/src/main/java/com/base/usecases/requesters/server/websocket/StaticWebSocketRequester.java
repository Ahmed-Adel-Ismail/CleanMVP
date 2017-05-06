package com.base.usecases.requesters.server.websocket;

import android.support.annotation.NonNull;

import com.base.abstraction.commands.Command;
import com.base.abstraction.events.Event;
import com.base.abstraction.events.Message;
import com.base.abstraction.exceptions.TestException;
import com.base.abstraction.logs.Logger;
import com.base.abstraction.system.AppResources;
import com.base.usecases.events.ResponseMessage;
import com.base.entities.cached.ServerMessage;
import com.base.usecases.annotations.WebSocketUrlLocator;
import com.base.usecases.events.SocketRequestMessage;
import com.base.usecases.requesters.server.base.ServerRequester;
import com.google.gson.Gson;

import java.net.URISyntaxException;

/**
 * a {@link ServerRequester} that handles opening a web-socket, and receiving / sending messages
 * to it through {@link SocketRequestMessage} and {@link ResponseMessage}, the Object used as
 * a response from web-sockets is {@link SocketMessage}, this holds the Object as {@code Json},
 * and can be retrieved as an Object through {@link SocketMessage#parseContent(Class)}
 * <p>
 * Created by Ahmed Adel on 1/22/2017.
 */
@WebSocketUrlLocator
public class StaticWebSocketRequester extends ServerRequester {

    private ActiveWebSocket activeWebSocket;

    protected StaticWebSocketRequester() {
    }


    @Override
    public Void execute(Event event) {

        long eventId = event.getId();
        SocketRequestMessage message = event.getMessage();
        SocketMode mode = message.getSocketMode();
        String content = message.getContent();

        switch (mode) {
            case OPEN:
                open(eventId, message);
                break;
            case SEND:
                send(eventId, content);
                break;
            case CLOSE:
                close(eventId);
                break;
            default:
                logError(eventId, "unsupported Socket Mode : " + mode);
                new TestException().execute(new UnsupportedOperationException());
        }
        return null;
    }

    private void open(long eventId, Message content) {
        if (activeWebSocket == null || activeWebSocket.isCurrentMode(SocketMode.CLOSE)) {
            try {
                openNewActiveWebSocket(eventId, content);
            } catch (Throwable e) {
                Logger.getInstance().exception(e);
                SocketMessage socketMessage = new SocketMessage();
                socketMessage.setMode(SocketMode.OPEN);
                socketMessage.setError(e.getMessage());
                notifyFailure(eventId, socketMessage);
            }
        } else {
            logError(eventId, " socket already opened");
        }
    }


    private void openNewActiveWebSocket(final long eventId, Message content) throws URISyntaxException {
        headers.update();
        String baseUrl = createUrl(eventId, content);
        activeWebSocket = new ActiveWebSocket(baseUrl, headers);
        activeWebSocket.mode(SocketMode.OPEN)
                .onOpen(notifyOpened(eventId))
                .onError(notifyOnError(eventId))
                .onMessages(onMessageReceived(eventId))
                .onClose(onCloseWebSocket(eventId))
                .openSocket()
                .onComplete(openSocketOnComplete(eventId))
                .onException(notifyConnectionFailed(eventId));

    }

    protected String createUrl(long eventId, Message content) {
        return getRequestUrlLocator().execute(eventId);
    }

    private Command<Exception, ?> notifyOnError(final long eventId) {
        return new Command<Exception, Void>() {
            @Override
            public Void execute(Exception exception) {
                SocketMessage socketMessage = new SocketMessage();
                socketMessage.setMode(SocketMode.ERROR);
                socketMessage.setContent(exception.getMessage());
                notifyFailure(eventId, socketMessage);
                return null;
            }
        };
    }

    @NonNull
    private Command<Exception, Void> notifyOpened(final long eventId) {
        return new Command<Exception, Void>() {
            @Override
            public Void execute(Exception exception) {
                onOpenSocket(exception, eventId);
                return null;
            }
        };
    }


    @NonNull
    private Command<String, Void> onMessageReceived(final long eventId) {
        return new Command<String, Void>() {
            @Override
            public Void execute(String message) {
                SocketMessage socketMessage = new SocketMessage();
                socketMessage.setMode(SocketMode.RECEIVED);
                socketMessage.setContent(message);
                notifySuccess(eventId, socketMessage);
                return null;
            }
        };
    }

    @NonNull
    private Command<SocketMessage, Void> onCloseWebSocket(final long eventId) {
        return new Command<SocketMessage, Void>() {
            @Override
            public Void execute(SocketMessage socketMessage) {
                socketMessage.setMode(SocketMode.CLOSE);
                if (activeWebSocket == null || activeWebSocket.isCurrentMode(SocketMode.CLOSE)) {
                    notifySuccess(eventId, socketMessage);
                } else {
                    notifyFailure(eventId, socketMessage);
                }
                activeWebSocket = null;
                return null;
            }
        };
    }

    private Command<Boolean, Void> openSocketOnComplete(final long eventId) {
        return new Command<Boolean, Void>() {
            @Override
            public Void execute(Boolean success) {
                onOpenSocket(null, eventId);
                return null;
            }
        };
    }

    private void onOpenSocket(Exception exception, long eventId) {
        SocketMessage socketMessage = new SocketMessage();
        socketMessage.setMode(SocketMode.OPEN);
        if (exception == null) {
            notifySuccess(eventId, socketMessage);
        } else {
            socketMessage.setCloseCode(SocketCloseCode.NO_STATUS_CODE);
            socketMessage.setContent(exception.getMessage());
            notifyFailure(eventId, socketMessage);
            activeWebSocket = null;
        }
    }


    @NonNull
    private Command<Throwable, Void> notifyConnectionFailed(final long eventId) {
        return new Command<Throwable, Void>() {
            @Override
            public Void execute(Throwable e) {
                activeWebSocket = null;
                Logger.getInstance().exception(e);
                SocketMessage socketMessage = new SocketMessage();
                socketMessage.setCloseCode(SocketCloseCode.NO_STATUS_CODE);
                socketMessage.setMode(SocketMode.OPEN);
                socketMessage.setError(e.getMessage());
                notifyFailure(eventId, socketMessage);
                return null;
            }
        };
    }


    private void send(final long eventId, String message) {
        if (activeWebSocket != null && activeWebSocket.isCurrentMode(SocketMode.OPEN)) {
            activeWebSocket.sendMessage(message)
                    .onComplete(notifySendSuccess(eventId))
                    .onException(notifySendFailure(eventId));

        }
    }

    @NonNull
    private Command<Boolean, Void> notifySendSuccess(final long eventId) {
        return new Command<Boolean, Void>() {
            @Override
            public Void execute(Boolean p) {
                SocketMessage socketMessage = new SocketMessage();
                socketMessage.setMode(SocketMode.SEND);
                notifySuccess(eventId, socketMessage);
                return null;
            }
        };
    }

    @NonNull
    private Command<Throwable, Void> notifySendFailure(final long eventId) {
        return new Command<Throwable, Void>() {
            @Override
            public Void execute(Throwable e) {
                SocketMessage socketMessage = new SocketMessage();
                socketMessage.setMode(SocketMode.SEND);
                socketMessage.setError(e.getMessage());
                notifyFailure(eventId, socketMessage);
                return null;
            }
        };
    }


    private void close(long eventId) {
        if (activeWebSocket != null && activeWebSocket.isCurrentMode(SocketMode.OPEN)) {
            activeWebSocket.closeSocket()
                    .onComplete(notifyClosed(eventId))
                    .onException(notifyCloseFailed(eventId));
        } else {
            logError(eventId, " socket already closed");

        }
    }

    @NonNull
    private Command<Throwable, Void> notifyCloseFailed(final long eventId) {
        return new Command<Throwable, Void>() {
            @Override
            public Void execute(Throwable e) {
                Logger.getInstance().exception(e);
                SocketMessage socketMessage = new SocketMessage();
                socketMessage.setMode(SocketMode.CLOSE);
                socketMessage.setError(e.getMessage());
                notifyFailure(eventId, socketMessage);
                return null;
            }
        };
    }

    @NonNull
    private Command<Boolean, Void> notifyClosed(final long eventId) {
        return new Command<Boolean, Void>() {
            @Override
            public Void execute(Boolean p) {
                if (activeWebSocket != null) {
                    activeWebSocket.mode(SocketMode.CLOSE);
                    activeWebSocket = null;
                }
                SocketMessage socketMessage = new SocketMessage();
                socketMessage.setCloseCode(SocketCloseCode.NORMAL);
                socketMessage.setMode(SocketMode.CLOSE);
                socketMessage.setClosedRemotely(false);
                notifySuccess(eventId, socketMessage);

                return null;
            }
        };
    }


    @Override
    public void clear() {
        super.clear();
        activeWebSocket = null;
    }


    private void notifySuccess(long eventId, SocketMessage message) {
        ResponseMessage.Builder builder = new ResponseMessage.Builder();
        builder.id(eventId);
        builder.content(new Gson().toJson(message));
        builder.successful(true);
        builder.statusCode(ResponseMessage.HTTP_OK);
        notifyCallback(new Event.Builder(eventId).message(builder.build()).build());
    }

    private void notifyFailure(long eventId, SocketMessage message) {
        ResponseMessage.Builder builder = new ResponseMessage.Builder();

        ServerMessage serverMessage = new ServerMessage();
        serverMessage.setProperty(String.valueOf(message.getCloseCode()));
        serverMessage.setMessage(new Gson().toJson(message));
        serverMessage.setMessageType(ServerMessage.MessageType.ERROR);

        builder.id(eventId);
        builder.content(new Gson().toJson(serverMessage));
        builder.successful(false);
        builder.statusCode(message.getCloseCode());
        notifyCallback(new Event.Builder(eventId).message(builder.build()).build());
    }


    private String logError(long eventId, String message) {
        Logger.getInstance().error(getClass(),
                (message = AppResources.resourceEntryName((int) eventId) + message));
        return message;
    }
}

