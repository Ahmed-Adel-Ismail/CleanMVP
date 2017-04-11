package com.base.usecases.requesters.server.mocks;

import android.annotation.SuppressLint;

import com.base.abstraction.aggregates.AggregateContainable;
import com.base.abstraction.aggregates.KeyAggregateAddable;
import com.base.abstraction.aggregates.KeyAggregateAllAddable;
import com.base.abstraction.aggregates.KeyAggregateGettable;
import com.base.abstraction.interfaces.Clearable;
import com.base.abstraction.interfaces.Emptyable;
import com.base.abstraction.system.AppResources;
import com.base.usecases.annotations.Mock;
import com.base.usecases.exceptions.NoMockedResponseException;

import java.util.HashMap;

/**
 * a class that holds all the Entities instances that are used in mocked responses
 * <p>
 * Created by Ahmed Adel on 11/20/2016.
 */
public class MockedEntitiesRegistry implements
        KeyAggregateGettable<Long, MockedEntity>,
        KeyAggregateAddable<Long, MockedEntity>,
        KeyAggregateAllAddable<Void, MockedEntitiesRegistry>,
        AggregateContainable<Long>,
        Emptyable,
        Clearable {

    private final HashMap<Long, MockedEntity> responses;

    @SuppressLint("UseSparseArrays")
    public MockedEntitiesRegistry() {
        responses = new HashMap<>();
    }

    @Override
    public MockedEntity put(Long requestId, MockedEntity response) {
        responses.put(requestId, response);
        return response;
    }

    /**
     * @throws NoMockedResponseException if no {@link MockedEntity} mapped to the
     *                                   given request id ... this entity is generated
     *                                   from {@link Mock} annotation
     */
    @Override
    public MockedEntity get(Long requestId) {
        MockedEntity response = responses.get(requestId);
        if (response == null) {
            throw new NoMockedResponseException(
                    AppResources.resourceEntryName(Integer.valueOf(requestId.toString())));
        }
        return response;
    }

    @Override
    public boolean isEmpty() {
        return responses.isEmpty();
    }


    @Override
    public boolean contains(Long object) {
        return responses.get(object) != null;
    }

    @Override
    public Void putAll(MockedEntitiesRegistry mockedEntitiesRegistry) {
        if (mockedEntitiesRegistry != null) {
            responses.putAll(mockedEntitiesRegistry.responses);
        }
        return null;
    }

    @Override
    public void clear() {
        responses.clear();
    }

}
