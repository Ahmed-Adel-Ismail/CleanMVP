package com.appzoneltd.lastmile.customer.features.tracking;

import android.support.annotation.NonNull;
import android.view.View;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.features.tracking.model.TrackingModel;
import com.appzoneltd.lastmile.customer.subfeatures.MapView.InvalidateMapViewLifeCycle;
import com.appzoneltd.lastmile.customer.subfeatures.MapView.MapViewLifeCycle;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.abstraction.system.AppResources;
import com.base.presentation.annotations.interfaces.Sync;
import com.base.presentation.base.abstracts.features.Feature;
import com.base.presentation.base.abstracts.features.ViewBinder;
import com.base.presentation.base.presentation.ViewModel;
import com.directions.route.Route;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

/**
 * Created by Wafaa on 11/13/2016.
 */

class TrackingFragmentViewModel extends ViewModel implements
        OnMapReadyCallback {

    @Sync("destination")
    LatLng destinationLatLng;
    @Sync("source")
    LatLng pickupLocation;
    protected List<Route> routes;
    boolean showProgress;
    private static final float ZOOM_VALUE = 14f;
    private int[] radarImages =
            new int[]{R.drawable.radar_1
                    , R.drawable.radar_2
                    , R.drawable.radar_3
                    , R.drawable.radar_4};
    private GoogleMap map;
    private int mapCycleStatus = MapViewLifeCycle.ONCREATE.status;

    TrackingFragmentViewModel(ViewBinder viewBinder) {
        super(viewBinder);
    }

    @Override
    public void preInitialize() {
        super.preInitialize();
    }

    @NonNull
    @Override
    protected CommandExecutor<Long, View> createInvalidateCommands() {
        CommandExecutor<Long, View> commandExecutor = new CommandExecutor<>();
        Command<View, Void> command = createOnMapLayoutInvalidateCommand();
        commandExecutor.put((long) R.id.driver_map_view, command);
        command = createOnDriverLayoutCommand();
        commandExecutor.put((long) R.id.driver_layout, command);
        command = invalidateProgress();
        commandExecutor.put((long) R.id.fragment_tracking_loading, command);
        return commandExecutor;
    }

    private Command<View, Void> createOnMapLayoutInvalidateCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                MapView driverMap = (MapView) view;
                new InvalidateMapViewLifeCycle(driverMap).execute(mapCycleStatus);
                driverMap.getMapAsync(TrackingFragmentViewModel.this);
                MapsInitializer.initialize(getFeature().getHostActivity());
                return null;
            }
        };
    }

    private Command<View, Void> createOnDriverLayoutCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                Feature<TrackingModel> feature = getFeature();
                if (feature.getModel().payloadActiveVehicleDetails != null) {
                    clearMap();
                    view.setVisibility(View.VISIBLE);
                } else {
                    view.setVisibility(View.GONE);
                }
                return null;
            }

        };
    }

    @NonNull
    private Command<View, Void> invalidateProgress() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                if (showProgress) {
                    view.setVisibility(View.VISIBLE);
                } else {
                    view.setVisibility(View.GONE);
                }
                return null;
            }
        };
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (map == null) {
            map = googleMap;
        }
        map.getUiSettings().setMapToolbarEnabled(false);
        map.getUiSettings().setZoomControlsEnabled(false);
        map.clear();
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(pickupLocation, ZOOM_VALUE));
    }

    void drawRadar(int i) {
        map.clear();
        MarkerOptions markerOptions = new MarkerOptions()
                .position(pickupLocation)
                .icon(BitmapDescriptorFactory.fromResource(radarImages[i]))
                .anchor(0.5f, 0.5f);
        map.addMarker(markerOptions);
    }

    void drawRoute(LatLng destination) {
        map.clear();
        map.addMarker(new MarkerOptions().position(destination).
                icon(BitmapDescriptorFactory.fromResource(R.drawable.pin)));
        map.addMarker(new MarkerOptions().position(pickupLocation).
                icon(BitmapDescriptorFactory.fromResource(R.drawable.pin)));
        if (createPolylineOptions() != null) {
            map.addPolyline(createPolylineOptions());
        }
    }

    private PolylineOptions createPolylineOptions() {
        if (routes != null) {
            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(AppResources.color(R.color.colorAccent));
            polyOptions.width(5);
            polyOptions.addAll(routes.get(0).getPoints());
            return polyOptions;
        }
        return null;
    }

    int getRadarImagesLength() {
        return radarImages.length;
    }

    void setMapCycleStatus(int mapCycleStatus) {
        this.mapCycleStatus = mapCycleStatus;
    }


    void clearMap() {
        if (map != null)
            map.clear();
    }

    @Override
    public void onDestroy() {
        map = null;
    }
}
