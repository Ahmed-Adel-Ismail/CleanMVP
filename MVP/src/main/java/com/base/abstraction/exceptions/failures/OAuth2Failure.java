package com.base.abstraction.exceptions.failures;

import com.base.abstraction.R;

/**
 * a {@link Failure} that indicates an un-authorized request to server
 * <p>
 * Created by Ahmed Adel on 10/24/2016.
 */
public class OAuth2Failure extends Failure {

    public OAuth2Failure() {
    }

    @Override
    protected long getFailureHandlerAddress() {
        return R.id.addressAuthHandler;
    }
}
