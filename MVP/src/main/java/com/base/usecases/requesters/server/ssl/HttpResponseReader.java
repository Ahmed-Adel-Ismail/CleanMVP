package com.base.usecases.requesters.server.ssl;

import android.support.annotation.NonNull;

import com.base.abstraction.commands.Command;
import com.base.abstraction.logs.Logger;
import com.base.usecases.events.ResponseMessage;
import com.base.usecases.failures.FailureMessageGenerator;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import static java.net.HttpURLConnection.HTTP_OK;

/**
 * A class to read the Http Response
 * <p>
 * Created by Ahmed Adel on 10/18/2016.
 */
@SuppressWarnings("deprecation")
class HttpResponseReader implements Command<HttpResponse, ResponseMessage> {


    private static final int HTTP_RESPONSE_OK = HTTP_OK;
    private long eventId;

    HttpResponseReader(long eventId) {
        this.eventId = eventId;
    }

    @Override
    public ResponseMessage execute(HttpResponse response) {
        ResponseMessage responseMessage;

        if (response != null) {
            responseMessage = createMessageFromResponse(response);
        } else {
            responseMessage = new FailureMessageGenerator().execute(eventId);

        }
        return responseMessage;
    }

    private ResponseMessage createMessageFromResponse(@NonNull HttpResponse response) {


        int statusCode = response.getStatusLine().getStatusCode();
        HttpEntity entity = response.getEntity();
        String serverResponse = new HttpEntityReader().execute(entity);

        logResponse(statusCode, serverResponse);

        ResponseMessage.Builder builder = new ResponseMessage.Builder();
        builder.id(eventId);
        builder.content(serverResponse);
        builder.successful(statusCode == HTTP_RESPONSE_OK);
        builder.statusCode(statusCode);
        return builder.build();
    }

    private void logResponse(int statusCode, String serverResponse) {
        Logger.getInstance().error(getClass(), "statusCode : " + statusCode);
        Logger.getInstance().error(getClass(), "body : " + serverResponse);
    }
}
