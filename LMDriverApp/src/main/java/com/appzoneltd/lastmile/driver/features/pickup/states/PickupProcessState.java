package com.appzoneltd.lastmile.driver.features.pickup.states;

import com.base.presentation.references.BooleanProperty;
import com.base.abstraction.state.SwitchableState;

public abstract class PickupProcessState implements SwitchableState<PickupProcessState> {

    public final BooleanProperty verifyingState = new BooleanProperty(false);
    public final BooleanProperty paymentState = new BooleanProperty(false);
    public final BooleanProperty documentsState = new BooleanProperty(false);

    PickupProcessState() {
    }

}
