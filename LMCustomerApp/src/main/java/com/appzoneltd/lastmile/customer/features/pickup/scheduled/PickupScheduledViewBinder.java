package com.appzoneltd.lastmile.customer.features.pickup.scheduled;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.abstracts.LastMileViewBinder;
import com.appzoneltd.lastmile.customer.features.pickup.models.PickupModel;
import com.appzoneltd.lastmile.customer.subfeatures.refresh.SwipeRefreshPresenter;
import com.appzoneltd.lastmile.customer.subfeatures.refresh.SwipeRefreshViewModel;
import com.base.presentation.base.abstracts.features.Feature;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import butterknife.BindView;

/**
 * A View Binder for Scheduled Pickup Calendar
 * <p/>
 * Created by Ahmed Adel on 9/21/2016.
 */
class PickupScheduledViewBinder extends LastMileViewBinder<PickupModel> {

    @BindView(R.id.scheduled_scroll_layout)
    ScrollView scheduledLayout;
    @BindView(R.id.pickup_scheduled_calendarView)
    MaterialCalendarView calendarView;
    @BindView(R.id.pickup_schedule_time_interval)
    Spinner timeIntervalsSpinner;
    @BindView(R.id.pickup_scheduled_next_btn)
    Button nextButton;
    @BindView(R.id.date_error_msg)
    TextView dateErrorMsg;
    @BindView(R.id.time_error_msg)
    TextView timeErrorMsg;
    @BindView(R.id.scheduled_swipe_layout)
    SwipeRefreshLayout scheduledSwipeLayout;

    public PickupScheduledViewBinder(Feature<PickupModel> feature) {
        super(feature);
    }

    @Override
    public void initializeAfterBindingViews(Bundle savedInstanceState) {
        PickupScheduledViewModel viewModel = new PickupScheduledViewModel(this);
        viewModel.addView(calendarView);
        viewModel.addView(nextButton);
        viewModel.addView(timeIntervalsSpinner);
        viewModel.addView(dateErrorMsg);
        viewModel.addView(timeErrorMsg);
        viewModel.addView(scheduledLayout);
        addEventsSubscriber(new PickupScheduledPresenter(viewModel));
        SwipeRefreshViewModel swipeRefreshViewModel = new SwipeRefreshViewModel(this) {
            @Override
            public long getSwipeRefreshViewId() {
                return R.id.scheduled_swipe_layout;
            }
        };
        swipeRefreshViewModel.addView(scheduledSwipeLayout);
        addEventsSubscriber(new SwipeRefreshPresenter<>(swipeRefreshViewModel));
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_pickup_scheduled;
    }


}
