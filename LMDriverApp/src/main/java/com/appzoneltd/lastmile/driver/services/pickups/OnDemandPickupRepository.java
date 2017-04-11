package com.appzoneltd.lastmile.driver.services.pickups;

import com.appzoneltd.lastmile.driver.R;
import com.base.abstraction.annotations.interfaces.Address;
import com.base.cached.ServerMessage;
import com.base.presentation.annotations.interfaces.JsonRequest;
import com.base.presentation.annotations.interfaces.Requester;
import com.base.presentation.repos.base.Repository;
import com.base.usecases.annotations.Mock;
import com.base.usecases.requesters.server.ssl.AuthorizedHttpsRequester;

@Address(R.id.addressOnDemandPickupRepository)
@Requester(AuthorizedHttpsRequester.class)
class OnDemandPickupRepository extends Repository {

    @Mock(R.id.requestSubmitDriverAcceptedOnDemandPickup)
    @JsonRequest(R.id.requestSubmitDriverAcceptedOnDemandPickup)
    ServerMessage requestDriverAcceptedOnDemandPickup;

    @Mock(R.id.requestSubmitDriverRejectedOnDemandPickup)
    @JsonRequest(R.id.requestSubmitDriverRejectedOnDemandPickup)
    ServerMessage requestDriverRejectedOnDemandPickup;
}
