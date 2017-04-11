package com.base.presentation.exceptions.references.validators;


import com.base.presentation.references.Validator;

/**
 * a {@link RuntimeException} that is thrown if {@link Validator#validate()} found that the
 * current value is {@code null}
 * <p>
 * Created by Ahmed Adel on 12/31/2016.
 */
public class InvalidNullValueException extends RuntimeException {
}
