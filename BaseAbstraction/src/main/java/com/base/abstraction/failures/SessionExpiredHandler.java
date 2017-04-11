package com.base.abstraction.failures;

import com.base.abstraction.R;
import com.base.abstraction.exceptions.failures.SessionExpiredFailure;

/**
 * a class to handle the {@link SessionExpiredFailure}
 * <p>
 * Created by Ahmed Adel on 10/30/2016.
 */
public abstract class SessionExpiredHandler extends FailureHandler {


    @Override
    public final long getActorAddress() {
        return R.id.addressSessionExpiredHandler;
    }
}
