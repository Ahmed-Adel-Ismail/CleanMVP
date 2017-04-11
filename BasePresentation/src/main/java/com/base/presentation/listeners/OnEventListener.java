package com.base.presentation.listeners;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;

import com.base.abstraction.events.Event;
import com.base.abstraction.events.Message;
import com.base.abstraction.logs.Logger;
import com.base.presentation.R;
import com.base.presentation.base.abstracts.features.AbstractActivity;
import com.base.presentation.base.presentation.Presenter;
import com.base.presentation.interfaces.ActivityHosted;
import com.base.presentation.interfaces.Clickable;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.lang.ref.WeakReference;

/**
 * An {@link android.view.View.OnClickListener} that will notify the {@link AbstractActivity}
 * to be able to notify all it's {@link com.base.abstraction.observer.Observer} classes
 * (including {@link Presenter} classes)
 * <p/>
 * Created by Ahmed Adel on 9/6/2016.
 */
public class OnEventListener implements
        Clickable,
        View.OnClickListener,
        View.OnTouchListener,
        OnDateSelectedListener,
        AdapterView.OnItemSelectedListener,
        NavigationView.OnNavigationItemSelectedListener,
        SwipeRefreshLayout.OnRefreshListener,
        CompoundButton.OnCheckedChangeListener {

    private final WeakReference<AbstractActivity<?>> activityWeakReference;
    private final Options options;
    private boolean touched;


    public OnEventListener(ActivityHosted activityHosted) {
        this(activityHosted, new Options());

    }

    public OnEventListener(ActivityHosted activityHosted, Options options) {
        this.activityWeakReference =
                new WeakReference<AbstractActivity<?>>(activityHosted.getHostActivity());
        this.options = options;
    }

    @Override
    public final void onClick(View v) {
        AbstractActivity activity = activityWeakReference.get();
        if (activity != null) {
            activity.onClick(v);
        }
    }

    @Override
    public final boolean onNavigationItemSelected(@NonNull MenuItem item) {
        AbstractActivity activity = activityWeakReference.get();
        if (activity != null) {
            if (activity.hasLockedInteractions()) {
                activity.onLockedInteractions();
            } else {
                invokeOnNavigationItemSelected(item, activity);
            }
        }
        return true;
    }

    private void invokeOnNavigationItemSelected(MenuItem item, AbstractActivity activity) {
        notifyActivityOnUpdateModel(activity);
        Message message = new Message.Builder().id(item.getItemId()).content(item).build();
        Event event = new Event.Builder(R.id.onNavigationItemSelected).message(message)
                .senderActorAddress(activity.getActorAddress()).build();
        activity.onUpdateWithSameCategoryId(event);
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() != options.onTouchMotionEvent) {
            Logger.getInstance().error(getClass(), "onTouch() motionEvent : "
                    + motionEvent.getAction());
            return false;
        }

        AbstractActivity activity = activityWeakReference.get();
        if (activity != null) {
            if (activity.hasLockedInteractions()) {
                activity.onLockedInteractions();
            } else {
                invokeOnTouch(view, motionEvent, activity);
            }
        }
        return false;
    }

    private void invokeOnTouch(View view, MotionEvent motionEvent, AbstractActivity activity) {
        touched = true;
        notifyActivityOnUpdateModel(activity);
        OnTouchParams p = new OnTouchParams(view, motionEvent);
        Message message = new Message.Builder().id(view.getId()).content(p).build();
        Event event = new Event.Builder(R.id.onTouch).message(message)
                .senderActorAddress(activity.getActorAddress()).build();
        activity.onUpdateWithSameCategoryId(event);
    }


    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget,
                               @NonNull CalendarDay date, boolean selected) {
        AbstractActivity activity = activityWeakReference.get();
        if (activity != null) {
            if (activity.hasLockedInteractions()) {
                activity.onLockedInteractions();
            } else {
                invokeOnDateSelected(widget, date, selected, activity);
            }
        }
    }

    private void invokeOnDateSelected(@NonNull MaterialCalendarView widget,
                                      @NonNull CalendarDay date,
                                      boolean selected, AbstractActivity activity) {
        notifyActivityOnUpdateModel(activity);
        OnCalendarDateSelectParams p = new OnCalendarDateSelectParams(date, widget, selected);
        Message message = new Message.Builder().content(p).build();
        Event event = new Event.Builder(R.id.onCalendarDateChanged).message(message)
                .senderActorAddress(activity.getActorAddress()).build();
        activity.onUpdateWithSameCategoryId(event);
    }

    private void notifyActivityOnUpdateModel(AbstractActivity activity) {
        Event event = new Event.Builder(R.id.onUpdateModel)
                .senderActorAddress(activity.getActorAddress())
                .build();
        activity.onUpdateWithSameCategoryId(event);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        AbstractActivity activity = activityWeakReference.get();
        if (activity != null) {
            if (activity.hasLockedInteractions()) {
                activity.onLockedInteractions();
            } else if (touched) {
                invokeOnItemSelected(parent, view, position, id, activity);
            }
        }
    }

    private void invokeOnItemSelected(AdapterView<?> parent, View view, int position, long id,
                                      AbstractActivity activity) {
        touched = false;
        OnItemSelectedParam p = new OnItemSelectedParam(parent, view, position, id);
        Message message = new Message.Builder()
                .content(p)
                .id(parent.getId())
                .build();
        Event event = new Event.Builder(R.id.onItemSelected).message(message)
                .senderActorAddress(activity.getActorAddress()).build();
        activity.onUpdateWithSameCategoryId(event);
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onRefresh() {
        AbstractActivity activity = activityWeakReference.get();
        if (activity != null) {
            Event event = new Event.Builder(R.id.onRefresh).build();
            activity.onUpdateWithSameCategoryId(event);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        AbstractActivity activity = activityWeakReference.get();
        if (activity != null) {
            if (activity.hasLockedInteractions()) {
                activity.onLockedInteractions();
            } else {
                invokeOnCheckedChanged(compoundButton, checked, activity);
            }
        }
    }

    private void invokeOnCheckedChanged(
            CompoundButton compoundButton,
            boolean checked,
            AbstractActivity activity) {

        OnCheckedChangedParams p = new OnCheckedChangedParams();
        p.compoundButton.set(compoundButton);
        p.checked.set(checked);
        p.tag.set(compoundButton.getTag());
        Message message = new Message.Builder().id(compoundButton.getId()).content(p).build();
        Event event = new Event.Builder(R.id.onCheckedChanged).message(message)
                .senderActorAddress(activity.getActorAddress()).build();
        activity.onUpdateWithSameCategoryId(event);


    }

    /**
     * the options to setup the {@link OnEventListener}
     */
    public static class Options {

        private int onTouchMotionEvent = MotionEvent.ACTION_UP;

        public Options() {
        }

        /**
         * set the {@link MotionEvent} action that will be used to listen
         * to touches, the default is {@link {@link MotionEvent#ACTION_UP}}
         *
         * @param onTouchMotionEvent the motion event to be used as the touch event
         * @return {@code this} instance for chaining
         */
        public Options onTouchMotionEvent(int onTouchMotionEvent) {
            this.onTouchMotionEvent = onTouchMotionEvent;
            return this;
        }
    }

}
