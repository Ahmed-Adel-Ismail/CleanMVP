package com.base.presentation.system;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.CallSuper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.base.abstraction.interfaces.Clearable;

/**
 * a class to check permissions in manifest
 * <p>
 * Created by Ahmed Adel on 1/18/2017.
 */
public class ManifestPermissionsChecker implements Clearable {

    private Context context;

    public ManifestPermissionsChecker(Context context) {
        this.context = context;
    }

    public boolean isLocationPermissionAllowed() {
        return context != null
                && !(ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED);
    }

    public boolean isTakeAndCaptureImagePermissionAllowed() {
        return context != null
                && !(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED);
    }

    public boolean isCallPermissionAllowed() {
        return context != null
                && (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    @CallSuper
    public void clear() {
        context = null;
    }
}
