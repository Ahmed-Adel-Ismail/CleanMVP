package com.appzoneltd.lastmile.driver.features.pickup.model;

import com.appzoneltd.lastmile.driver.R;
import com.base.abstraction.annotations.interfaces.Address;
import com.base.cached.ServerMessage;
import com.base.presentation.annotations.interfaces.JsonRequest;
import com.base.presentation.annotations.interfaces.Requester;
import com.base.presentation.repos.base.Repository;
import com.base.usecases.annotations.Mock;
import com.base.usecases.requesters.server.ssl.AuthorizedHttpsRequester;
import com.entities.cached.pakage.PackageDetails;
import com.entities.cached.pakage.PackageLabelsGroup;
import com.entities.cached.pakage.PackageTypesGroup;
import com.entities.cached.cancellation.CancellationReasonsGroup;
import com.entities.cached.pickup.PickupInvoice;

/**
 * the {@link Repository} for the Pickup-process feature
 * <p>
 * Created by Ahmed Adel on 12/24/2016.
 */
@Address(R.id.addressPickupProcessRepository)
@Requester(AuthorizedHttpsRequester.class)
class PickupProcessRepository extends Repository {

    @Mock(R.id.requestAllPackageTypes)
    @JsonRequest(R.id.requestAllPackageTypes)
    PackageTypesGroup requestAllPackageTypes;

    @Mock(R.id.requestAllPackageLabels)
    @JsonRequest(R.id.requestAllPackageLabels)
    PackageLabelsGroup requestAllPackageLabels;

    @Mock(R.id.requestPackageDetails)
    @JsonRequest(R.id.requestPackageDetails)
    PackageDetails requestPackageDetails;

    @Mock(R.id.requestPickupInvoice)
    @JsonRequest(R.id.requestPickupInvoice)
    PickupInvoice requestPickupInvoice;

    @Mock(R.id.requestAllCancellationReasons)
    @JsonRequest(R.id.requestAllCancellationReasons)
    CancellationReasonsGroup requestAllCancellationReasons;

    @Mock(R.id.requestVerifyPackageDetails)
    @JsonRequest(R.id.requestVerifyPackageDetails)
    ServerMessage requestVerifyPackageDetails;

    @Mock(R.id.requestConfirmPickupInvoice)
    @JsonRequest(R.id.requestConfirmPickupInvoice)
    ServerMessage requestConfirmPackageInvoice;

    @Mock(R.id.requestCancelRequest)
    @JsonRequest(R.id.requestCancelRequest)
    ServerMessage requestCancelRequest;

    @Mock(R.id.requestConfirmPackageDocuments)
    @JsonRequest(R.id.requestConfirmPackageDocuments)
    ServerMessage requestConfirmPackageDocuments;

}
