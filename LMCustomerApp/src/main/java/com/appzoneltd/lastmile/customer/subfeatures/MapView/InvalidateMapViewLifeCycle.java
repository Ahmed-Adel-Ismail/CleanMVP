package com.appzoneltd.lastmile.customer.subfeatures.MapView;

import com.base.abstraction.commands.Command;
import com.google.android.gms.maps.MapView;

/**
 * Created by Wafaa on 12/1/2016.
 */

public class InvalidateMapViewLifeCycle implements Command<Integer, Void> {

    private MapView mapView;

    public InvalidateMapViewLifeCycle(MapView mapView) {
        this.mapView = mapView;
    }

    @Override
    public Void execute(Integer status) {
        if (MapViewLifeCycle.ONCREATE.status == status) {
            mapView.onCreate(null);
        } else if (MapViewLifeCycle.ONSTART.status == status) {
            mapView.onStart();
        } else if (MapViewLifeCycle.ONRESUME.status == status) {
            mapView.onResume();
        } else if (MapViewLifeCycle.ONPAUSE.status == status) {
            mapView.onPause();
        } else if (MapViewLifeCycle.ONSTOP.status == status) {
            mapView.onStop();
        } else if (MapViewLifeCycle.ONDESTROY.status == status) {
            mapView.onDestroy();
        } else if (MapViewLifeCycle.ONSAVEINSTANTSTATE.status == status) {
            mapView.onSaveInstanceState(null);
        } else if (MapViewLifeCycle.ONLOWMEMOTY.status == status) {
            mapView.onLowMemory();
        }
        return null;
    }
}
