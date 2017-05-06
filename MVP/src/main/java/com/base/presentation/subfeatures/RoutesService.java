package com.base.presentation.subfeatures;

import com.base.abstraction.commands.Command;
import com.base.abstraction.logs.Logger;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * responsible for rendering the rout between locations
 * <p>
 * Created by Wafaa on 12/21/2016.
 */

public class RoutesService implements RoutingListener {

    private Command<List<Route>, Void> onSuccess;
    private Routing routing;
    private boolean isRunning = false;

    public RoutesService(String key, Command<List<Route>, Void> onSuccess, LatLng... points) {
        this.onSuccess = onSuccess;
        routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(false)
                .waypoints(points)
                .key(key)
                .build();
    }

    public void start() {
        if (!isRunning) {
            isRunning = true;
            routing.execute();
        }
    }

    public void cancel() {
        routing.cancel(true);
    }

    @Override
    public void onRoutingFailure(RouteException e) {
        Logger.getInstance().info(getClass(), e.getMessage());
    }

    @Override
    public void onRoutingStart() {
        Logger.getInstance().info(getClass(), "Routing is started");
    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> routes, int i) {
        isRunning = false;
        if (onSuccess != null) {
            onSuccess.execute(routes);
        }
    }

    @Override
    public void onRoutingCancelled() {
        Logger.getInstance().info(getClass(), "Routing is cancelled");
    }
}
