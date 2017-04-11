package com.appzoneltd.lastmile.customer.features.main.packageslist.repo;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.requesters.SecureUrlLocator;
import com.base.abstraction.annotations.interfaces.Address;
import com.base.presentation.repos.base.Repository;
import com.base.usecases.annotations.Mock;
import com.base.usecases.annotations.RequestsHandler;
import com.base.usecases.annotations.ResponsesHandler;
import com.base.usecases.requesters.base.EntityGateway;
import com.base.usecases.requesters.base.EntityRequester;
import com.base.usecases.requesters.server.ssl.AuthorizedHttpsRequester;
import com.entities.cached.PickupStatusGroup;

/**
 * the repository for packages list screen
 * <p>
 * Created by Wafaa on 12/14/2016.
 */

@Address(R.id.addressPackagesListRepository)
@RequestsHandler(PackagesListRepositoryRequestsHandler.class)
@ResponsesHandler(PackagesListRepositoryResponsesHandler.class)
public class PackageListRepository extends Repository {


    @Mock(R.id.requestPackagesList)
    PickupStatusGroup packagesList;

    private EntityGateway httpsServerGateway;

    @Override
    public void preInitialize() {
        EntityRequester requester = new AuthorizedHttpsRequester(new SecureUrlLocator());
        httpsServerGateway = new EntityGateway(requester, this);
    }

    EntityGateway getHttpsServerGateway() {
        return httpsServerGateway;
    }

    @Override
    public void onClear() {
        if (httpsServerGateway != null) {
            httpsServerGateway.clear();
            httpsServerGateway = null;
        }

    }
}
