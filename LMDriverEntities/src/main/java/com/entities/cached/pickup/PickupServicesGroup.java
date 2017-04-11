package com.entities.cached.pickup;

import com.base.annotations.MockEntity;
import com.entities.mocks.pickup.MockedPickupServicesGroup;

import java.util.LinkedList;

/**
 * a group of {@link PickupService} Objects
 * <p>
 * Created by Ahmed Adel on 1/1/2017.
 */
@MockEntity(MockedPickupServicesGroup.class)
public class PickupServicesGroup extends LinkedList<PickupService> {
}
