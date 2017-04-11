package com.appzoneltd.lastmile.customer.features.main.packageslist.repo;

import com.appzoneltd.lastmile.customer.R;
import com.base.abstraction.annotations.interfaces.ExecutableCommand;
import com.base.cached.ServerMessage;
import com.base.presentation.repos.base.RepositoryResponsesHandler;
import com.base.presentation.repos.json.JsonResponse;
import com.entities.cached.PickupStatusGroup;
import com.google.gson.reflect.TypeToken;

/**
 * the {@link RepositoryResponsesHandler} for packages list
 * <p>
 * Created by Wafaa on 12/15/2016.
 */

public class PackagesListRepositoryResponsesHandler extends RepositoryResponsesHandler<PackageListRepository> {

    @ExecutableCommand(R.id.requestPackagesList)
    JsonResponse<PickupStatusGroup> requestPackagesList() {
        return new JsonResponse<PickupStatusGroup>() {
            @Override
            protected TypeToken<PickupStatusGroup> getTypeToken() {
                return new TypeToken<PickupStatusGroup>() {
                };
            }
        };
    }

    @ExecutableCommand(R.id.requestRating)
    JsonResponse<ServerMessage> submitRatingResponse() {
        return new JsonResponse<ServerMessage>() {

            @Override
            protected TypeToken<ServerMessage> getTypeToken() {
                return new TypeToken<ServerMessage>() {
                };
            }
        };
    }

}
