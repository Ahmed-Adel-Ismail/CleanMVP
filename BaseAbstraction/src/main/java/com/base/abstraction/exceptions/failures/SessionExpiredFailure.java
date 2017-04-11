package com.base.abstraction.exceptions.failures;

import com.base.abstraction.R;

/**
 * a {@link Failure} that indicates that a session has been expired
 * <p>
 * Created by Ahmed Adel on 10/25/2016.
 */
public class SessionExpiredFailure extends Failure {

    public SessionExpiredFailure() {
    }

    public SessionExpiredFailure(String detailsMessage) {
        super(detailsMessage);
    }

    @Override
    protected long getFailureHandlerAddress() {
        return R.id.addressSessionExpiredHandler;
    }
}
