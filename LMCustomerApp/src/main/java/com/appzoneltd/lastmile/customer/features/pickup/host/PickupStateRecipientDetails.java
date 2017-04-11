package com.appzoneltd.lastmile.customer.features.pickup.host;

import com.base.presentation.exceptions.OnBackPressException;

/**
 * A State that represents the Recipient Details State for Pickup request progress
 * <p/>
 * Created by Ahmed Adel on 9/26/2016.
 */
class PickupStateRecipientDetails extends PickupState {

    protected PickupStateRecipientDetails(PickupState pickupState) {
        super(pickupState);
        getViewModel().setRecipientDetailsVisibility(true);
        getViewModel().setTitle(getModel().getRecipient().getTile());
        getViewModel().invalidateViews();
    }

    @Override
    public boolean hasBack() {
        return true;
    }

    @Override
    public PickupState back() throws OnBackPressException {
        getViewModel().setRecipientDetailsVisibility(false);
        return new PickupStatePackageDetails(this);
    }

    @Override
    public boolean hasNext() {
        return getModel().getRecipient().isValid();
    }

    @Override
    public PickupState next() {
        getViewModel().setRecipientDetailsVisibility(false);
        return new PickupStateReview(this);
    }


}
