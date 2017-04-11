package com.appzoneltd.lastmile.driver.features.pickup.model;

import com.appzoneltd.lastmile.driver.R;
import com.base.abstraction.annotations.interfaces.Address;
import com.base.presentation.annotations.interfaces.JsonRequest;
import com.base.presentation.repos.base.Repository;
import com.base.presentation.annotations.interfaces.Requester;
import com.base.usecases.annotations.Mock;
import com.base.usecases.requesters.server.ssl.AuthorizedGetHttpsRequester;
import com.base.cached.ServerImage;

@Address(R.id.addressPickupProcessGetMethodRepository)
@Requester(AuthorizedGetHttpsRequester.class)
class GetMethodRepository extends Repository {


    @Mock(R.id.requestFindImage)
    @JsonRequest(value = R.id.requestFindImage, concurrent = true)
    ServerImage requestFindImage;



}
