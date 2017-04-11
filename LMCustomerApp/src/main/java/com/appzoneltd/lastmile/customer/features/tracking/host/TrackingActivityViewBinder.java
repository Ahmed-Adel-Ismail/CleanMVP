package com.appzoneltd.lastmile.customer.features.tracking.host;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.abstracts.LastMileViewBinder;
import com.appzoneltd.lastmile.customer.features.tracking.model.TrackingModel;
import com.appzoneltd.lastmile.customer.subfeatures.notification.NotificationPresenter;
import com.appzoneltd.lastmile.customer.subfeatures.notification.NotificationViewModel;
import com.base.presentation.annotations.interfaces.BindLayout;
import com.base.presentation.base.abstracts.features.Feature;

import butterknife.BindView;

/**
 * Created by Wafaa on 11/22/2016.
 */
@BindLayout(R.layout.activity_tracking)
class TrackingActivityViewBinder extends LastMileViewBinder<TrackingModel> {

    @BindView(R.id.app_toolbar)
    Toolbar trackDriverToolbar;

    public TrackingActivityViewBinder(Feature<TrackingModel> feature) {
        super(feature);
    }

    @Override
    public void initializeAfterBindingViews(Bundle savedInstanceState) {
        getHostActivity().setSupportActionBar(trackDriverToolbar);
        getHostActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getHostActivity().getSupportActionBar().setDisplayShowHomeEnabled(true);
        TrackingActivityViewModel viewModel = new TrackingActivityViewModel(this);
        addEventsSubscriber(new TrackingActivityPresenter(viewModel));
        NotificationViewModel notificationViewModel = new NotificationViewModel(this);
        notificationViewModel.addView(trackDriverToolbar);
        addEventsSubscriber(new NotificationPresenter<>(notificationViewModel));
    }


}
