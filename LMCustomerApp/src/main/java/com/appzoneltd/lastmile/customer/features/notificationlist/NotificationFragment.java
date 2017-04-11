package com.appzoneltd.lastmile.customer.features.notificationlist;

import android.os.Bundle;

import com.appzoneltd.lastmile.customer.R;
import com.base.abstraction.annotations.interfaces.Address;
import com.base.presentation.base.abstracts.features.AbstractFragment;
import com.base.presentation.base.abstracts.features.ViewBinder;

/**
 * Created by Wafaa on 11/23/2016.
 */
@Address(R.id.addressNotificationListFragment)
public class NotificationFragment extends AbstractFragment<NotificationListModel> {

    @Override
    public ViewBinder createViewBinder(Bundle savedInstanceState) {
        return new NotificationFragmentViewBinder(this);
    }
}
