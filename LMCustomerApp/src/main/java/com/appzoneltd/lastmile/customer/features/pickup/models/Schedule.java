package com.appzoneltd.lastmile.customer.features.pickup.models;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.features.pickup.host.Titleable;
import com.base.abstraction.interfaces.Validateable;
import com.base.abstraction.converters.SerializableObject;
import com.base.abstraction.interfaces.Clearable;
import com.base.abstraction.system.AppResources;
import com.entities.cached.PickupTime;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.List;

/**
 * a Class holds data related to Scheduling a Pickup
 * <p/>
 * Created by Ahmed Adel on 9/27/2016.
 */
public class Schedule extends SerializableObject implements
        Clearable,
        Validateable,
        Titleable {

    private boolean scheduled;
    private Date date;
    private transient CalendarDay calendarDay;
    private String[] pickupTimesStringArray;
    private List<PickupTime> pickupTimes;
    private long selectedTimeId;
    private String selectedTime;
    private int pickupTimeSelectedIndex;
    private boolean isDateSelected;

    Schedule() {
        date = new Date();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isScheduled() {
        return scheduled;
    }

    public void setScheduled(boolean scheduled) {
        this.scheduled = scheduled;
    }

    public String getSelectedTime() {
        return selectedTime;
    }

    public void setSelectedTime(String selectedTime) {
        this.selectedTime = selectedTime;
    }

    public boolean hasPickupTimes() {
        return pickupTimesStringArray != null && pickupTimesStringArray.length > 0;
    }

    public String[] getPickupTimesStringArray() {
        return pickupTimesStringArray;
    }

    void setPickupTimesStringArray(String[] pickupTimesStringArray) {
        this.pickupTimesStringArray = pickupTimesStringArray;
    }

    public boolean isDateSelected() {
        return isDateSelected;
    }

    public void setDateSelected(boolean dateSelected) {
        isDateSelected = dateSelected;
    }

    public void setPickupTimeSelectedIndex(int pickupTimeSelectedIndex) {
        this.pickupTimeSelectedIndex = pickupTimeSelectedIndex;
    }


    public void setCalendarDay(CalendarDay calendarDay) {
        this.calendarDay = calendarDay;
    }

    public int getPickupTimeSelectedIndex() {
        return pickupTimeSelectedIndex;
    }

    public long getSelectedTimeId() {
        return selectedTimeId;
    }

    public void setSelectedTimeId(long selectedTimeId) {
        this.selectedTimeId = selectedTimeId;
    }

    public List<PickupTime> getPickupTimes() {
        return pickupTimes;
    }

    public void setPickupTimes(List<PickupTime> pickupTimes) {
        this.pickupTimes = pickupTimes;
    }

    @Override
    public boolean isValid() {
        return selectedTime != null;
    }

    @Override
    public void clear() {
        date = null;
        pickupTimesStringArray = null;
        selectedTime = null;
    }

    @Override
    public String getTile() {
        return AppResources.string(R.string.scheduled_details_title);
    }


    @Override
    protected void serializeObject(ObjectOutputStream stream)
            throws IOException {

        if (calendarDay != null) {
            stream.writeLong(calendarDay.getDate().getTime());
        } else {
            stream.writeLong(0);
        }

    }

    @Override
    protected void deserializeObject(ObjectInputStream stream)
            throws IOException, ClassCastException {

        long time = stream.readLong();
        if (time != 0) {
            calendarDay = CalendarDay.from(new Date(time));
        }

    }
}
