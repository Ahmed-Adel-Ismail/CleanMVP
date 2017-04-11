package com.appzoneltd.lastmile.driver.features.main.home;

import com.base.abstraction.commands.Command;
import com.base.cached.Location;
import com.base.cached.LocationsGroup;
import com.base.cached.LocationsGroup.NotRouteException;
import com.mapzen.android.graphics.MapzenMap;
import com.mapzen.tangram.LngLat;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;

/**
 * a class that draws the route on map based on {@link LocationsGroup} passed, the
 * {@link #execute(LocationsGroup)} method may throw {@link NotRouteException} if the passed
 * {@link LocationsGroup} is not a valid rout with a start and an end
 * <p>
 * Created by Wafaa on 2/1/2017.
 */
class MapzenRouteDrawerObservable implements Command<LocationsGroup, Maybe<Location>> {

    private MapzenMap mapzenMap;
    private static final float ZOOM_VALUE = 16f;
    private Command<Location, LngLat> lngLatAdapter;


    MapzenRouteDrawerObservable(MapzenMap mapzenMap) {
        this.mapzenMap = mapzenMap;
        this.lngLatAdapter = createLngLatAdapter();
    }


    @NotNull
    private Command<Location, LngLat> createLngLatAdapter() {
        return new Command<Location, LngLat>() {
            @Override
            public LngLat execute(Location location) {
                return new LngLat(location.getLongitude(), location.getLatitude());
            }
        };
    }


    @Override
    public Maybe<Location> execute(LocationsGroup route) throws NotRouteException {

        LocationsGroup.RouteEdges routeEdges = route.getRouteEdges();
        LngLat startLocation = lngLatAdapter.execute(routeEdges.start);
        LngLat endLocation = lngLatAdapter.execute(routeEdges.end);

        mapzenMap.clearDroppedPins();
        mapzenMap.getMapController().setZoom(ZOOM_VALUE);
        mapzenMap.getMapController().setPosition(startLocation);
        mapzenMap.drawRoutePins(startLocation, endLocation);
        mapzenMap.setTilt(5f);

        return Observable.fromIterable(route).reduce(drawSegments());
    }


    @NotNull
    private BiFunction<Location, Location, Location> drawSegments() {
        return new BiFunction<Location, Location, Location>() {
            @Override
            public Location apply(Location firstLocation, Location secondLocation) throws Exception {
                mapzenMap.drawRouteLine(segmentLngLats(firstLocation, secondLocation));
                return secondLocation;
            }
        };
    }

    private List<LngLat> segmentLngLats(Location firstLocation, Location secondLocation) {
        return Observable.fromArray(firstLocation, secondLocation)
                .map(convertToLngLat())
                .toList()
                .blockingGet();
    }

    @NotNull
    private Function<Location, LngLat> convertToLngLat() {
        return new Function<Location, LngLat>() {
            @Override
            public LngLat apply(Location location) throws Exception {
                return new LngLat(location.getLongitude(), location.getLatitude());
            }
        };
    }


}
