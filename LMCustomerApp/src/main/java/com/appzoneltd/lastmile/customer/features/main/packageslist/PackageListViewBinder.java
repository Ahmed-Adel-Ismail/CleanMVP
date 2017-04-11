package com.appzoneltd.lastmile.customer.features.main.packageslist;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appzoneltd.lastmile.customer.R;
import com.base.presentation.annotations.interfaces.BindLayout;
import com.base.presentation.annotations.interfaces.Presenter;
import com.base.presentation.base.abstracts.features.ViewBinder;

import butterknife.BindView;

/**
 * Created by Wafaa on 12/14/2016.
 */

@BindLayout(R.layout.fragment_package_list)
public class PackageListViewBinder extends ViewBinder{

    @Presenter
    PackageListPresenter packageListPresenter;

    @BindView(R.id.package_list_container)
    ViewGroup packageListContainer;
    @BindView(R.id.fragment_package_list_package_list)
    RecyclerView packageList;
    @BindView(R.id.fragment_package_list_progress)
    ProgressBar packageListProgress;
    @BindView(R.id.fragment_package_list_error_message)
    TextView errorMsg;
}
