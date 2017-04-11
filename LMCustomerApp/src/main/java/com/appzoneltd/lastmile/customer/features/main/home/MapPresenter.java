package com.appzoneltd.lastmile.customer.features.main.home;

import android.location.Location;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.subfeatures.MapView.InvalidateMapViewLifeCycle;
import com.appzoneltd.lastmile.customer.subfeatures.MapView.MapViewLifeCycle;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.abstraction.events.Event;
import com.base.abstraction.events.Message;
import com.base.presentation.base.abstracts.features.ViewBinder;
import com.base.presentation.base.presentation.Presenter;
import com.base.presentation.receivers.NetworkStateReceiver;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * @deprecated old class
 * <p/>
 * Created by Wafaa on 9/7/2016.
 */
class MapPresenter extends Presenter implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener {

    private static final float ZOOM_VALUE = 14f;
    private int mapCycleStatus = MapViewLifeCycle.ONCREATE.status;
    private GoogleMap map;
    private ViewGroup layoutNetworkError;
    private ViewGroup mapLayout;
    private ViewGroup layoutLoadingMap;
    private ViewGroup mapOuterLayout;
    private GPSTracker tracker;
    private LatLng lastLatLng = null;
    private InvalidateMapViewLifeCycle invalidateMapViewLifeCycle;


    @SuppressWarnings("deprecation")
    public MapPresenter(ViewBinder viewBinder, MapView mapView,
                        ViewGroup layoutNetworkError, ViewGroup mapLayout, ViewGroup mapOuterLayout, ViewGroup layoutLoadingMap) {
        super(viewBinder);
        mapView.getMapAsync(this);
        invalidateMapViewLifeCycle = new InvalidateMapViewLifeCycle(mapView);
        invalidateMapViewLifeCycle.execute(mapCycleStatus);
        MapsInitializer.initialize(getFeature().getHostActivity());
        tracker = new GPSTracker(getHostActivity(), getHostActivity());
        this.layoutNetworkError = layoutNetworkError;
        this.mapLayout = mapLayout;
        this.layoutLoadingMap = layoutLoadingMap;
        this.mapOuterLayout = mapOuterLayout;
    }

    @NonNull
    protected CommandExecutor<Long, View> createOnClickCommandExecutor() {
        CommandExecutor<Long, View> commandExecutor = new CommandExecutor<>();
        Command<View, Void> command = createMyLocationOnClickCommand();
        commandExecutor.put((long) R.id.floating_btn_locate_me, command);
        return commandExecutor;
    }

    private Command<View, Void> createMyLocationOnClickCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                LatLng latLng = getMyLocation();
                lastLatLng = latLng;
                goToSelectedLocation(latLng);
                notifyOnMyLocationEvent(latLng);
                return null;
            }
        };
    }


    private void putMarkerOnMapAndZoom(LatLng location) {
        if (location != null) {
            map.clear();
            map.addMarker(new MarkerOptions().position(location).
                    icon(BitmapDescriptorFactory.fromResource(R.drawable.pin)));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(location, ZOOM_VALUE));
        }
    }

    private LatLng getMyLocation() {
        Location myLocation;
        LatLng latLng = null;
        tracker.onConnected(null);
        myLocation = tracker.getLocation();
        if (myLocation != null) {
            latLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
        }
        return latLng;
    }

    private void goToMyLocationForFirstTime() {
        LatLng latLng = getMyLocation();
        if (latLng != null) {
            putMarkerOnMapAndZoom(latLng);
        }
    }


    private void goToSelectedLocation(LatLng latLng) {
        if (latLng != null) {
            putMarkerOnMapAndZoom(latLng);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (map == null) {
            map = googleMap;
            map.setOnMapClickListener(this);
            map.setOnMarkerClickListener(this);
        }

        map.getUiSettings().setMapToolbarEnabled(false);
        map.getUiSettings().setZoomControlsEnabled(false);
        map.clear();

        boolean permissionAllowed = getManifestPermissions().isLocationPermissionAllowed();
        setMapAndLoadingVisibility(permissionAllowed);
        if (!permissionAllowed) {
            getManifestPermissions().openLocationPermissionDialog(R.integer.requestCodeLocationPermission);
        } else {
            setMapAndErrorVisibility(getSystemServices().isNetworkConnected());
        }
    }

    private void setMapAndLoadingVisibility(boolean isMapLoaded) {
        if (isMapLoaded) {
            mapLayout.setVisibility(View.VISIBLE);
            mapOuterLayout.setVisibility(View.VISIBLE);
            layoutLoadingMap.setVisibility(View.GONE);
        } else {
            mapLayout.setVisibility(View.GONE);
            mapOuterLayout.setVisibility(View.GONE);
            layoutLoadingMap.setVisibility(View.VISIBLE);
        }
    }

    private void notifyOnMyLocationEvent(LatLng location) {
        Message message = new Message.Builder().content(location).build();
        Event.Builder event = new Event.Builder((long) R.id.Map_onLocationSelected);
        event.message(message);
        notifyObservers(event.build());
    }


    @Override
    public void onMapClick(LatLng latLng) {
        lastLatLng = latLng;
        boolean networkState = getSystemServices().isNetworkConnected();
        if (!networkState) {
            setMapAndErrorVisibility(false);
            return;
        }
        goToSelectedLocation(latLng);
        notifyOnMyLocationEvent(latLng);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        notifyOnMyLocationEvent(marker.getPosition());
        return true;
    }

    @Override
    protected CommandExecutor<Long, Message> createCommandExecutor() {
        CommandExecutor<Long, Message> commandExecutor = new CommandExecutor<>();
        Command<Message, Void> command = createOnSearchPlacesSelected();
        commandExecutor.put((long) R.id.HomeBinder_onSearchPlaceSelected, command);
        command = createOnLocationPermissionChanged();
        commandExecutor.put((long) R.id.HomeBinder_onLocationPermissionChanged, command);
        command = createOnGPSStateChanged();
        commandExecutor.put((long) R.id.onGPSStateChanged, command);
        command = createOnNetworkStateChangedCommand();
        commandExecutor.put((long) R.id.onNetworkStateChanged, command);
        command = createOnLocationFound();
        commandExecutor.put((long) R.id.onLocationFound, command);
        command = createOnPauseCommand();
        commandExecutor.put((long) R.id.onPause, command);
        command = createOnCreateCommand();
        commandExecutor.put((long) R.id.onCreate, command);
        command = createOnResumeCommand();
        commandExecutor.put((long) R.id.onResume, command);
        command = createOnStartCommand();
        commandExecutor.put((long) R.id.onStart, command);
        command = createOnStopCommand();
        commandExecutor.put((long) R.id.onStop, command);
        command = createOnDestroy();
        commandExecutor.put((long) R.id.onDestroy, command);
        return commandExecutor;
    }

    private Command<Message, Void> createOnCreateCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message p) {
                if (invalidateMapViewLifeCycle != null) {
                    invalidateMapViewLifeCycle.execute(MapViewLifeCycle.ONCREATE.status);
                }
                return null;
            }
        };
    }

    private Command<Message, Void> createOnResumeCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message p) {
                invalidateMapViewLifeCycle.execute(MapViewLifeCycle.ONRESUME.status);
                return null;
            }
        };
    }

    private Command<Message, Void> createOnPauseCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message p) {
                invalidateMapViewLifeCycle.execute(MapViewLifeCycle.ONPAUSE.status);
                return null;
            }

        };
    }

    private Command<Message, Void> createOnStartCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message p) {
                invalidateMapViewLifeCycle.execute(MapViewLifeCycle.ONSTART.status);
                return null;
            }
        };
    }

    private Command<Message, Void> createOnStopCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message p) {
                invalidateMapViewLifeCycle.execute(MapViewLifeCycle.ONSTOP.status);
                return null;
            }
        };
    }

    private Command<Message, Void> createOnDestroy() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message p) {
                invalidateMapViewLifeCycle.execute(MapViewLifeCycle.ONDESTROY.status);
                return null;
            }
        };
    }


    private Command<Message, Void> createOnSearchPlacesSelected() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                LatLng latLng = message.getContent();
                lastLatLng = latLng;
                goToSelectedLocation(latLng);
                notifyOnMyLocationEvent(latLng);
                return null;
            }
        };
    }

    private Command<Message, Void> createOnLocationPermissionChanged() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message p) {
                setMapAndLoadingVisibility(true);
                boolean permissionStatus = p.getContent();
                if (permissionStatus) {
                    goToMyLocationForFirstTime();
                }
                return null;
            }
        };
    }


    private Command<Message, Void> createOnGPSStateChanged() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                setMapAndLoadingVisibility(true);
                boolean gpsState = message.getContent();
                if (gpsState) {
                    locateMeAndPutMarker();
                }
                return null;
            }

        };
    }

    private void locateMeAndPutMarker() {
        if (lastLatLng == null) {
            lastLatLng = getMyLocation();
        }
        goToSelectedLocation(lastLatLng);
    }

    private Command<Message, Void> createOnNetworkStateChangedCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message p) {
                NetworkStateReceiver.ConnectedNetworks connectedNetworks = p.getContent();
                setMapAndErrorVisibility(connectedNetworks.any);
                goToSelectedLocation(lastLatLng);
                return null;
            }
        };
    }


    private void setMapAndErrorVisibility(boolean networkState) {
        if (networkState) {
            mapLayout.setVisibility(View.VISIBLE);
            layoutNetworkError.setVisibility(View.GONE);
        } else {
            mapLayout.setVisibility(View.GONE);
            layoutNetworkError.setVisibility(View.VISIBLE);
        }
    }

    private Command<Message, Void> createOnLocationFound() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                Location location = message.getContent();
                if (location != null && map != null && getSystemServices().isGPSProviderEnabled()) {
                    goToSelectedLocation(new LatLng(location.getLatitude(), location.getLongitude()));
                }
                return null;
            }
        };
    }


    @Override
    public void onDestroy() {
        map = null;
        tracker.stopLocationUpdates();
        tracker = null;
    }
}
