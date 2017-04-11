package com.appzoneltd.lastmile.customer.features.pickup.host;

import com.base.presentation.exceptions.OnBackPressException;

/**
 * A State that represents the Package Details State for Pickup request progress
 * <p/>
 * Created by Ahmed Adel on 9/26/2016.
 */
class PickupStatePackageDetails extends PickupState {


    public PickupStatePackageDetails(PickupState pickupState) {
        super(pickupState);
        getViewModel().setPackageDetailsVisibility(true);
        getViewModel().setTitle(getModel().getPackage().getTile());
        getViewModel().invalidateViews();
    }

    @Override
    public boolean hasBack() {
        return isScheduled();
    }

    @Override
    public PickupState back() throws OnBackPressException {
        getViewModel().setPackageDetailsVisibility(false);
        return new PickupStateSchedule(this);
    }

    @Override
    public boolean hasNext() {
        return getModel().getPackage().isValid();
    }

    @Override
    public PickupState next() {
        getViewModel().setPackageDetailsVisibility(false);
        return new PickupStateRecipientDetails(this);
    }


}
