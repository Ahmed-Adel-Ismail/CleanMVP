package com.appzoneltd.lastmile.customer.features.pickup.recipient;

import android.view.View;
import android.widget.TextView;

import com.appzoneltd.lastmile.customer.R;
import com.base.abstraction.commands.Command;
import com.base.presentation.base.presentation.ViewModel;

/**
 * a {@link ViewModel} to handle the UI of the Recipient Details screen
 * Created by Wafaa on 10/4/2016.
 */
class RecipientDetailsViewsInvalidator extends RecipientDetailsInvalidator {

    RecipientDetailsViewsInvalidator(RecipientDetailsViewModel viewModel) {
        super(viewModel);
        Command<View, Void> command = createOnServiceErrorMsgCommand();
        put((long) R.id.shipping_service_msg, command);
        command = createOnServiceTypeErrorMsgCommand();
        put((long) R.id.shipping_service_type_msg, command);
        command = createOnCountryErrorMsgCommand();
        put((long) R.id.country_msg, command);
        command = createOnCityErrorCommand();
        put((long) R.id.city_msg, command);
    }

    private Command<View, Void> createOnServiceErrorMsgCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                TextView errorMsgTextView = (TextView) view;
                invalidateViewVisibility(getViewModel().isServiceErrorMSgVisibility()
                        , errorMsgTextView);
                return null;
            }
        };
    }

    private Command<View, Void> createOnServiceTypeErrorMsgCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                TextView errorMsgTextView = (TextView) view;
                invalidateViewVisibility(getViewModel().isServiceTypeErrorMSgVisibility()
                        , errorMsgTextView);
                return null;
            }
        };
    }

    private Command<View, Void> createOnCountryErrorMsgCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                TextView errorMsgTextView = (TextView) view;
                invalidateViewVisibility(getViewModel().isCountryErrorMSgVisibility()
                        , errorMsgTextView);
                return null;
            }
        };
    }

    private Command<View, Void> createOnCityErrorCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                TextView errorMsgTextView = (TextView) view;
                invalidateViewVisibility(getViewModel().isCityErrorMsgVisibility()
                        , errorMsgTextView);
                return null;
            }
        };
    }

    private void invalidateViewVisibility(boolean visibility, TextView view) {
        if (visibility) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.INVISIBLE);
        }
    }
}
