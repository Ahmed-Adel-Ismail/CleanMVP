package com.base.presentation.views.validators;

import android.text.TextUtils;

import com.base.abstraction.commands.Command;


/**
 * Created by Wafaa on 10/26/2016.
 */

public class StringValidator implements Command<CharSequence, Boolean> {
    @Override
    public Boolean execute(CharSequence p) {
        return TextUtils.isEmpty(p);
    }
}
