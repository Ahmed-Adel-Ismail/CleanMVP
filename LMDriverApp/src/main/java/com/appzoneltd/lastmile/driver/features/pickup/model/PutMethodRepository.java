package com.appzoneltd.lastmile.driver.features.pickup.model;

import com.appzoneltd.lastmile.driver.R;
import com.base.abstraction.annotations.interfaces.Address;
import com.base.cached.UploadImageResponse;
import com.base.presentation.annotations.interfaces.JsonRequest;
import com.base.presentation.annotations.interfaces.Requester;
import com.base.presentation.repos.base.Repository;
import com.base.usecases.annotations.Mock;
import com.base.usecases.requesters.server.ssl.params.AuthorizedPutHttpsRequester;

@Address(R.id.addressPickupProcessPutMethodRepository)
@Requester(AuthorizedPutHttpsRequester.class)
class PutMethodRepository extends Repository {

    @Mock(R.id.requestUploadImage)
    @JsonRequest(value = R.id.requestUploadImage, concurrent = true)
    UploadImageResponse requestUploadImage;
}
