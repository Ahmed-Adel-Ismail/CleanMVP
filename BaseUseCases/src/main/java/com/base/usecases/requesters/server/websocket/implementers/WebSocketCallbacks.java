package com.base.usecases.requesters.server.websocket.implementers;

import com.base.abstraction.commands.Command;
import com.base.usecases.requesters.server.websocket.SocketMessage;

/**
 * an interface implemented by classes that acts upon callbacks for {@link WebSocket}
 * classes
 * <p>
 * Created by Ahmed Adel on 1/24/2017.
 */
public interface WebSocketCallbacks<T extends WebSocketCallbacks<T>> {

    /**
     * on socket connection opened
     *
     * @param onOpen a {@link Command} to be executed when a connection is opened, it's
     *               {@link Command#execute(Object)} method will be passed {@code true} if
     *               the connection is opened successfully
     * @return {@code this} instance for chaining
     */
    T onOpen(Command<Exception, ?> onOpen);

    /**
     * on message received from socket message
     *
     * @param onMessages a {@link Command} to be executed when ever a message is received from the
     *                   opened socket connection
     * @return {@code this} instance for chaining
     */
    T onMessages(Command<String, ?> onMessages);

    /**
     * on socket connection closed / disconnected
     *
     * @param onClose a {@link Command} to be executed when a connection is closed
     * @return {@code this} instance for chaining
     */
    T onClose(Command<SocketMessage, ?> onClose);

    /**
     * on error occurred in socket connection
     *
     * @param onError a {@link Command} to be executed when a connection error happens
     * @return {@code this} instance for chaining
     */
    T onError(Command<Exception, ?> onError);
}
