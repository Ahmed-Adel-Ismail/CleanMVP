package com.base.presentation.locations.google;

import android.location.Location;

import com.base.abstraction.interfaces.Clearable;
import com.base.abstraction.logs.Logger;
import com.google.android.gms.location.LocationListener;

import io.reactivex.subjects.Subject;

/**
 * the implementer of {@link LocationListener} interface
 * <p>
 * Created by Ahmed Adel on 12/22/2016.
 */
class GoogleLocationListener implements LocationListener, Clearable {

    private Subject<Location> locationSubject;

    GoogleLocationListener(Subject<Location> locationSubject) {
        this.locationSubject = locationSubject;
    }

    @Override
    public void onLocationChanged(Location location) {
        logLocation(location);
        if (locationSubject != null) {
            locationSubject.onNext(location);
        } else {
            lonNullSubject();
        }
    }


    private void logLocation(Location location) {
        Double lat = null;
        Double lng = null;
        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
        }
        Logger.getInstance().info(getClass(), "onLocationChanged() : {" + lat + "," + lng + "}");
    }

    private void lonNullSubject() {
        Logger.getInstance().error(getClass(), "no Location Subject to receive update, " +
                "maybe clear() was called but the location tracking is still active !!");
    }

    @Override
    public void clear() {
        locationSubject = null;
    }


}
