package com.appzoneltd.lastmile.driver.features.pickup.states;

class StatePayment extends PickupProcessState {

    StatePayment() {
        paymentState.set(true);
    }

    @Override
    public PickupProcessState back() {
        return new StateVerifying();
    }

    @Override
    public PickupProcessState next() {
        return new StateDocuments();
    }
}
