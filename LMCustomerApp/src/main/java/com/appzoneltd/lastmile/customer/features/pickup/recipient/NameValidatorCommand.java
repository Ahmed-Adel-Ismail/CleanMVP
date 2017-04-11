package com.appzoneltd.lastmile.customer.features.pickup.recipient;

import android.text.Editable;

import com.appzoneltd.lastmile.customer.R;
import com.base.abstraction.system.AppResources;
import com.base.presentation.base.presentation.ValidatorViewModel;
import com.base.presentation.views.validators.RegexValidatorCommand;

/**
 * a Validator for the recipient name {@link android.widget.EditText}
 * <p/>
 * Created by Ahmed Adel on 10/9/2016.
 */
class NameValidatorCommand extends RegexValidatorCommand {

    private static final String REGEX;

    static {
        REGEX = AppResources.string(R.string.pickup_request_recipient_name_regex);
    }

    public NameValidatorCommand(int viewId, ValidatorViewModel viewModel) {
        super(viewId, viewModel);

    }

    @Override
    public Void execute(Editable p) {
        String text = String.valueOf(p).trim();
        setError(!isRegexMatched(text, REGEX));
        return null;
    }
}
