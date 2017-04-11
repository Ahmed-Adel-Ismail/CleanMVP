package com.appzoneltd.lastmile.customer.features.notificationlist.host;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.abstracts.LastMileViewBinder;
import com.appzoneltd.lastmile.customer.features.notificationlist.NotificationListModel;
import com.appzoneltd.lastmile.customer.subfeatures.notification.NotificationPresenter;
import com.appzoneltd.lastmile.customer.subfeatures.notification.NotificationViewModel;
import com.base.presentation.base.abstracts.features.Feature;

import butterknife.BindView;

/**
 * Created by Wafaa on 11/23/2016.
 */

public class NotificationActivityViewBinder extends LastMileViewBinder<NotificationListModel> {

    @BindView(R.id.app_toolbar)
    Toolbar notificationListToolbar;

    public NotificationActivityViewBinder(Feature<NotificationListModel> feature) {
        super(feature);
    }

    @Override
    public void initializeAfterBindingViews(Bundle savedInstanceState) {
        getHostActivity().setSupportActionBar(notificationListToolbar);
        getHostActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getHostActivity().getSupportActionBar().setDisplayShowHomeEnabled(true);
        NotificationActivityViewModel viewModel = new NotificationActivityViewModel(this);
        addEventsSubscriber(new NotificationActivityPresenter(viewModel));
        NotificationViewModel notificationViewModel = new NotificationViewModel(this);
        addEventsSubscriber(new NotificationPresenter<>(notificationViewModel));
    }

    @Override
    public int getContentView() {
        return R.layout.activity_notification;
    }


}
