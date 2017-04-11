package com.appzoneltd.lastmile.driver.features.pickup.states;

import com.base.abstraction.exceptions.references.states.StateIsMovingToNullException;

public class StateVerifying extends PickupProcessState {

    public StateVerifying() {
        verifyingState.set(true);
    }

    @Override
    public PickupProcessState back() {
        throw new StateIsMovingToNullException();
    }

    @Override
    public PickupProcessState next() {
        return new StatePayment();
    }
}
