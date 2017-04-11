package com.appzoneltd.lastmile.driver.features.pickup.states;

import com.base.abstraction.exceptions.references.states.StateIsMovingToNullException;

class StateDocuments extends PickupProcessState {

    StateDocuments() {
        documentsState.set(true);
    }

    @Override
    public PickupProcessState back() {
        return new StatePayment();
    }


    @Override
    public PickupProcessState next() {
        throw new StateIsMovingToNullException();
    }
}
