package com.entities.cached.pickup;

import com.base.annotations.MockEntity;
import com.entities.mocks.pickup.MockedPickupOrder;

import java.io.Serializable;

/**
 * the PickupOrder Object for the active vehicle of the current driver
 * <p>
 * Created by Ahmed Adel on 12/19/2016.
 */
@MockEntity(MockedPickupOrder.class)
public class PickupOrder implements Serializable {

    protected long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
