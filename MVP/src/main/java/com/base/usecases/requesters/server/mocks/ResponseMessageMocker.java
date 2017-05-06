package com.base.usecases.requesters.server.mocks;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.base.abstraction.commands.Command;
import com.base.abstraction.logs.Logger;
import com.base.abstraction.system.App;
import com.base.usecases.events.ResponseMessage;
import com.google.gson.Gson;

import java.io.Serializable;

/**
 * a class that generates mocked successful {@link ResponseMessage}
 * <p>
 * Created by Ahmed Adel on 11/20/2016.
 */
class ResponseMessageMocker implements Command<MockedEntity, ResponseMessage> {

    private long eventId;

    ResponseMessageMocker(long eventId) {
        this.eventId = eventId;
    }

    @Override
    public ResponseMessage execute(MockedEntity mockedEntity) {
        if (isNetworkAvailable()) {
            return createMockedResponseMessage(mockedEntity);
        } else {
            return createFailureResponseMessage(mockedEntity);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connMgr = (ConnectivityManager) App.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private ResponseMessage createMockedResponseMessage(MockedEntity mockedEntity) {
        ResponseMessage.Builder builder = new ResponseMessage.Builder();
        builder.id(eventId);
        builder.statusCode(mockedEntity.getStatusCode());
        builder.successful(mockedEntity.isSuccessfulResponse());

        Serializable entity = (mockedEntity.isSuccessfulResponse())
                ? mockedEntity.getSuccessResponse()
                : mockedEntity.getErrorResponse();

        String response = new Gson().toJson(entity);
        builder.content(response);

        Logger.getInstance().error(MockRequester.class, "statusCode : " + mockedEntity.getStatusCode());
        Logger.getInstance().error(MockRequester.class, "body : " + response);


        return builder.build();
    }

    private ResponseMessage createFailureResponseMessage(MockedEntity mockedEntity) {
        ResponseMessage.Builder builder = new ResponseMessage.Builder();
        builder.id(eventId);
        builder.statusCode(ResponseMessage.HTTP_NO_RESPONSE);
        builder.successful(false);

        Serializable entity = mockedEntity.getErrorResponse();

        String response = new Gson().toJson(entity);
        builder.content(response);

        Logger.getInstance().error(MockRequester.class, "statusCode : " + ResponseMessage.HTTP_NO_RESPONSE);
        Logger.getInstance().error(MockRequester.class, "body : " + response);


        return builder.build();
    }


}
