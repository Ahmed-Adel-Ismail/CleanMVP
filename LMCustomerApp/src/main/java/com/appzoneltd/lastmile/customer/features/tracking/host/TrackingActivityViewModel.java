package com.appzoneltd.lastmile.customer.features.tracking.host;

import android.support.annotation.NonNull;
import android.view.View;

import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.presentation.base.abstracts.features.ViewBinder;
import com.base.presentation.base.presentation.ViewModel;

/**
 * Created by Wafaa on 11/22/2016.
 */

public class TrackingActivityViewModel extends ViewModel {


    public TrackingActivityViewModel(ViewBinder viewBinder) {
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
