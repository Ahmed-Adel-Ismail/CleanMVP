package com.base.presentation.system;

import android.Manifest;
import android.app.Activity;
import android.support.v4.app.ActivityCompat;

import com.base.abstraction.system.AppResources;
import com.base.presentation.base.abstracts.features.Feature;

/**
 * A Class to check for taking Permissions in Manifest, it extends
 * {@link ManifestPermissionsChecker}
 * <p/>
 * Created by Wafaa on 9/19/2016.
 */
public class ManifestPermissions extends ManifestPermissionsChecker {

    private Activity activity;

    public ManifestPermissions(Feature feature) {
        super(feature.getHostActivity());
        this.activity = feature.getHostActivity();
    }


    /**
     * open the location permission dialog and wait for the given request-code
     *
     * @param requestCodeResource the request code resource value in {@code R.integer} class,
     *                            like passing {@code R.integer.requestCodeLocationPermission}
     */
    public void openLocationPermissionDialog(int requestCodeResource) {
        if (activity == null) {
            return;
        }
        ActivityCompat.requestPermissions(activity, new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION},
                AppResources.integer(requestCodeResource));
    }

    public void openCameraPermissionDialog(int requestId) {
        if (activity == null) {
            return;
        }
        ActivityCompat.requestPermissions(activity, new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                AppResources.integer(requestId));
    }

    public void openCallPermissionDialog(int requestId) {
        if (activity == null) {
            return;
        }
        ActivityCompat.requestPermissions(activity, new String[]{
                        Manifest.permission.CALL_PHONE},
                AppResources.integer(requestId));
    }

    @Override
    public void clear() {
        super.clear();
        activity = null;
    }
}
