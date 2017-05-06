package com.base.presentation.views.validators;

import android.text.Editable;
import android.widget.EditText;

import com.base.presentation.base.presentation.ValidatorViewModel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A parent class for Edit-Texts Validators that requires to check for a Regex
 * <p/>
 * Created by Ahmed Adel on 10/9/2016.
 */
public abstract class RegexValidatorCommand extends ValidatorCommand<Editable> {

    public RegexValidatorCommand(int viewId, ValidatorViewModel viewModel) {
        super(viewId, viewModel);
    }

    /**
     * check if the value of {@link EditText#getText()} <b>matches</b> the passed regex
     *
     * @param editable     the {@link Editable} that is found in an {@link EditText}
     * @param regexPattern the pattern to match
     * @return {@code true} if the value in the edit text matches exactly the pattern
     */
    protected final boolean isRegexMatched(String editable, String regexPattern) {
        Pattern p = Pattern.compile(regexPattern);
        Matcher m = p.matcher(String.valueOf(editable));
        return m.matches();
    }

    /**
     * check if the value of {@link EditText#getText()} <b>contains</b> the passed regex
     *
     * @param editable     the {@link Editable} that is found in an {@link EditText}
     * @param regexPattern the pattern to match
     * @return {@code true} if the value in the edit text contains the pattern in any part
     * of it
     */
    protected final boolean isRegexFound(String editable, String regexPattern) {
        Pattern p = Pattern.compile(regexPattern);
        Matcher m = p.matcher(editable);
        return m.find();
    }
}