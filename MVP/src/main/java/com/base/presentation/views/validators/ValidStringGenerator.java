package com.base.presentation.views.validators;


import com.base.abstraction.commands.Command;

/**
 * Created by Wafaa on 10/26/2016.
 */
public class ValidStringGenerator implements Command<CharSequence, String> {
    @Override
    public String execute(CharSequence p) {
        String str = String.valueOf(p);
        return (!("null".equals(str))) ? str.trim() : null;
    }
}
