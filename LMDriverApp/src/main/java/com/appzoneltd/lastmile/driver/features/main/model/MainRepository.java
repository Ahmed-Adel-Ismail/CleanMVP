package com.appzoneltd.lastmile.driver.features.main.model;

import com.appzoneltd.lastmile.driver.R;
import com.base.abstraction.annotations.interfaces.Address;
import com.base.cached.ServerMessage;
import com.base.presentation.annotations.interfaces.JsonRequest;
import com.base.presentation.annotations.interfaces.Requester;
import com.base.presentation.repos.base.Repository;
import com.base.usecases.annotations.Mock;
import com.base.usecases.requesters.server.ssl.AuthorizedHttpsRequester;
import com.entities.cached.RouteEdges;
import com.entities.cached.orders.JobOrdersGroup;
import com.entities.requesters.PickupProcessId;

/**
 * the {@link Repository} for the main feature of the application
 * <p>
 * Created by Ahmed Adel on 12/1/2016.
 */
@Address(R.id.addressMainRepository)
@Requester(AuthorizedHttpsRequester.class)
class MainRepository extends Repository {

    @Mock(R.id.requestSubmitDriverAcceptedOnDemandPickup)
    @JsonRequest(R.id.requestSubmitDriverAcceptedOnDemandPickup)
    ServerMessage requestDriverAcceptedOnDemandPickup;

    @Mock(R.id.requestSubmitDriverRejectedOnDemandPickup)
    @JsonRequest(R.id.requestSubmitDriverRejectedOnDemandPickup)
    ServerMessage requestDriverRejectedOnDemandPickup;

    @Mock(R.id.requestDriverStartNavigation)
    @JsonRequest(R.id.requestDriverStartNavigation)
    PickupProcessId requestDriverStartNavigation;

    @Mock(R.id.requestRegisterFirebaseToken)
    @JsonRequest(value = R.id.requestRegisterFirebaseToken, concurrent = true)
    ServerMessage requestRegisterFirebaseToken;

    @Mock(R.id.requestRouteEdges)
    @JsonRequest(R.id.requestRouteEdges)
    RouteEdges routeEdges;

    @Mock(R.id.requestJobOrdersRoute)
    @JsonRequest(R.id.requestJobOrdersRoute)
    JobOrdersGroup jobOrdersRoute;

}
