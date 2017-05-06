package com.base.presentation.views.validators;

import android.text.Editable;

import com.base.abstraction.commands.Command;
import com.base.presentation.base.presentation.ValidatorViewModel;

/**
 * A {@link Command} that holds default validation for an empty {@link android.widget.EditText}
 * View
 * <p/>
 * Created by Ahmed Adel on 10/9/2016.
 */
public class EmptyEditTextValidatorCommand extends ValidatorCommand<Editable> {

    public EmptyEditTextValidatorCommand(int viewId, ValidatorViewModel viewModel) {
        super(viewId, viewModel);
    }

    @Override
    public Void execute(Editable editable) {
        String s = new ValidStringGenerator().execute(editable);
        setError(new StringValidator().execute(s));
        return null;
    }

}

