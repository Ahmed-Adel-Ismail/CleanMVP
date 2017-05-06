package com.base.presentation.validators;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Ahmed Adel on 12/12/2016.
 */
public class PasswordValidatorTest {

    public PasswordValidatorTest() {
    }


    @Test
    public void execute_charsWithSpaces$$false() throws Exception {
        Assert.assertTrue(!new PasswordValidator().execute(" pass word "));
    }


    @Test
    public void execute_CharsWithNoSpaces$$true() throws Exception {
        Assert.assertTrue(new PasswordValidator().execute(" password01 "));
    }

    @Test
    public void execute_specialCharsWithNoSpaces$$true() throws Exception {
        Assert.assertTrue(new PasswordValidator().execute(" &*password%^% "));
    }

    @Test
    public void execute_specialCharsWithSpaces$$false() throws Exception {
        Assert.assertTrue(!new PasswordValidator().execute(" &*pass word%^% "));
    }


}