package com.base.usecases.requesters.server.websocket;

import android.support.annotation.NonNull;

import com.base.abstraction.api.usecases.RequestUrlLocator;
import com.base.abstraction.commands.RxCommand;
import com.base.abstraction.events.Event;
import com.base.abstraction.logs.Logger;
import com.base.usecases.events.ResponseMessage;
import com.base.usecases.events.SocketRequestMessage;
import com.base.usecases.requesters.server.base.ServerRequester;
import com.base.usecases.requesters.server.ssl.HttpUrlGenerator;

import java.net.URI;
import java.net.URISyntaxException;

import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

import static com.base.usecases.events.ResponseMessage.HTTP_OK;
import static com.base.usecases.events.ResponseMessage.HTTP_SERVER_ERROR;

/**
 * A web socket requester that open a connection with a port to listen to
 * a chanel and parse the messages.
 * <p>
 * Created by Wafaa on 12/4/2016.
 *
 * @deprecated use {@link StaticWebSocketRequester} or {@link AuthorizedWebSocketsRequester} for
 * full implementation
 */
@Deprecated
public class WebSocketRequester extends ServerRequester {

    private Disposable disposable;
    private Subject<String> observable;
    private long eventId;
    private WebSocketState webSocketState;

    public WebSocketRequester(@NonNull RequestUrlLocator requestUrlLocator) {
        super(requestUrlLocator);
        observable = PublishSubject.create();
        disposable = observable.subscribe(createOnNextCommand(), createOnErrorCommand());
    }

    @NonNull
    private RxCommand<String> createOnNextCommand() {
        return new RxCommand<String>() {
            @Override
            public Void execute(String message) {
                ResponseMessage.Builder builder = new ResponseMessage.Builder();
                builder.id(eventId);
                builder.content(message);
                builder.successful(true);
                builder.statusCode(HTTP_OK);
                notifyCallback(new Event.Builder(eventId).message(builder.build()).build());
                return null;
            }
        };
    }

    @NonNull
    private RxCommand<Throwable> createOnErrorCommand() {
        return new RxCommand<Throwable>() {
            @Override
            public Void execute(Throwable ex) {
                ResponseMessage.Builder builder = new ResponseMessage.Builder();
                builder.id(eventId);
                builder.content(ex.getMessage());
                builder.successful(false);
                builder.statusCode(HTTP_SERVER_ERROR);
                notifyCallback(new Event.Builder(eventId).message(builder.build()).build());
                return null;
            }
        };
    }

    @Override
    public Void execute(Event event) {
        eventId = event.getId();
        String url = createRequestUrl(event);
        SocketRequestMessage message = event.getMessage();
        SocketMode mode = message.getSocketMode();
        URI uri = null;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            Logger.getInstance().exception(e);
        }
        if (uri != null) {
            initializeWebSocket(uri, mode);
            new SwitchWebSocketModeCommand().execute(webSocketState);
        }
        return null;
    }

    private String createRequestUrl(Event event) {
        String baseUrl = getRequestUrlLocator().execute(event.getId());
        return new HttpUrlGenerator(baseUrl).execute(event.getMessage());
    }

    private void initializeWebSocket(URI uri, SocketMode mode) {
        if (webSocketState == null) {
            webSocketState = new WebSocketState();
            webSocketState.setModes(mode);
            webSocketState.setWebSocket(new WebSocket(uri, observable));
        }
    }


    @Override
    public void clear() {
        super.clear();
        if (disposable != null) {
            if (!disposable.isDisposed()) {
                disposable.dispose();
            }
            disposable = null;
        }
    }

}
