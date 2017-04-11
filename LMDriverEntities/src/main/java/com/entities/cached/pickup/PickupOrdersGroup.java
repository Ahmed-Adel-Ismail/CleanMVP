package com.entities.cached.pickup;

import com.base.annotations.MockEntity;
import com.entities.mocks.pickup.MockedPickupOrdersGroup;

import java.util.LinkedList;

/**
 * the group of {@link PickupOrder} instances
 * <p>
 * Created by Ahmed Adel on 12/19/2016.
 */
@MockEntity(MockedPickupOrdersGroup.class)
public class PickupOrdersGroup extends LinkedList<PickupOrder> {

}
