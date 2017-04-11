package com.base.usecases.exceptions;

import com.base.usecases.requesters.server.mocks.MockedEntitiesRegistry;

/**
 * a {@link RuntimeException} that is thrown when {@link MockedEntitiesRegistry#get(Long)} fails
 * to get the Object to work with
 * <p>
 * Created by Ahmed Adel on 1/1/2017.
 */
public class NoMockedResponseException extends RuntimeException {

    public NoMockedResponseException(String message) {
        super("no mocked response entity for : " + message);
    }
}
