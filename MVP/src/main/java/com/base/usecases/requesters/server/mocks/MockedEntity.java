package com.base.usecases.requesters.server.mocks;

import java.io.Serializable;

import static com.base.usecases.events.ResponseMessage.HTTP_OK;

/**
 * a class that represents a Mocked {@link Serializable}, encapsulating it's required data to be used
 * along multiple classes
 * <p>
 * Created by Ahmed Adel on 12/4/2016.
 */
public class MockedEntity {

    private int statusCode;
    private long requestId;
    private Serializable successResponse;
    private Serializable errorResponse;

    public Serializable getErrorResponse() {
        return errorResponse;
    }

    public void setErrorResponse(Serializable errorResponse) {
        this.errorResponse = errorResponse;
    }

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Serializable getSuccessResponse() {
        return successResponse;
    }

    public void setSuccessResponse(Serializable successResponse) {
        this.successResponse = successResponse;
    }

    public boolean isSuccessfulResponse() {
        return statusCode == HTTP_OK;
    }
}
