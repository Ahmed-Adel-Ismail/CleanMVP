package com.entities.mocks.orders;

import com.base.cached.Location;
import com.entities.cached.orders.JobOrder;
import com.entities.cached.orders.JobOrderTiming;
import com.entities.cached.orders.JobOrdersGroup;

import java.util.Date;


public class MockedJobOrdersGroup extends JobOrdersGroup {

//    type	latitude	longitude	name
//    W	29.9988840	30.9676480	MUST
//    W	29.9915155	30.9645796	Nabatat
//    W	29.9813772	30.9516406	october club
//    W	29.9723531	30.9445381	Hosary square
//    W	29.9609022	30.9292173	Coop station
//    W	29.9512209	30.9123945	Vodafone square


    private static final Location MUST_UNIVERSITY = new Location(29.9988840, 30.9676480);
    private static final Location NABATAT_STREET = new Location(29.9915155, 30.9645796);
    private static final Location OCTOBER_CLUB = new Location(29.9813772, 30.9516406);
    private static final Location HOSARY_SQUARE = new Location(29.9723531, 30.9445381);
    private static final Location COOP_FUEL_STATION = new Location(29.9609022, 30.9292173);
    private static final Location VODAFONE_SQUARE = new Location(29.9512209, 30.9123945);

    public MockedJobOrdersGroup() {
        add(createJobOrder(1, MUST_UNIVERSITY, "MUST university, 6th of October ... Al-Mehwar Al-Markazy"));
        add(createJobOrder(2, NABATAT_STREET, "8 Nabatat Street, near Race for renting cars"));
        add(createJobOrder(3, OCTOBER_CLUB, "6th of October club ... Al-Mehwar Al-Markazy"));
        add(createJobOrder(4, HOSARY_SQUARE, "Al-Hosary square, 6th of October ... Al-Mehwar Al-Markazy"));
        add(createJobOrder(5, COOP_FUEL_STATION, "Coop fuel station ... Al-Mehwar Al-Markazy"));
        add(createJobOrder(6, VODAFONE_SQUARE, "Vodafone Square, 6th of October ... Al-Mehwar Al-Markazy"));


    }

    private JobOrder createJobOrder(long id, Location location, String address) {
        JobOrder jobOrder = new JobOrder();
        jobOrder.setLocation(location);
        jobOrder.setAddress(address);
        jobOrder.setPriority((int) id);
        jobOrder.setId(id);
        jobOrder.setPackageId(id);
        jobOrder.setWeight(10D + id);
        jobOrder.setTiming(createJobOrderTiming());
        return jobOrder;
    }

    private JobOrderTiming createJobOrderTiming() {
        JobOrderTiming timing = new JobOrderTiming();
        timing.setPickupStart("00:00 am");
        timing.setPickupEnd("00:00 am");
        timing.setArrival(new Date());
        timing.setDeparture(new Date());
        timing.setEtaSeconds(60L * 60L);
        return timing;
    }
}
