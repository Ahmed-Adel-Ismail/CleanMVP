package com.base.presentation.locations.google;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;

import com.base.abstraction.commands.Command;
import com.base.abstraction.concurrency.Future;
import com.base.abstraction.interfaces.Clearable;
import com.base.abstraction.logs.Logger;
import com.base.presentation.exceptions.locations.ConnectionSuspendedException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.location.LocationRequest;

import io.reactivex.subjects.Subject;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * the implementer for {@link ConnectionCallbacks}
 * <p>
 * Created by Ahmed Adel on 12/22/2016.
 */
class GoogleCallbacks implements ConnectionCallbacks, Clearable {


    private static final long UPDATE_INTERVAL_MILLIS = 10 * 1000;
    private static final long FASTEST_INTERVAL_MILLIS = 2 * 1000;

    private Subject<Location> locationSubject;
    private GoogleApiClient googleApiClient;
    private Future<LocationRequest> future;

    GoogleCallbacks(Subject<Location> locationSubject, Command<LocationRequest, Void> onConnected) {
        this.locationSubject = locationSubject;
        future = new Future<LocationRequest>().onComplete(onConnected);
    }

    void setGoogleApiClient(GoogleApiClient googleApiClient) {
        this.googleApiClient = googleApiClient;
    }

    @Override
    @RequiresPermission(allOf = {ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION})
    public void onConnected(@Nullable Bundle bundle) {
        Logger.getInstance().info(getClass(), "onConnected()");
        if (googleApiClient != null) {
            future.setResult(createLocationRequest());
        } else {
            future.setResult(null);
            Logger.getInstance().info(getClass(), "onConnected() : null @ GoogleApiClient");
        }
    }

    @NonNull
    private LocationRequest createLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL_MILLIS);
        locationRequest.setFastestInterval(FASTEST_INTERVAL_MILLIS);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Logger.getInstance().error(getClass(), "onConnectionSuspended() : " + cause);
        if (locationSubject != null) {
            locationSubject.onError(new ConnectionSuspendedException(cause));
        }
    }

    @Override
    public void clear() {
        googleApiClient = null;
        locationSubject = null;
        if (future != null) {
            future.clear();
            future = null;
        }
    }
}
