package com.appzoneltd.lastmile.customer.features.main.host;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.abstracts.LastMileViewBinder;
import com.appzoneltd.lastmile.customer.features.main.models.MainModel;
import com.appzoneltd.lastmile.customer.subfeatures.drawers.MainDrawerPresenter;
import com.appzoneltd.lastmile.customer.subfeatures.notification.CounteredNotificationViewModel;
import com.appzoneltd.lastmile.customer.subfeatures.notification.NotificationPresenter;
import com.appzoneltd.lastmile.customer.subfeatures.notification.NotificationViewModel;
import com.base.presentation.base.abstracts.features.Feature;

import butterknife.BindView;

/**
 * Created by Wafaa on 12/1/2016.
 */

public class MainActivityViewBinder extends LastMileViewBinder<MainModel> {

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.activity_main_toolbar)
    Toolbar toolbar;

    MainActivityViewBinder(Feature<MainModel> feature) {
        super(feature);
    }

    @Override
    public void initializeAfterBindingViews(Bundle savedInstanceState) {
        addEventsSubscriber(createMainDrawerPresenter());
        MainActivityViewModel viewModel = new MainActivityViewModel(this);
        addEventsSubscriber(new MainActivityPresenter(viewModel));
        NotificationViewModel notificationViewModel = new CounteredNotificationViewModel(this);
        notificationViewModel.addView(toolbar);
        addEventsSubscriber(new NotificationPresenter<>(notificationViewModel));
    }

    private MainDrawerPresenter createMainDrawerPresenter() {
        MainDrawerPresenter.Params p = new MainDrawerPresenter.Params();
        p.viewBinder = this;
        p.drawerLayout = drawerLayout;
        p.navigationView = navigationView;
        return new MainDrawerPresenter(p, toolbar);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_main;
    }

}
