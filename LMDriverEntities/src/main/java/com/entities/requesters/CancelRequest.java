package com.entities.requesters;

/**
 * an Object that indicate a request to cancel a pickup order
 * <p>
 * Created by Ahmed Adel on 12/19/2016.
 */
public class CancelRequest extends BaseRequest {

    private long reasonId;
    private String description;
    private long requestId;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getReasonId() {
        return reasonId;
    }

    public void setReasonId(long reasonId) {
        this.reasonId = reasonId;
    }

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }
}
