package com.appzoneltd.lastmile.customer.features.main.packageslist.host;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.appzoneltd.lastmile.customer.R;
import com.base.presentation.annotations.interfaces.BindLayout;
import com.base.presentation.annotations.interfaces.Presenter;
import com.base.presentation.base.abstracts.features.ViewBinder;

import butterknife.BindView;

/**
 * Created by Wafaa on 12/15/2016.
 */


@BindLayout(R.layout.activity_package_list)
public class PackageListActivityViewBinder extends ViewBinder {

    @Presenter
    PackageListActivityPresenter packageListActivityPresenter;


    @BindView(R.id.app_toolbar)
    Toolbar packagesListToolbar;
    @BindView(R.id.activity_package_list_layout)
    FrameLayout containerLayout;


    @Override
    public void initializeAfterBindingViews(Bundle savedInstanceState) {
        getHostActivity().setSupportActionBar(packagesListToolbar);
        getHostActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getHostActivity().getSupportActionBar().setDisplayShowHomeEnabled(true);
    }


}
