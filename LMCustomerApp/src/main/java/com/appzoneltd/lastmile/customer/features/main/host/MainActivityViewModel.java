package com.appzoneltd.lastmile.customer.features.main.host;

import android.support.annotation.NonNull;
import android.view.View;

import com.appzoneltd.lastmile.customer.features.main.models.MainModel;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.presentation.base.abstracts.features.ViewBinder;
import com.base.presentation.base.presentation.ViewModel;

/**
 * Created by Wafaa on 12/1/2016.
 */

public class MainActivityViewModel extends ViewModel {

    public MainActivityViewModel(ViewBinder viewBinder) {
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
