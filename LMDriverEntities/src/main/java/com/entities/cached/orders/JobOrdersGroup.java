package com.entities.cached.orders;

import com.base.annotations.MockEntity;
import com.entities.mocks.orders.MockedJobOrdersGroup;

import java.util.LinkedList;

/**
 * a group of {@link JobOrder} Objects
 * <p>
 * Created by Ahmed Adel on 2/26/2017.
 */
@MockEntity(MockedJobOrdersGroup.class)
public class JobOrdersGroup extends LinkedList<JobOrder> {


}
