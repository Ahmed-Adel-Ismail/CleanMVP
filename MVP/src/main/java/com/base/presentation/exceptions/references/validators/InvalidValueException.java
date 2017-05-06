package com.base.presentation.exceptions.references.validators;

import com.base.abstraction.interfaces.Validateable;
import com.base.presentation.references.Validator;


/**
 * a {@link RuntimeException} that is thrown when {@link Validator#validate()} finds that the
 * value it is validating is instance of {@link Validateable} and it's
 * {@link Validateable#isValid()} returns {@code false} ... or if a value is invalid in the
 * Object that is being checked
 * <p>
 * Created by Ahmed Adel on 12/31/2016.
 */
public class InvalidValueException extends RuntimeException {

    public InvalidValueException(String message) {
        super(message);
    }
}
