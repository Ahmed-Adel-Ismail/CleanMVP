package com.appzoneltd.lastmile.customer.features.pickup.host;

/**
 * A State that represents the Pickup Scheduling State for Pickup request progress
 * <p/>
 * Created by Ahmed Adel on 9/26/2016.
 */
class PickupStateSchedule extends PickupState {


    public PickupStateSchedule(PickupState pickupState) {
        super(pickupState);
        getViewModel().setPickupScheduledVisibility(true);
        getViewModel().setTitle(getModel().getSchedule().getTile());
        getViewModel().invalidateViews();
    }


    @Override
    public boolean hasNext() {
        return getModel().getSchedule().isValid();
    }

    @Override
    public PickupState next() {
        getViewModel().setPickupScheduledVisibility(false);
        return new PickupStatePackageDetails(this);
    }


}
