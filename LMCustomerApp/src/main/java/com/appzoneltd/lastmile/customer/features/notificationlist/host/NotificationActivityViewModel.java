package com.appzoneltd.lastmile.customer.features.notificationlist.host;

import android.support.annotation.NonNull;
import android.view.View;

import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.presentation.base.abstracts.features.ViewBinder;
import com.base.presentation.base.presentation.ViewModel;

/**
 * Created by Wafaa on 11/23/2016.
 */
class NotificationActivityViewModel extends ViewModel {

    public NotificationActivityViewModel(ViewBinder viewBinder) {
        super(viewBinder);
    }

    @NonNull
    @Override
    protected CommandExecutor<Long, View> createInvalidateCommands() {
        return null;
    }

    @Override
    public void onDestroy() {

    }
}
