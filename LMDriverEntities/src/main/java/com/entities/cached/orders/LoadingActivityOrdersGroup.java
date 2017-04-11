package com.entities.cached.orders;

import com.base.annotations.MockEntity;
import com.entities.mocks.orders.MockedLoadingActivityOrdersGroup;

import java.util.LinkedList;

/**
 * a group of {@link LoadingActivityOrder}
 * <p>
 * Created by Ahmed Adel on 2/16/2017.
 */
@MockEntity(MockedLoadingActivityOrdersGroup.class)
public class LoadingActivityOrdersGroup extends LinkedList<LoadingActivityOrder> {
}
