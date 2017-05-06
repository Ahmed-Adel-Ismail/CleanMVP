package com.base.entities.cached;

import java.util.LinkedHashSet;

/**
 * a group of {@link Location} Objects
 * <p>
 * Created by Ahmed Adel on 3/21/2017.
 */
public class LocationsGroup extends LinkedHashSet<Location> {


    /**
     * get the {@link RouteEdges} of the Route represented by this {@link LocationsGroup}
     *
     * @return a valid {@link RouteEdges} object
     * @throws NotRouteException if this {@link LocationsGroup} is not a route, like having less
     *                           than 2 {@link Location} objects
     */
    public RouteEdges getRouteEdges() throws NotRouteException {
        if (size() < 2) throw new NotRouteException("locations size : " + size());
        Location[] locationsArray = toArray(new Location[0]);
        return new RouteEdges(locationsArray[0], locationsArray[locationsArray.length - 1]);
    }


    /**
     * a class that holds the start and end locations for a {@link LocationsGroup} that indicates a
     * Route on a map
     */
    public static class RouteEdges {
        public final Location start;
        public final Location end;

        private RouteEdges(Location start, Location end) {
            this.start = start;
            this.end = end;
        }
    }

    /**
     * a {@link RuntimeException} thrown when there is no valid route found in
     * {@link LocationsGroup}
     */
    public static class NotRouteException extends RuntimeException {

        private NotRouteException(String message) {
            super(message);
        }
    }


}
