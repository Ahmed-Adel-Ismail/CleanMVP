package com.base.abstraction.aggregates;

import java.util.Map;
import java.util.Set;

/**
 * interface implemented by classes that has an Aggregate data and want an API to
 * return the key values as a {@link Set}, similar to {@link Map#keySet()}
 * <p/>
 * Created by Ahmed Adel on 9/20/2016.
 *
 * @param <Key> the type of the keys exected
 */
public interface AggregateKeySet<Key> extends KeyAggregate {

    /**
     * get the {@link Set} of Keys in the current Aggregate Class
     *
     * @return the Keys as a {@link Set}
     */
    Set<Key> keySet();

}
