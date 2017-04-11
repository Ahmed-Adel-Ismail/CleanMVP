package com.appzoneltd.lastmile.driver.features.main.model;

import com.appzoneltd.lastmile.driver.R;
import com.base.abstraction.annotations.interfaces.Address;
import com.base.cached.ServerMessage;
import com.base.presentation.annotations.interfaces.JsonRequest;
import com.base.presentation.repos.base.Repository;
import com.base.presentation.annotations.interfaces.Requester;
import com.base.usecases.requesters.server.ssl.AuthorizedHttpsRequester;

@Address(R.id.addressMainLocationsRepository)
@Requester(AuthorizedHttpsRequester.class)
class LocationsRepository extends Repository {

    @JsonRequest(value = R.id.requestOpenDriverLocationSocket, concurrent = true)
    ServerMessage requestSubmitDriverLocation;


}
