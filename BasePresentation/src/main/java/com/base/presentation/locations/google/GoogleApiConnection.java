package com.base.presentation.locations.google;

import android.content.Context;
import android.location.Location;
import android.support.annotation.RequiresPermission;

import com.base.abstraction.commands.Command;
import com.base.abstraction.interfaces.Clearable;
import com.base.abstraction.logs.Logger;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import io.reactivex.subjects.Subject;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * the handler for starting/stopping connection to locations {@link GoogleApiClient}, every call to
 * {@link #execute(Context)} will stop the current connection and will start a new one for the
 * same {@link Subject}
 * <p>
 * Created by Ahmed Adel on 12/22/2016.
 */
class GoogleApiConnection implements
        Command<Context, GoogleApiConnection>,
        Clearable {


    private static final long LAST_VALID_LOCATION_MILLIS = 5 * 60 * 1000;
    private Subject<Location> locationSubject;
    private GoogleApiClient googleApiClient;
    private GoogleLocationListener locationListener;
    private GoogleFailureListener failureListener;
    private GoogleCallbacks callbacks;


    GoogleApiConnection(Subject<Location> locationSubject) {
        this.locationSubject = locationSubject;
        Logger.getInstance().error(getClass(), "<init> : " + locationSubject);
    }

    /**
     * start the connection with the passed client
     *
     * @param context the {@link Context} that will establish connection with
     *                {@link GoogleApiClient}
     * @return {@code this} instance for chaining
     */
    @Override
    public GoogleApiConnection execute(Context context) {
        Logger.getInstance().error(getClass(), "execute(" + context.getClass().getSimpleName() + ") : start");
        clear();
        this.locationListener = new GoogleLocationListener(locationSubject);
        this.failureListener = new GoogleFailureListener(locationSubject);
        this.callbacks = new GoogleCallbacks(locationSubject, onConnected());
        this.googleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(callbacks)
                .addOnConnectionFailedListener(failureListener)
                .addApi(LocationServices.API)
                .build();

        this.callbacks.setGoogleApiClient(googleApiClient);
        this.googleApiClient.connect();
        Logger.getInstance().error(getClass(), "googleApiClient.connect()");
        Logger.getInstance().error(getClass(), "execute() : end");
        return this;
    }

    private Command<LocationRequest, Void> onConnected() {
        return new Command<LocationRequest, Void>() {
            @Override
            @RequiresPermission(allOf = {ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION})
            public Void execute(LocationRequest locationRequest) {
                Logger.getInstance().error(getClass(), "onConnected()");
                if (locationRequest != null) {
                    start(locationRequest);
                }
                return null;
            }
        };
    }

    @RequiresPermission(allOf = {ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION})
    private void start(LocationRequest locationRequest) {
        Logger.getInstance().error(getClass(), "start()");
        updateLocationSubjectWithLastValidLocation();
        LocationServices.FusedLocationApi.requestLocationUpdates(
                googleApiClient,
                locationRequest,
                locationListener);
    }

    @RequiresPermission(allOf = {ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION})
    private void updateLocationSubjectWithLastValidLocation() {
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (lastLocation != null) {
            emitLastLocationIfValid(lastLocation);
        }

    }

    private void emitLastLocationIfValid(Location lastLocation) {
        if (System.currentTimeMillis() - lastLocation.getTime() <= LAST_VALID_LOCATION_MILLIS) {
            Logger.getInstance().error(getClass(), "emitting Last valid Location");
            locationSubject.onNext(lastLocation);
        }
    }


    @Override
    public void clear() {
        Logger.getInstance().error(getClass(), "clear() : started");
        stop();
        clearLocationListener();
        clearFailureListener();
        clearCallbacks();
        googleApiClient = null;
        Logger.getInstance().error(getClass(), "clear() : completed");
    }


    private void stop() {
        Logger.getInstance().error(getClass(), "stop()");
        if (googleApiClient != null && googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    googleApiClient,
                    locationListener);

            googleApiClient.disconnect();
            Logger.getInstance().error(getClass(), "googleApiClient.disconnect()");
        }
    }

    private void clearLocationListener() {
        if (locationListener != null) {
            locationListener.clear();
            locationListener = null;
        }
    }


    private void clearFailureListener() {
        if (failureListener != null) {
            failureListener.clear();
            failureListener = null;
        }
    }

    private void clearCallbacks() {
        if (callbacks != null) {
            callbacks.clear();
            callbacks = null;
        }
    }


}
