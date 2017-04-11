package com.appzoneltd.lastmile.customer.features.main.packageslist;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.features.main.packageslist.models.PackageListModel;
import com.base.abstraction.annotations.interfaces.Executable;
import com.base.presentation.annotations.interfaces.Sync;
import com.base.presentation.base.abstracts.features.Feature;
import com.base.presentation.base.presentation.ViewModel;
import com.entities.cached.PickupStatusGroup;

/**
 * the {@link ViewModel} for the package list screen
 * <p>
 * Created by Wafaa on 12/14/2016.
 */

public class PackageListViewModel extends ViewModel {

    private final int gridColumnsNumbers = 2;
    private PackageListAdapter packageListAdapter;
    private boolean progressVisibility = true;
    private boolean errorMsgVisibility = false;
    @Sync("pickupStatusGroup")
    PickupStatusGroup group;

    public PackageListViewModel() {
        packageListAdapter = new PackageListAdapter();
    }

    @Executable(R.id.fragment_package_list_package_list)
    void invalidatePackageList(RecyclerView list) {
        LinearLayoutManager layoutManager =
                new GridLayoutManager(getFeature().getHostActivity(), gridColumnsNumbers);
        list.setLayoutManager(layoutManager);
        if (group != null) {
            packageListAdapter.setup(group, list);
        }
    }

    @Executable(R.id.fragment_package_list_progress)
    void invalidateProgress(ProgressBar progressBar) {
        if (progressVisibility) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Executable(R.id.fragment_package_list_error_message)
    void invalidateErrorMsg(TextView textView) {
        if (errorMsgVisibility) {
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
        }
    }

    public void setProgressVisibility(boolean progressVisibility) {
        this.progressVisibility = progressVisibility;
    }

    public void setErrorMsgVisibility(boolean errorMsgVisibility) {
        this.errorMsgVisibility = errorMsgVisibility;
    }
}
