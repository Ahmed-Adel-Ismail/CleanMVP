package com.appzoneltd.lastmile.customer.features.pickup.host;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.abstracts.LastMileViewBinder;
import com.appzoneltd.lastmile.customer.features.pickup.models.PickupModel;
import com.appzoneltd.lastmile.customer.subfeatures.notification.CounteredNotificationViewModel;
import com.appzoneltd.lastmile.customer.subfeatures.notification.NotificationPresenter;
import com.appzoneltd.lastmile.customer.subfeatures.notification.NotificationViewModel;
import com.base.presentation.annotations.interfaces.BindLayout;
import com.base.presentation.base.abstracts.features.Feature;

import butterknife.BindView;

/**
 * Created by Ahmed Adel on 9/21/2016.
 */
@BindLayout(R.layout.activity_pickup)
class PickupViewBinder extends LastMileViewBinder<PickupModel> {

    @BindView(R.id.fragment_container)
    ViewGroup fragmentContainer;
    @BindView(R.id.pickup_toolbar)
    Toolbar pickupToolbar;

    PickupViewBinder(Feature<PickupModel> feature) {
        super(feature);
    }

    @Override
    public void initializeAfterBindingViews(Bundle savedInstanceState) {
        PickupFragmentsViewModel viewModel = new PickupFragmentsViewModel(this);
        viewModel.addView(fragmentContainer);
        viewModel.addView(pickupToolbar);
        getHostActivity().setSupportActionBar(pickupToolbar);
        getHostActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getHostActivity().getSupportActionBar().setDisplayShowHomeEnabled(true);
        addEventsSubscriber(new PickupFragmentsPresenter(viewModel));
        NotificationViewModel notificationViewModel = new CounteredNotificationViewModel(this);
        notificationViewModel.addView(pickupToolbar);
        addEventsSubscriber(new NotificationPresenter<>(notificationViewModel));
    }

}
