package com.appzoneltd.lastmile.driver.features.main.home;

import android.support.annotation.NonNull;

import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.RxCommand;
import com.base.abstraction.interfaces.Clearable;
import com.base.abstraction.logs.Logger;
import com.entities.cached.RouteEdges;
import com.mapzen.android.graphics.model.CameraType;
import com.mapzen.android.routing.MapzenRouter;
import com.mapzen.model.ValhallaLocation;
import com.mapzen.tangram.LngLat;
import com.mapzen.valhalla.Route;
import com.mapzen.valhalla.RouteCallback;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by Wafaa on 2/7/2017.
 */

class MapzenNavigationCommand implements Command<RouteEdges, Void> , Clearable{

    private static final float ZOOM_VALUE = 16f;
    private static final float ROUTING_TILT = 0.96f;

    private HomeViewModel viewModel;
    private Disposable onLocationChangedDisposable;
    private Disposable onRouteChangedDisposable;

    MapzenNavigationCommand(HomeViewModel viewModel) {
        this.viewModel = viewModel;
        onLocationChangedDisposable = viewModel.locationChangedObservable.subscribe(createOnLocationChangedNextCommand());
        onRouteChangedDisposable = viewModel.routeChangedObservable.subscribe(createOnRouteChangedCommand());
    }

    @NonNull
    private RxCommand<ValhallaLocation> createOnLocationChangedNextCommand() {
        return new RxCommand<ValhallaLocation>() {
            @Override
            public Void execute(ValhallaLocation location) {
                reRenderMapMarker(location);
                rotateMapzen(location);
                return null;
            }
        };
    }

    private RxCommand<ValhallaLocation> createOnRouteChangedCommand() {
        return new RxCommand<ValhallaLocation>() {
            @Override
            public Void execute(ValhallaLocation location) {
                RouteEdges routeEdges = createDestination(location);
                MapzenNavigationCommand.this.execute(routeEdges);
                return null;
            }
        };
    }


    private RouteEdges createDestination(ValhallaLocation location) {
        RouteEdges routeEdges = new RouteEdges();
        routeEdges.setSourceLat(location.getLatitude());
        routeEdges.setSourceLng(location.getLongitude());
        routeEdges.setDestinationLat(viewModel.navigationRoute.get().getDestinationLat());
        routeEdges.setDestinationLng(viewModel.navigationRoute.get().getDestinationLng());
        return routeEdges;
    }

    @Override
    public Void execute(RouteEdges routeEdges) {
        viewModel.mapzenMap.clearDroppedPins();
        viewModel.mapzenMap.clearRoutePins();
        viewModel.router.clearLocations();
        viewModel.mapzenMap.clearRouteLine();
        viewModel.mapzenMap.clearRouteLocationMarker();
        viewModel.mapzenMap.clearTransitRouteLine();
        viewModel.router.setCallback(createFetchRouteCallback(routeEdges));
        double[] fromPoint = {routeEdges.getSourceLat(), routeEdges.getSourceLng()};
        double[] toPint = {routeEdges.getDestinationLat(), routeEdges.getDestinationLng()};
        viewModel.router.setLocation(fromPoint)
                .setLocation(toPint)
                .setDistanceUnits(MapzenRouter.DistanceUnits.KILOMETERS)
                .fetch();
        return null;
    }


    @NonNull
    private RouteCallback createFetchRouteCallback(final RouteEdges routeEdges) {
        return new RouteCallback() {
            @Override
            public void success(@NonNull Route route) {
                if (viewModel.isDestroyed()) {
                    Logger.getInstance().error(MapzenNavigationCommand.class
                            , "ViewModel Destroyed @ createFetchRouteCallback()");
                    return;
                }
                viewModel.routeListener.setRoute(route);
                viewModel.engine.setRoute(route);
                drawRoutePoints(route, routeEdges);
                viewModel.mapzenMap.setCameraType(CameraType.PERSPECTIVE);
                viewModel.mapzenMap.setTilt(ROUTING_TILT);
            }

            @Override
            public void failure(int i) {
                Logger.getInstance().error(MapzenRouteDrawerObservable.class, "fail:" + i);
            }
        };
    }

    private void drawRoutePoints(Route route, RouteEdges routeEdges) {
        ArrayList<ValhallaLocation> geometry = route.getGeometry();
        rotateMapzen(geometry.get(0));
        List<LngLat> points = new LinkedList<>();
        for (ValhallaLocation location : geometry) {
            LngLat lngLat = new LngLat(location.getLongitude(), location.getLatitude());
            points.add(lngLat);
        }
        viewModel.mapzenMap.getMapController().setZoom(ZOOM_VALUE);
        viewModel.mapzenMap.getMapController().setPosition(new LngLat(routeEdges.getSourceLng(), routeEdges.getSourceLat()));
        viewModel.mapzenMap.drawRouteLocationMarker(new LngLat(routeEdges.getSourceLng(), routeEdges.getSourceLat()));
        viewModel.mapzenMap.drawDroppedPin(new LngLat(routeEdges.getDestinationLng(), routeEdges.getDestinationLat()));
        viewModel.mapzenMap.drawRouteLine(points);
    }

    private void reRenderMapMarker(ValhallaLocation location) {
        viewModel.mapzenMap.clearRouteLocationMarker();
        viewModel.mapzenMap.drawRouteLocationMarker(
                new LngLat(location.getLongitude(), location.getLatitude()));
    }

    private void rotateMapzen(ValhallaLocation location) {
        viewModel.mapzenMap.setRotation((float) Math.toRadians((double)
                (360 - location.getBearing())));
    }


    @Override
    public void clear() {
        if(onLocationChangedDisposable != null){
            if(!onLocationChangedDisposable.isDisposed()) onLocationChangedDisposable.dispose();
            onLocationChangedDisposable = null;
        }

        if(onRouteChangedDisposable != null){
            if(!onRouteChangedDisposable.isDisposed()) onRouteChangedDisposable.dispose();
            onRouteChangedDisposable = null;
        }

    }
}
