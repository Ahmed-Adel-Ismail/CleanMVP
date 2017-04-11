package com.base.presentation.listeners;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

/**
 * Created by Ahmed Adel on 9/21/2016.
 */
public class OnCalendarDateSelectParams {


    public final MaterialCalendarView widget;
    public final CalendarDay date;
    public final boolean selected;

    public OnCalendarDateSelectParams(CalendarDay date, MaterialCalendarView widget,
                                      boolean selected) {
        this.date = date;
        this.widget = widget;
        this.selected = selected;
    }
}
