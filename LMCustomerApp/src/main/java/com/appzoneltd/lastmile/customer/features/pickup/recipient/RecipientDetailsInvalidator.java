package com.appzoneltd.lastmile.customer.features.pickup.recipient;

import android.view.View;

import com.base.abstraction.commands.executors.CommandExecutor;

import java.lang.ref.WeakReference;

/**
 * Created by Wafaa on 10/4/2016.
 */
class RecipientDetailsInvalidator extends CommandExecutor<Long, View> {

    private WeakReference<RecipientDetailsViewModel> viewModelReference;

    public RecipientDetailsInvalidator(RecipientDetailsViewModel viewModel) {
        this.viewModelReference = new WeakReference<>(viewModel);
    }

    protected final RecipientDetailsViewModel getViewModel() {
        return viewModelReference.get();
    }

}
