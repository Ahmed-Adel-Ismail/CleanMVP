package com.appzoneltd.lastmile.driver.features.main.home;


import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ProgressBar;

import com.appzoneltd.lastmile.driver.R;
import com.appzoneltd.lastmile.driver.subfeatures.SnackbarGenerator;
import com.base.abstraction.annotations.interfaces.Executable;
import com.base.abstraction.annotations.interfaces.Initializer;
import com.base.abstraction.commands.Command;
import com.base.abstraction.exceptions.CheckedReferenceClearedException;
import com.base.abstraction.system.AppResources;
import com.base.presentation.annotations.interfaces.Sync;
import com.base.presentation.base.presentation.ViewModel;
import com.base.presentation.references.BooleanProperty;
import com.base.presentation.references.Consumable;
import com.base.presentation.references.Property;
import com.entities.cached.RouteEdges;
import com.entities.cached.orders.JobOrdersRoute;
import com.mapzen.android.graphics.MapView;
import com.mapzen.android.graphics.MapzenMap;
import com.mapzen.android.graphics.OnMapReadyCallback;
import com.mapzen.android.routing.MapzenRouter;
import com.mapzen.helpers.RouteEngine;
import com.mapzen.model.ValhallaLocation;
import com.mapzen.tangram.LngLat;

import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * the {@link ViewModel} for the home Screen
 * <p>
 * Created by Ahmed Adel on 12/20/2016.
 */
class HomeViewModel extends ViewModel implements OnMapReadyCallback {

    private static final float ZOOM_VALUE = 14f;

    @Sync("current-location")
    final Property<Location> currentLocation = new Property<>();
    @Sync("navigating")
    final BooleanProperty navigating = new BooleanProperty();
    @Sync("job-orders-route")
    final Property<JobOrdersRoute> routes = new Property<>();
    @Sync("navigating-route")
    final Property<RouteEdges> navigationRoute = new Property<>();
    final Consumable<String> snackBarText = new Consumable<>();
    final BooleanProperty progressBarVisible = new BooleanProperty();
    MapzenMap mapzenMap;
    MapzenRouter router;
    RouteListenerImpl routeListener;
    RouteEngine engine;
    Subject<ValhallaLocation> locationChangedObservable;
    Subject<ValhallaLocation> routeChangedObservable;
    SpeakerBox speakerBox;
    private MapView mapView;
    private MapzenRouteDrawerObservable mapzenRouteDrawerObservable;
    private MapzenNavigationCommand mapzenNavigationCommand;


    @Override
    public void preInitialize() {
        currentLocation.onUpdate(updateEngineLocationIfNavigating());
        router = new MapzenRouter(this.getHostActivity(),
                AppResources.string(R.string.mapzen_api_key));
        router.setDriving();
        speakerBox = new SpeakerBox(this.getHostActivity());
        locationChangedObservable = PublishSubject.create();
        routeChangedObservable = PublishSubject.create();
        routeListener = new RouteListenerImpl(this);
        engine = new RouteEngine();
        engine.setListener(routeListener);
    }

    @NonNull
    private Command<Location, Void> updateEngineLocationIfNavigating() {
        return new Command<Location, Void>() {
            @Override
            public Void execute(Location location) {
                if (location != null && isNavigating()) {
                    updateEngineLocation();
                }
                return null;
            }

            private boolean isNavigating() {
                return navigating.isTrue()
                        && engine.getRoute() != null
                        && engine.getRoute().getStatus() != null
                        && !engine.getRoute().getStatus().equals(RouteEngine.RouteState.COMPLETE.ordinal());
            }
        };
    }

    @Executable(R.id.screen_host_container_id)
    void onInvalidate(View view) {
        try {
            String text = snackBarText.consume();
            new SnackbarGenerator(view).execute(text).show();
        } catch (CheckedReferenceClearedException e) {
            // do nothing
        }
    }

    @Initializer(R.id.screen_home_map_view)
    void initializeMapView(MapView mapView) {
        if (this.mapView == null) {
            this.mapView = mapView;
        }
        if (mapzenMap == null) {
            mapView.getMapAsync(this);
        }
    }

    @Executable(R.id.screen_home_map_view)
    void mapView(MapView mapView) {
        if (mapzenMap != null) {
            drawLocationOrRoute();
        }
    }

    private void drawLocationOrRoute() {
        if (!navigationRoute.isEmpty()) {
            mapzenNavigationCommand.execute(navigationRoute.get());
        } else if (routes.isEmpty()) {
            drawPinOnLocation();
        } else {
            mapzenRouteDrawerObservable.execute(routes.get().getRoute()).subscribe();
        }
    }

    private void updateEngineLocation() {
        ValhallaLocation location = new ValhallaLocation();
        location.setLatitude(currentLocation.get().getLatitude());
        location.setLongitude(currentLocation.get().getLongitude());
        engine.onLocationChanged(location);
    }

    @Executable(R.id.screen_home_main_progress_bar)
    void mainProgressBar(ProgressBar progressBar) {
        progressBar.setVisibility(progressBarVisible.isTrue() ? View.VISIBLE : View.GONE);
    }


    @Override
    public void clear() {
        engine.setListener(null);
        speakerBox.clear();
        super.clear();
    }

    @Executable(R.id.screen_home_floating_action_button)
    void onInvalidateFloatingActionButton(FloatingActionButton button) {
        if (navigating.isTrue()) {
            button.setImageResource(R.drawable.home_fab_finish);
        } else {
            button.setImageResource(R.drawable.home_fab_start_nav);
        }
    }

    @Override
    public void onMapReady(MapzenMap mapzenMap) {
        this.mapzenMap = mapzenMap;
        mapzenRouteDrawerObservable = new MapzenRouteDrawerObservable(mapzenMap);
        mapzenNavigationCommand = new MapzenNavigationCommand(this);
        drawPinOnLocation();
    }

    private void drawPinOnLocation() {
        if (!currentLocation.isEmpty()) {
            zoomOnLocation(currentLocation.get().getLongitude(),
                    currentLocation.get().getLatitude());
        }
        invalidateView(R.id.screen_home_main_progress_bar);
    }

    private void zoomOnLocation(double lng, double lat) {
        mapzenMap.clearDroppedPins();
        mapzenMap.drawDroppedPin(new LngLat(lng, lat));
        mapzenMap.getMapController().setZoom(ZOOM_VALUE);
        mapzenMap.getMapController().setPosition(new LngLat(lng, lat));
    }

    void clearRouteAndGoToLastPoint(ValhallaLocation location) {
        router.clearLocations();
        engine.setListener(null);
        mapzenMap.clearDroppedPins();
        mapzenMap.clearRouteLine();
        mapzenMap.clearRoutePins();
        mapzenMap.clearTransitRouteLine();
        mapzenMap.clearRouteLocationMarker();
        mapzenMap.drawDroppedPin(new LngLat(location.getLongitude(), location.getLatitude()));
    }

    void reCenter() {
        mapzenMap.setPosition(new LngLat(currentLocation.get().getLongitude()
                , currentLocation.get().getLatitude()));
    }

}
