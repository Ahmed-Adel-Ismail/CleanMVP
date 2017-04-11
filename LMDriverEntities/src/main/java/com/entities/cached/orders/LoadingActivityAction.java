package com.entities.cached.orders;

import java.io.Serializable;

/**
 * an action took for an {@link LoadingActivityOrder} item
 * <p>
 * Created by Ahmed Adel on 2/16/2017.
 */
public class LoadingActivityAction implements Serializable {

    private long jobId;
    private long cancellationReasonId;

    public long getCancellationReasonId() {
        return cancellationReasonId;
    }

    public void setCancellationReasonId(long cancellationReasonId) {
        this.cancellationReasonId = cancellationReasonId;
    }

    public long getJobId() {
        return jobId;
    }

    public void setJobId(long jobId) {
        this.jobId = jobId;
    }
}
