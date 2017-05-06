package com.base.usecases.requesters.server.websocket;

import com.base.entities.annotations.MockEntity;
import com.google.gson.Gson;

import java.io.Serializable;

/**
 * a class that indicates a message generated from a Socket interaction
 * <p>
 * Created by Ahmed Adel on 1/19/2017.
 */
@MockEntity(error = SocketMessage.class)
public class SocketMessage implements Serializable {


    @SocketCloseCode
    private int closeCode;
    private boolean closedRemotely;
    private String content;
    private SocketMode mode;
    private String error;

    public SocketMode getMode() {
        return mode;
    }

    public void setMode(SocketMode mode) {
        this.mode = mode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @SocketCloseCode
    public int getCloseCode() {
        return closeCode;
    }

    public void setCloseCode(@SocketCloseCode int closeCode) {
        this.closeCode = closeCode;
    }

    public boolean isClosedRemotely() {
        return closedRemotely;
    }

    public void setClosedRemotely(boolean closedRemotely) {
        this.closedRemotely = closedRemotely;
    }

    /**
     * get the Content that is stored as Json casted to the expected type
     *
     * @param klass the {@link Class} of the expected type
     * @param <T>   the type of the Object expected
     * @return the Json as an Object of the expected type
     */
    public <T> T parseContent(Class<T> klass) {
        return new Gson().fromJson(content, klass);
    }

}
