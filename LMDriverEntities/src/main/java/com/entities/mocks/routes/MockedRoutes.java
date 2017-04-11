package com.entities.mocks.routes;

import com.entities.cached.routes.*;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Wafaa on 1/30/2017.
 */

public class MockedRoutes extends Routes {

    public MockedRoutes() {
        super.plan = createNewPlane();
    }

    public Plan createNewPlane() {
        Plan plan = new Plan();
        plan.setItineraries(createItineraries());
        return plan;
    }

    private List<Itineraries> createItineraries() {
        Itineraries itineraries = new Itineraries();
        itineraries.setLegs(createLegsArray());
        List<Itineraries> itineraries1 = new LinkedList<>();
        itineraries1.add(itineraries);
        return itineraries1;
    }

    private List<Legs> createLegsArray() {
        Legs legs = new Legs();
        List<Legs> legsArray = new LinkedList<>();
        legs.setFrom(createFromPoint());
        legs.setTo(createToPoint());
        legs.setSteps(createSteps());
        legsArray.add(legs);
        return legsArray;
    }

    private Point createFromPoint() {
        Point point = new Point();
        point.setLon(30.9660012);
        point.setLat(29.9740701);
        return point;
    }

    private Point createToPoint() {
        Point point = new Point();
        point.setLon(30.9657061);
        point.setLat(29.9731384);
        return point;
    }


    private List<Steps> createSteps() {
        List<Steps> steps = new LinkedList<>();
        steps.add(new Steps(30.9660012, 29.9740701));
        steps.add(new Steps(30.9660816, 29.9739238));
        steps.add(new Steps(30.9661809, 29.9737379));
        steps.add(new Steps(30.9662694, 29.9735915));
        steps.add(new Steps(30.9657061, 29.9731384));
        return steps;
    }
}
