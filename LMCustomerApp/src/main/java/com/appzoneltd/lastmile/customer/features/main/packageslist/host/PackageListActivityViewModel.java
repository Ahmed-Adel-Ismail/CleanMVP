package com.appzoneltd.lastmile.customer.features.main.packageslist.host;

import android.graphics.drawable.Drawable;
import android.view.MenuItem;
import android.view.View;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.cutomerappsystem.Features;
import com.appzoneltd.lastmile.customer.features.main.packageslist.models.PackageListModel;
import com.appzoneltd.lastmile.customer.subfeatures.notification.NotificationCounterDrawerCommand;
import com.base.abstraction.annotations.interfaces.Executable;
import com.base.presentation.base.presentation.ViewModel;

/**
 * * the {@link ViewModel} for package list screen
 * <p>
 * Created by Wafaa on 12/15/2016.
 */

class PackageListActivityViewModel extends ViewModel {

    MenuItem menuItem;
    private Features.PackageListFragment packageListFragment;


    @Executable(R.id.activity_package_list_layout)
    void invalidateFragment(View view) {
        drawNotificationCounter();
        if (packageListFragment == null) {
            attachFragment();
        }
    }

    private void attachFragment() {
        packageListFragment = new Features.PackageListFragment();
        getFeature().getHostActivity().addFragment(R.id.activity_package_list_layout
                , packageListFragment);
    }

    private void drawNotificationCounter() {
        if (menuItem != null) {
            Drawable drawable = new NotificationCounterDrawerCommand(
                    getFeature().getHostActivity()).execute(menuItem);
            menuItem.setIcon(drawable);
        }
    }

    @Override
    public void onDestroy() {
        packageListFragment = null;
    }
}
