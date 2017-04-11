package com.appzoneltd.lastmile.customer.features.pickup.scheduled;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.features.pickup.models.PickupModel;
import com.base.abstraction.events.Event;
import com.base.abstraction.events.Message;
import com.base.abstraction.system.App;
import com.base.presentation.base.presentation.PresenterUpdater;
import com.base.presentation.commands.RequestBasedCommand;
import com.base.presentation.commands.RequesterCommand;

/**
 * a {@link PresenterUpdater} for {@link PickupScheduledPresenter}
 * <p>
 * Created by Ahmed Adel on 10/17/2016.
 */
class PickupScheduledUpdater
        extends PresenterUpdater<PickupScheduledPresenter, PickupScheduledViewModel, PickupModel> {


    @Override
    public void onUpdateViewModel() {
        getModel().getSchedule().setScheduled(true);
        getViewModel().setSelectedPickupTimeIndex(getModel().getSchedule().getPickupTimeSelectedIndex());
        getViewModel().setDate(getModel().getSchedule().getDate());
        getViewModel().setPickupTimesArray(getModel().getSchedule().getPickupTimesStringArray());
        getViewModel().setDateSelected(getModel().getSchedule().isDateSelected());
        getPresenter().getTimeIntervalRequesterCommand().startRequest();
        timeIntervalBasedCommand(getPresenter().getTimeIntervalRequesterCommand()).execute(null);
        getViewModel().invalidateViews();
    }

    private RequestBasedCommand<Void> timeIntervalBasedCommand(RequesterCommand command) {
        return new RequestBasedCommand<Void>(command) {
            @Override
            protected Void onExecute(Message message) {
                invalidatePickupTimesSpinner();
                return null;
            }

            private void invalidatePickupTimesSpinner() {
                String[] arr = getModel().getSchedule().getPickupTimesStringArray();
                getViewModel().setPickupTimesArray(arr);
                getViewModel().invalidateViews();
            }

            @Override
            protected Void onFailure(Message message) {
                App.getInstance().getActorSystem()
                        .get((long) R.id.addressActivity).execute(createToastEvent());
                return null;
            }

            private Event createToastEvent() {
                Message message = new Message.Builder()
                        .content(R.string.error_loading_pickup_times_interval).build();
                return new Event.Builder(R.id.showToast)
                        .message(message)
                        .build();

            }
        };
    }

}
