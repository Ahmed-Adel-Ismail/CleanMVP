package com.appzoneltd.lastmile.customer.features.pickup.scheduled;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.features.pickup.models.PickupModel;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.abstraction.events.Event;
import com.base.abstraction.events.Message;
import com.base.abstraction.system.AppResources;
import com.base.presentation.base.presentation.Presenter;
import com.base.presentation.commands.CashedRequesterCommand;
import com.base.presentation.listeners.OnCalendarDateSelectParams;
import com.base.presentation.listeners.OnItemSelectedParam;
import com.base.usecases.events.ResponseMessage;

import java.util.Calendar;
import java.util.Date;

/**
 * A Presenter for the Scheduled pickup Fragment
 * <p/>
 * Created by Ahmed Adel on 9/21/2016.
 */
class PickupScheduledPresenter extends
        Presenter<PickupScheduledPresenter, PickupScheduledViewModel, PickupModel> {

    private CashedRequesterCommand timeIntervalRequesterCommand;

    @Override
    public void preInitialize() {
        this.timeIntervalRequesterCommand = createTimeIntervalRequesterCommand();

    }

    private CashedRequesterCommand createTimeIntervalRequesterCommand() {
        return new CashedRequesterCommand(this.getObservable(), R.id.requestPickupTimeInterval) {
            @Override
            public boolean isDataCashed() {
                if (getModel().getSchedule().getPickupTimesStringArray() != null &&
                        getModel().getSchedule().getPickupTimesStringArray().length > 0) {
                    return true;
                }
                return false;
            }

            @Override
            protected boolean request() {
                Event event = new Event.Builder(R.id.requestPickupTimeInterval).build();
                getModel().execute(event);
                return true;
            }
        };
    }


    PickupScheduledPresenter(PickupScheduledViewModel abstractViewModel) {
        super(abstractViewModel);
        setUpdater(new PickupScheduledUpdater());
    }

    @NonNull
    protected CommandExecutor<Long, View> createOnClickCommandExecutor() {
        CommandExecutor<Long, View> commandExecutor = new CommandExecutor<>();
        Command<View, Void> command = createOnNextButtonClickCommand();
        commandExecutor.put((long) R.id.pickup_scheduled_next_btn, command);
        return commandExecutor;
    }

    private Command<View, Void> createOnNextButtonClickCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                boolean valid = getModel().getSchedule().isValid();
                getViewModel().setShowDateErrorMessage(!valid);
                getViewModel().setShowTimeErrorMessage(!valid);
                getViewModel().invalidateViews();
                return null;
            }
        };
    }

    @NonNull
    @Override
    protected CommandExecutor<Long, ResponseMessage> createResponseCommands() {
        CommandExecutor<Long, ResponseMessage> commandExecutor = new CommandExecutor<>();
        commandExecutor.put((long) R.id.requestPickupTimeInterval, timeIntervalRequesterCommand);
        return commandExecutor;
    }

    @Override
    protected CommandExecutor<Long, Message> createCommandExecutor() {
        CommandExecutor<Long, Message> commandExecutor = new CommandExecutor<>();
        Command<Message, Void> command = createOnCalendarChangedCommand();
        commandExecutor.put((long) R.id.onCalendarDateChanged, command);
        command = createOnItemSelectedCommand();
        commandExecutor.put((long) R.id.onItemSelected, command);
        return commandExecutor;
    }

    private Command<Message, Void> createOnCalendarChangedCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                OnCalendarDateSelectParams p = message.getContent();
                Date selectedDate = p.date.getDate();
                if (isValidDate(selectedDate)) {
                    getModel().getSchedule().setCalendarDay(p.date);
                    getModel().getSchedule().setDate(selectedDate);
                    getModel().getSchedule().setDateSelected(true);
                    getViewModel().setDate(selectedDate);
                    getViewModel().setCalendarDay(p.date);
                    getViewModel().setValidSelectedDate(true);
                } else {
                    getViewModel().setValidSelectedDate(false);
                    showToast();
                    getViewModel().invalidateViews();
                }
                return null;
            }

            private boolean isValidDate(Date date) {
                boolean valid = true;
                if (getSelectedDay(date) < getToday()) {
                    valid = false;
                }
                return valid;
            }

            private int getToday() {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                return calendar.get(Calendar.DAY_OF_YEAR);
            }

            private int getSelectedDay(Date date) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                return calendar.get(Calendar.DAY_OF_YEAR);
            }

            private void showToast() {
                Toast.makeText(getHostActivity()
                        , AppResources.string(R.string.select_date_msg)
                        , Toast.LENGTH_SHORT).show();
            }
        };
    }

    private Command<Message, Void> createOnItemSelectedCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                OnItemSelectedParam p = message.getContent();
                int selectedItemIndex = p.position;
                getModel().getSchedule().setPickupTimeSelectedIndex(selectedItemIndex);
                getModel().getSchedule().setSelectedTime
                        (getModel().getSchedule().getPickupTimesStringArray()[p.position]);
                getViewModel().setSelectedPickupTimeIndex(selectedItemIndex);
                getModel().getSchedule().setSelectedTimeId(
                        getModel().getSchedule().getPickupTimes()
                                .get(p.position).getPickupTimeId());
                return null;
            }
        };
    }

    CashedRequesterCommand getTimeIntervalRequesterCommand() {
        return timeIntervalRequesterCommand;
    }

    @Override
    public void onDestroy() {
        timeIntervalRequesterCommand = null;
    }
}
