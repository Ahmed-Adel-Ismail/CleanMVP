package com.base.presentation.validators;

import com.base.abstraction.commands.Command;

/**
 * a class to validate mobile numbers
 * <p>
 * Created by Ahmed Adel on 12/7/2016.
 */
public class MobileValidator implements Command<String, Boolean> {
    @Override
    public Boolean execute(String mobileNumber) {
        return mobileNumber != null && !mobileNumber.isEmpty()
                && mobileNumber.trim().matches("[0-9]+");
    }
}
