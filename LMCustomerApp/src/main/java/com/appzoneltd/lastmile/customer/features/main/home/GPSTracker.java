package com.appzoneltd.lastmile.customer.features.main.home;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.appzoneltd.lastmile.customer.R;
import com.base.abstraction.events.Event;
import com.base.abstraction.events.Message;
import com.base.presentation.base.abstracts.features.AbstractActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.lang.ref.WeakReference;

public class GPSTracker implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {


    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */

    private final WeakReference<Context> contextWeakReference;
    private final WeakReference<AbstractActivity> abstractActivityWeakReference;
    private Location mLocation = null;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    public void setLocation(Location location) {
        this.mLocation = location;
    }

    public Location getLocation() {
        return this.mLocation;
    }

    public GPSTracker(Context context, AbstractActivity abstractActivity) {
        this.contextWeakReference = new WeakReference<>(context);
        this.abstractActivityWeakReference = new WeakReference<>(abstractActivity);
        buildGoogleApiClient();
        createLocationRequest();
        mGoogleApiClient.connect();
    }


    private synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(contextWeakReference.get())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(contextWeakReference.get(), android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(contextWeakReference.get(), android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLocation == null) {
            startLocationUpdates();
        } else {
            sentFindLocationEvent(mLocation);
        }
        setLocation(mLocation);
    }

    private void sentFindLocationEvent(Location location) {
        Message message = new Message.Builder().content(location).build();
        Event event = new Event.Builder(R.id.onLocationFound).message(message).build();
        abstractActivityWeakReference.get().getMailbox().execute(event);
    }

    private void startLocationUpdates() {
        if (mGoogleApiClient.isConnected()) {
            if (ActivityCompat.checkSelfPermission(contextWeakReference.get(), android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(contextWeakReference.get(), android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                return;
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
        setLocation(location);
    }

    void stopLocationUpdates() {
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
        }
    }

}