package com.entities.mocks.cache;

import java.io.Serializable;
import java.util.HashMap;

/**
 * a cache for Mocked Objects to store there related values
 * <p>
 * Created by Ahmed Adel on 1/1/2017.
 */

public class MocksCache extends HashMap<CacheKeys, Serializable> {

    private static MocksCache instance;

    private MocksCache() {

    }

    public static MocksCache getInstance() {
        if (instance == null) {
            instance = new MocksCache();
        }
        return instance;
    }

}
