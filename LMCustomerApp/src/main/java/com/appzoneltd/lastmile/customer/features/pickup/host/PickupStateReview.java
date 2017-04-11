package com.appzoneltd.lastmile.customer.features.pickup.host;

import com.base.presentation.exceptions.OnBackPressException;

/**
 * A State that represents the Pickup Review State for Pickup request progress
 * <p/>
 * Created by Ahmed Adel on 9/26/2016.
 */
class PickupStateReview extends PickupState {


    protected PickupStateReview(PickupState pickupState) {
        super(pickupState);
        getViewModel().setReviewVisibility(true);
        getViewModel().setTitle(getModel().getTile());
        getViewModel().invalidateViews();
    }

    @Override
    public boolean hasBack() {
        return true;
    }

    @Override
    public PickupState back() throws OnBackPressException {
        getViewModel().setReviewVisibility(false);
        return new PickupStateRecipientDetails(this);
    }


}
