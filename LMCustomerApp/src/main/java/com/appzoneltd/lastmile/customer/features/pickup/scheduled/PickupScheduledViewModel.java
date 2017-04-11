package com.appzoneltd.lastmile.customer.features.pickup.scheduled;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.appzoneltd.lastmile.customer.R;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.presentation.base.abstracts.features.ViewBinder;
import com.base.presentation.base.presentation.ViewModel;
import com.base.presentation.listeners.OnEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.Date;

/**
 * the {@link ViewModel} for the Calendar in the Pickup Screen
 * <p/>
 * Created by Ahmed Adel on 9/21/2016.
 */
class PickupScheduledViewModel extends ViewModel {

    private OnEventListener eventListener;
    private boolean showDateErrorMessage;
    private boolean showTimeErrorMessage;
    private Date date;
    private String[] pickupTimesArray;
    private int selectedPickupTimeIndex;
    private boolean isDateSelected = false;
    private boolean validSelectedDate = true;
    private CalendarDay calendarDay = CalendarDay.today();


    PickupScheduledViewModel(ViewBinder viewBinder) {
        super(viewBinder);
        eventListener = new OnEventListener(viewBinder);
    }

    @NonNull
    @Override
    protected CommandExecutor<Long, View> createInvalidateCommands() {
        CommandExecutor<Long, View> commandExecutor = new CommandExecutor<>();
        Command<View, Void> command = createOnInvalidateCalendarCommand();
        commandExecutor.put((long) R.id.pickup_scheduled_calendarView, command);
        command = createOnInvalidateSpinnerCommand();
        commandExecutor.put((long) R.id.pickup_schedule_time_interval, command);
        command = createOnInvalidateNextButtonCommand();
        commandExecutor.put((long) R.id.pickup_scheduled_next_btn, command);
        command = createOnInvalidateDateErrorMsgCommand();
        commandExecutor.put((long) R.id.date_error_msg, command);
        command = createOnInvalidateTimeErrorMsgCommand();
        commandExecutor.put((long) R.id.time_error_msg, command);
        return commandExecutor;
    }


    private Command<View, Void> createOnInvalidateCalendarCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                invalidateCalendar((MaterialCalendarView) view);
                return null;
            }
        };
    }

    private void invalidateCalendar(MaterialCalendarView calendarView) {
        calendarView.setOnDateChangedListener(eventListener);
        if (validSelectedDate) {
            invalidateCalendarSelection(calendarView);
        } else {
            calendarView.setSelectedDate(calendarDay.getDate());
            calendarView.setCurrentDate(calendarDay, true);
        }

    }

    private void invalidateCalendarSelection(MaterialCalendarView calendarView) {
        calendarView.setDateSelected(date, true);
        if (!isDateSelected) {
            eventListener.onDateSelected(calendarView, calendarDay, true);
        }
    }


    private Command<View, Void> createOnInvalidateSpinnerCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View v) {
                if (pickupTimesArray != null) {
                    invalidateSpinner((Spinner) v);
                }
                return null;
            }
        };
    }

    private Command<View, Void> createOnInvalidateNextButtonCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                invalidateNextButton((Button) view);
                return null;
            }
        };
    }

    private Command<View, Void> createOnInvalidateDateErrorMsgCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                TextView textView = (TextView) view;
                if (showDateErrorMessage && date == null) {
                    textView.setVisibility(View.VISIBLE);
                } else {
                    textView.setVisibility(View.GONE);
                }
                return null;
            }
        };
    }

    private Command<View, Void> createOnInvalidateTimeErrorMsgCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                TextView textView = (TextView) view;
                if (showTimeErrorMessage) {
                    textView.setVisibility(View.VISIBLE);
                } else {
                    textView.setVisibility(View.GONE);
                }
                return null;
            }
        };
    }


    private void invalidateSpinner(Spinner spinner) {
        ArrayAdapter<String> spinnerArrayAdapter;
        spinnerArrayAdapter = new ArrayAdapter<>(getFeature().getHostActivity(),
                android.R.layout.simple_spinner_item, pickupTimesArray);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setOnTouchListener(eventListener);
        spinner.setOnItemSelectedListener(eventListener);
        spinner.setSelection(selectedPickupTimeIndex);
    }

    private void invalidateNextButton(Button view) {
        view.setOnClickListener(eventListener);
    }

    void setPickupTimesArray(String[] pickupTimesArray) {
        this.pickupTimesArray = pickupTimesArray;
    }

    void setDate(Date date) {
        this.date = date;
    }

    void setShowDateErrorMessage(boolean showDateErrorMessage) {
        this.showDateErrorMessage = showDateErrorMessage;
    }

    void setSelectedPickupTimeIndex(int selectedPickupTimeIndex) {
        this.selectedPickupTimeIndex = selectedPickupTimeIndex;
    }

    void setCalendarDay(CalendarDay calendarDay) {
        this.calendarDay = calendarDay;
    }

    void setDateSelected(boolean dateSelected) {
        isDateSelected = dateSelected;
    }

    void setShowTimeErrorMessage(boolean showTimeErrorMessage) {
        this.showTimeErrorMessage = showTimeErrorMessage;
    }

    void setValidSelectedDate(boolean validSelectedDate) {
        this.validSelectedDate = validSelectedDate;
    }

    @Override
    public void onDestroy() {
        eventListener = null;
        date = null;
        pickupTimesArray = null;
    }
}
