package com.appzoneltd.lastmile.driver.features.main.model;

import android.location.Location;
import android.support.annotation.NonNull;

import com.appzoneltd.lastmile.driver.R;
import com.base.abstraction.annotations.interfaces.Retry;
import com.base.abstraction.annotations.interfaces.Save;
import com.base.abstraction.commands.Command;
import com.base.cached.ServerMessage;
import com.base.presentation.annotations.interfaces.JsonRequest;
import com.base.presentation.annotations.interfaces.Repository;
import com.base.presentation.annotations.interfaces.Sync;
import com.base.presentation.models.Model;
import com.base.presentation.references.BooleanProperty;
import com.base.presentation.references.Entity;
import com.base.presentation.references.Property;
import com.base.usecases.events.ResponseMessage;
import com.entities.cached.RouteEdges;
import com.entities.cached.orders.JobOrdersRoute;
import com.entities.requesters.PickupProcessId;

/**
 * the model of the main feature of the application
 * <p>
 * Created by Ahmed Adel on 11/21/2016.
 */
@Repository(MainRepository.class)
public class MainModel extends Model {

    @Sync("current-location")
    public final Property<Location> currentLocation = new Property<>();

    @Sync("navigating")
    public final BooleanProperty navigating = new BooleanProperty();

    @JsonRequest(R.id.requestSubmitDriverAcceptedOnDemandPickup)
    public final Entity<ServerMessage> driverPickupAccept = new Entity<>();

    @JsonRequest(R.id.requestSubmitDriverRejectedOnDemandPickup)
    public final Entity<ServerMessage> driverPickupRejection = new Entity<>();

    @Save(R.string.INTENT_KEY_PICKUP_PROCESS_ID)
    @JsonRequest(R.id.requestDriverStartNavigation)
    public final Entity<PickupProcessId> pickupProcessId = new Entity<>();

    @Retry
    @Sync("job-orders-route")
    @JsonRequest(R.id.requestJobOrdersRoute)
    public final Entity<JobOrdersRoute> jobOrdersRoute = new Entity<>();

    @Sync("navigating-route")
    @JsonRequest(R.id.requestRouteEdges)
    public final Entity<RouteEdges> navigationRoute = new Entity<>();


    public MainModel() {
        pickupProcessId.onComplete(requestNavigationRoute());
        navigationRoute.onComplete(updateNavigationRouteSource());
        jobOrdersRoute.onNext(notifyIfNotNavigating());
    }

    @NonNull
    private Command<ResponseMessage, Boolean> notifyIfNotNavigating() {
        return new Command<ResponseMessage, Boolean>() {
            @Override
            public Boolean execute(ResponseMessage responseMessage) {
                return !navigating.isTrue();
            }
        };
    }


    @NonNull
    private Command<ResponseMessage, Void> updateNavigationRouteSource() {
        return new Command<ResponseMessage, Void>() {

            @Override
            public Void execute(ResponseMessage p) {
                if (p.isSuccessful()) {
                    navigationRoute.get().setSourceLng(currentLocation.get().getLongitude());
                    navigationRoute.get().setSourceLat(currentLocation.get().getLatitude());
                }
                return null;
            }
        };
    }

    private Command<ResponseMessage, Void> requestNavigationRoute() {
        return new Command<ResponseMessage, Void>() {
            @Override
            public Void execute(ResponseMessage p) {
                if (p.isSuccessful()) {
                    navigationRoute.request();
                }
                return null;
            }
        };
    }

}
