package com.base.presentation.validators;

import com.base.abstraction.commands.Command;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * a class to validate the email
 * <p>
 * Created by Ahmed Adel on 12/7/2016.
 */
public class EmailValidator implements Command<String, Boolean> {

    @Override
    public Boolean execute(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        String mailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern r = Pattern.compile(mailPattern);
        Matcher matcher = r.matcher(email.trim());
        return matcher.matches();
    }


}
