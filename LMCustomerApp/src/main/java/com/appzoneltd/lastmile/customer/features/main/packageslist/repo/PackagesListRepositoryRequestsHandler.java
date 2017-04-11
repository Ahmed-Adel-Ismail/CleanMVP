package com.appzoneltd.lastmile.customer.features.main.packageslist.repo;

import com.appzoneltd.lastmile.customer.R;
import com.base.abstraction.annotations.interfaces.Executable;
import com.base.usecases.events.RequestMessage;
import com.base.presentation.repos.base.RepositoryRequestsHandler;
import com.base.presentation.repos.json.JsonRequest;

/**
 * Created by Wafaa on 12/15/2016.
 */

public class PackagesListRepositoryRequestsHandler extends RepositoryRequestsHandler
        <PackageListRepository> {

    private JsonRequest jsonRequest;

    @Override
    public void initialize(PackageListRepository packageListRepository) {
        super.initialize(packageListRepository);
        jsonRequest = new JsonRequest(packageListRepository.getHttpsServerGateway());
    }

    @Executable({R.id.requestPackagesList, R.id.requestRating})
    void createJsonRequest(RequestMessage message){
        jsonRequest.execute(message);
    }

    @Override
    public void clear() {
        super.clear();
        jsonRequest = null;
    }

}
