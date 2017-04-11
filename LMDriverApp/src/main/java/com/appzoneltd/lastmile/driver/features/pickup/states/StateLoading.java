package com.appzoneltd.lastmile.driver.features.pickup.states;

import com.base.abstraction.exceptions.references.states.StateIsMovingToNullException;

/**
 * Created by ESC on 12/30/2016.
 */
public class StateLoading extends PickupProcessState {


    @Override
    public PickupProcessState back() {
        throw new StateIsMovingToNullException();
    }


    @Override
    public PickupProcessState next() {
        return new StateVerifying();
    }
}
