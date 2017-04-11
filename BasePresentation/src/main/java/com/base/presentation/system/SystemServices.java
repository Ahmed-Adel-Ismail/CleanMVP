package com.base.presentation.system;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.base.abstraction.interfaces.Clearable;

import static android.content.Context.POWER_SERVICE;

/**
 * A Class to Access System Services provided by Android platform
 * <p/>
 * Created by Ahmed Adel on 9/14/2016.
 */
public class SystemServices implements Clearable {

    private Context context;

    public SystemServices(Context context) {
        this.context = context;
    }

    /**
     * check if the GPS provider enabled or not
     *
     * @return {@code true} if the provider is enabled, else {@code false}
     */
    public boolean isGPSProviderEnabled() {
        if (context == null) {
            return false;
        }
        LocationManager locationManager;
        locationManager = (LocationManager) context.getSystemService(Activity.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public boolean isNetworkConnected() {
        return isMobileNetworkConnected() || isWifiConnected();
    }

    public boolean isWifiConnected() {
        return isNetworkConnected(ConnectivityManager.TYPE_WIFI);
    }

    public boolean isMobileNetworkConnected() {
        return isNetworkConnected(ConnectivityManager.TYPE_MOBILE);
    }

    private boolean isNetworkConnected(int networkType) {
        if (context == null) {
            return false;
        }
        ConnectivityManager connMgr = getConnectionManager(context);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return isConnected(networkInfo, networkType);
    }

    private ConnectivityManager getConnectionManager(Context activity) {
        return (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    private boolean isConnected(NetworkInfo networkInfo, int networkType) {
        return networkInfo != null
                && networkInfo.getType() == networkType
                && networkInfo.isConnected();
    }

    public void hideKeyboard() {
        View view;
        if (context != null && context instanceof Activity &&
                (view = ((Activity) context).getCurrentFocus()) != null) {
            doHideKeyboard(view);
        }
    }

    private void doHideKeyboard(@NonNull View view) {
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public PowerManager.WakeLock getNewWakeLock(String tag) {
        PowerManager powerManager = (PowerManager) context.getSystemService(POWER_SERVICE);
        return powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, tag);
    }

    @Override
    public void clear() {
        context = null;
    }
}
