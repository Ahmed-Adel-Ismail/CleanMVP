package com.appzoneltd.lastmile.customer.features.pickup.review;

import android.view.View;
import android.widget.TextView;

import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.presentation.views.validators.StringValidator;
import com.base.presentation.views.validators.ValidStringGenerator;

import java.lang.ref.WeakReference;

/**
 * a {@link CommandExecutor} for creating the invalidate commands for the Pickup "Review"
 * Screen
 * <p/>
 * Created by Ahmed Adel on 9/27/2016.
 */
class PickupReviewInvalidator extends CommandExecutor<Long, View> {

    private static final String EMPTY_TEXT = "";
    private WeakReference<PickupReviewViewModel> viewModelReference;

    public PickupReviewInvalidator(PickupReviewViewModel viewModel) {
        this.viewModelReference = new WeakReference<>(viewModel);
    }

    protected final PickupReviewViewModel getViewModel() {
        return viewModelReference.get();
    }

    protected final void updateTextView(View view, String text) {
        TextView textView = (TextView) view;
        text = new ValidStringGenerator().execute(text);
        if (!(new StringValidator().execute(text))) {
            textView.setText(text);
        } else {
            textView.setText(EMPTY_TEXT);
        }
    }


}
