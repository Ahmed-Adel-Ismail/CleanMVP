package com.appzoneltd.lastmile.customer.features.notificationlist;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.abstracts.LastMileViewBinder;
import com.base.presentation.base.abstracts.features.Feature;

import butterknife.BindView;

/**
 * Created by Wafaa on 11/14/2016.
 */

public class NotificationFragmentViewBinder extends LastMileViewBinder<NotificationListModel> {

    @BindView(R.id.notification_list)
    RecyclerView notificationList;
    @BindView(R.id.fragment_notification_empty_msg)
    TextView emptyMsg;
    @BindView(R.id.frame_notification_layout)
    ViewGroup layout;

    public NotificationFragmentViewBinder(Feature<NotificationListModel> feature) {
        super(feature);
    }

    @Override
    public void initializeAfterBindingViews(Bundle savedInstanceState) {
        NotificationFragmentViewModel viewModel = new NotificationFragmentViewModel(this);
        viewModel.addView(notificationList);
        viewModel.addView(layout);
        viewModel.addView(emptyMsg);
        addEventsSubscriber(new NotificationFragmentPresenter(viewModel));
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_notification;
    }

}
