package com.entities.cached.cancellation;

import com.base.annotations.MockEntity;
import com.entities.mocks.cancellation.MockedCancellationReason;

import java.io.Serializable;

/**
 * the reason for cancelling a an order
 * <p>
 * Created by Ahmed Adel on 12/19/2016.
 */
@MockEntity(MockedCancellationReason.class)
public class CancellationReason implements Serializable, Comparable<CancellationReason> {

    protected long id;
    protected String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return (int) id % 2;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null
                && obj instanceof CancellationReason
                && ((CancellationReason) obj).id == id;
    }

    @Override
    public int compareTo(CancellationReason o) {
        if (o == null || o.name == null) {
            return -1;
        } else if (name == null) {
            return 1;
        } else {
            return name.compareTo(o.name);
        }
    }
}
