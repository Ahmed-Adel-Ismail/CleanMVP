package com.base.presentation.exceptions.locations;

import com.base.abstraction.R;
import com.base.presentation.base.abstracts.features.AbstractActivity;

/**
 * a {@link RuntimeException} that is thrown if location permission is not granted
 * <p>
 * Created by Ahmed Adel on 12/20/2016.
 */
public class LocationPermissionRequiredException extends LocationTrackingException {

    @Override
    public Void execute(AbstractActivity abstractActivity) {
        abstractActivity.getManifestPermissions().openLocationPermissionDialog(
                R.integer.requestCodeLocationPermission);
        return null;
    }

    @Override
    public long getActorAddress() {
        return R.id.addressLocationPermissionRequiredException;
    }
}
