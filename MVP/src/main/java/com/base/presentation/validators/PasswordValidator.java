package com.base.presentation.validators;

import com.base.abstraction.commands.Command;

/**
 * a class to validate password
 * <p>
 * Created by Ahmed Adel on 12/12/2016.
 */
public class PasswordValidator implements Command<String, Boolean> {

    @Override
    public Boolean execute(String password) {
        return password != null && !password.isEmpty() && !password.trim().contains(" ");
    }
}
