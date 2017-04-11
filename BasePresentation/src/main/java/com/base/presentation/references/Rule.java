package com.base.presentation.references;

import com.base.abstraction.commands.Command;
import com.base.presentation.exceptions.references.validators.InvalidNullValueException;
import com.base.presentation.exceptions.references.validators.InvalidValueException;

/**
 * a {@link Command} that represents a validation rule, a Rule is a normal {@link Command} that
 * overrides the {@link #equals(Object)} and {@link #hashCode()} methods to be used by
 * {@link Validator} class
 * <p>
 * the default implementation of {@link #equals(Object)} is that any 2 Objects will be equal
 * if they are not {@code null} and there {@link Class#getName()} is equal
 * <p>
 * this class can throw exceptions like {@link InvalidNullValueException},
 * {@link InvalidValueException}, or similar {@link Runtime} exceptions in it's
 * {@link #execute(Object)} method
 * Created by Ahmed Adel on 1/1/2017.
 */
public abstract class Rule<T> implements Command<T, T> {


    @Override
    public boolean equals(Object obj) {
        return obj != null
                && obj instanceof Rule
                && obj.getClass().getName().equals(getClass().getName());
    }

    @Override
    public int hashCode() {
        return getClass().getName().length();
    }
}
