package com.entities.cached.orders;

import com.base.cached.LocationsGroup;

import java.io.Serializable;
import java.util.TreeSet;

/**
 * a class that indicates the job orders route on map
 * <p>
 * Created by Ahmed Adel on 3/14/2017.
 */
public class JobOrdersRoute implements Serializable {

    private TreeSet<JobOrder> jobOrders;
    private LocationsGroup route;

    public TreeSet<JobOrder> getJobOrders() {
        return jobOrders;
    }

    public void setJobOrders(TreeSet<JobOrder> jobOrders) {
        this.jobOrders = jobOrders;
    }

    public LocationsGroup getRoute() {
        return route;
    }

    public void setRoute(LocationsGroup route) {
        this.route = route;
    }
}
