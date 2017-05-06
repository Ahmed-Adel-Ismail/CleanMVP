package com.base.presentation.locations;

import android.content.Context;
import android.support.annotation.NonNull;

import com.base.abstraction.commands.Command;
import com.base.abstraction.exceptions.CheckedReferenceClearedException;
import com.base.abstraction.interfaces.Clearable;
import com.base.presentation.exceptions.locations.FineLocationDisabledException;
import com.base.presentation.exceptions.locations.LocationPermissionRequiredException;
import com.base.presentation.locations.google.GoogleLocationConnection;
import com.base.presentation.system.ManifestPermissionsChecker;
import com.base.presentation.system.SystemServices;

/**
 * a class that initializes a {@link LocationConnection} for tracking location
 * of the device, you should invoke {@link #execute(Context)} to retrieve a this
 * {@link LocationConnection},
 * <p>
 * <p>
 * Created by Ahmed Adel on 12/20/2016.
 */
public class LocationTracking implements Command<Context, LocationConnection> {

    /**
     * initialize a {@link LocationConnection} to use it in tracking location
     *
     * @param context the {@link Context} that will be used in
     *                checking for permissions, GPS state and similar stuff
     * @return the {@link LocationConnection} to be used
     * @throws LocationPermissionRequiredException if no location permission was grated
     * @throws FineLocationDisabledException       if GPS is disabled/off
     * @throws CheckedReferenceClearedException    if the host Context is no more alive
     */
    @Override
    public LocationConnection execute(@NonNull Context context)
            throws
            LocationPermissionRequiredException,
            FineLocationDisabledException,
            CheckedReferenceClearedException {

        ManifestPermissionsChecker permissionsChecker = new ManifestPermissionsChecker(context);
        SystemServices systemServices = new SystemServices(context);
        if (!permissionsChecker.isLocationPermissionAllowed()) {
            clear(permissionsChecker, systemServices);
            throw new LocationPermissionRequiredException();
        } else if (!systemServices.isGPSProviderEnabled()) {
            clear(permissionsChecker, systemServices);
            throw new FineLocationDisabledException();
        } else {
            clear(permissionsChecker, systemServices);
            return new GoogleLocationConnection(context);
        }
    }

    private void clear(@NonNull Clearable... clearableObjects) {
        for (Clearable clearable : clearableObjects) {
            clearable.clear();
        }
    }


}
