package com.entities.cached;

import com.base.annotations.MockEntity;
import com.entities.mocks.MockedPickupStatusGroup;

import java.util.HashSet;

/**
 * a group of {@link PickupStatus} instances
 * <p>
 * Created by Wafaa on 12/20/2016.
 */

@MockEntity(MockedPickupStatusGroup.class)
public class PickupStatusGroup extends HashSet<PickupStatus> {

}
