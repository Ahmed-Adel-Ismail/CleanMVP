package com.appzoneltd.lastmile.customer.features.notificationlist.host;

import android.os.Bundle;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.abstracts.LastMileActivity;
import com.appzoneltd.lastmile.customer.features.notificationlist.NotificationListModel;
import com.base.presentation.annotations.interfaces.Menu;
import com.base.presentation.base.abstracts.features.ViewBinder;

@Menu(R.menu.notification_menu)
public class NotificationActivity extends LastMileActivity<NotificationListModel> {


    @Override
    protected ViewBinder createLastMileViewBinder(Bundle savedInstanceState) {
        return new NotificationActivityViewBinder(this);
    }


}
