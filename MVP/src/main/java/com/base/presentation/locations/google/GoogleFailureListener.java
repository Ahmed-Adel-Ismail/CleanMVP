package com.base.presentation.locations.google;

import android.location.Location;
import android.support.annotation.NonNull;

import com.base.abstraction.interfaces.Clearable;
import com.base.abstraction.logs.Logger;
import com.base.presentation.exceptions.locations.ConnectionFailedException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

import io.reactivex.subjects.Subject;

/**
 * the implementation for {@link OnConnectionFailedListener}
 * <p>
 * Created by Ahmed Adel on 12/22/2016.
 */
class GoogleFailureListener implements
        OnConnectionFailedListener, Clearable {

    private Subject<Location> locationSubject;

    GoogleFailureListener(Subject<Location> locationSubject) {
        this.locationSubject = locationSubject;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Logger.getInstance().error(getClass(), "onConnectionFailed() : " + connectionResult);
        if (locationSubject != null) {
            locationSubject.onError(new ConnectionFailedException());
        }
    }

    @Override
    public void clear() {
        locationSubject = null;
    }
}
