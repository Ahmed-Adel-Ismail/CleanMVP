package com.appzoneltd.lastmile.customer.features.main.packageslist.models;

import com.appzoneltd.lastmile.customer.features.main.packageslist.repo.PackageListRepository;
import com.base.abstraction.annotations.interfaces.UpdatesHandler;
import com.base.presentation.annotations.interfaces.Repository;
import com.base.presentation.annotations.interfaces.Sync;
import com.base.presentation.models.Model;
import com.base.usecases.annotations.ResponsesHandler;
import com.entities.Notification;
import com.entities.cached.PickupStatusGroup;
import com.entities.cached.Rating;

/**
 * Created by Wafaa on 12/14/2016.
 */

@UpdatesHandler(PackageListModelUpdatesHandler.class)
@ResponsesHandler(PackagesListModelResponseHandler.class)
@Repository(PackageListRepository.class)
public class PackageListModel extends Model {

    public Notification notification;
    public Rating rating;
    @Sync("pickupStatusGroup")
    PickupStatusGroup pickupStatuses;

    public PickupStatusGroup getPickupStatuses() {
        return pickupStatuses;
    }

    public void setPickupStatuses(PickupStatusGroup pickupStatuses) {
        this.pickupStatuses = pickupStatuses;
    }

}
