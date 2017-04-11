package com.base.usecases.events;

import com.base.abstraction.events.Message;
import com.base.abstraction.interfaces.Successable;
import com.base.abstraction.system.ResourcesReader;


/**
 * a {@link Message} Object that implements the {@link Successable} interface,
 * used for Messages that can hold value or failure scenarios like Responses
 * <p/>
 * Created by Ahmed Adel on 8/31/2016.
 */
public class ResponseMessage extends Message implements Successable {

    public static final int HTTP_NO_RESPONSE = -1;
    public static final int HTTP_OK = 200;
    public static final int HTTP_UNAUTHORIZED = 401;
    public static final int HTTP_SERVER_ERROR = 500;

    private final int statusCode;
    private final boolean success;

    protected ResponseMessage(long id, Object content, boolean success, int statusCode) {
        super(id, content);
        this.success = success;
        this.statusCode = statusCode;
    }

    @Override
    public boolean isSuccessful() {
        return success;
    }

    /**
     * check the {@code status code} of this {@link ResponseMessage} ... the default value
     * is {@link #HTTP_NO_RESPONSE}
     *
     * @param statusCode the status code to compare with
     * @return {@code true} if the passed {@code status code} is the same as the
     * available one, else {@code false}
     * @see java.net.HttpURLConnection
     */
    public boolean isStatusCode(int statusCode) {
        return this.statusCode == statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public String toString() {
        return "[id=" +
                new ResourcesReader().execute(getId()) +
                ", success=" +
                success +
                ", statusCode=" +
                statusCode +
                ", content=" +
                String.valueOf(getContent()).trim() +
                "]";
    }

    @Override
    public ResponseMessage.Builder copyBuilder() {
        return new ResponseMessage.Builder()
                .id(getId())
                .content(getContent())
                .statusCode(getStatusCode())
                .successful(isSuccessful());

    }

    public static class Builder extends Message.Builder {

        boolean successful;
        int statusCode = HTTP_NO_RESPONSE;

        @Override
        public Builder id(long id) {
            super.id(id);
            return this;
        }

        @Override
        public Builder content(Object content) {
            super.content(content);
            return this;
        }

        @SuppressWarnings("unchecked")
        public Builder successful(boolean successful) {
            this.successful = successful;
            return this;
        }


        public Builder statusCode(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        @Override
        public ResponseMessage build() {
            return new ResponseMessage(id, content, successful, statusCode);
        }
    }

}
