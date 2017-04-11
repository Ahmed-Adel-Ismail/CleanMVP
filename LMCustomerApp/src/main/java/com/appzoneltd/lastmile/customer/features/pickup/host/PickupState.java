package com.appzoneltd.lastmile.customer.features.pickup.host;

import com.appzoneltd.lastmile.customer.features.pickup.models.PickupModel;
import com.base.abstraction.interfaces.Clearable;
import com.base.abstraction.state.FlowControllable;

/**
 * The State of the Pickup CreatePickupRequestParams Progress
 * <p/>
 * Created by Ahmed Adel on 9/26/2016.
 */
class PickupState implements
        Clearable,
        FlowControllable<PickupState> {

    private boolean scheduled;
    private PickupModel model;
    private PickupFragmentsViewModel viewModel;

    protected PickupState(boolean scheduled, PickupFragmentsViewModel viewModel,
                          PickupModel model) {
        this.scheduled = scheduled;
        this.viewModel = viewModel;
        this.model = model;
    }

    protected PickupState(PickupState pickupState) {
        this.scheduled = pickupState.scheduled;
        this.viewModel = pickupState.viewModel;
        this.model = pickupState.model;
    }

    protected final boolean isScheduled() {
        return scheduled;
    }

    protected final PickupFragmentsViewModel getViewModel() {
        return viewModel;
    }

    protected final PickupModel getModel() {
        return model;
    }


    public boolean hasBack() {
        return false;
    }

    public PickupState back() {
        return this;
    }

    public boolean hasNext() {
        return false;
    }

    public PickupState next() {
        return this;
    }


    /**
     * get the {@link PickupState} based on the passed parameters
     *
     * @param scheduled pass {@code true} if the pickup is scheduled
     * @param viewModel the {@link PickupFragmentsViewModel} to be updated
     * @param model     the {@link PickupModel} to check for data validation
     * @return the {@link PickupState} to start the flow
     */
    public static PickupState getState(boolean scheduled, PickupFragmentsViewModel viewModel,
                                       PickupModel model) {
        PickupState pickupState = new PickupState(scheduled, viewModel, model);
        model.getSchedule().setScheduled(scheduled);
        if (scheduled) {
            return new PickupStateSchedule(pickupState);
        } else {
            return new PickupStatePackageDetails(pickupState);
        }
    }


    @Override
    public final void clear() {
        model = null;
        viewModel = null;
    }
}