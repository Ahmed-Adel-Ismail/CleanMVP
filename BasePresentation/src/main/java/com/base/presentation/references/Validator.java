package com.base.presentation.references;

import com.base.abstraction.concurrency.Future;
import com.base.abstraction.exceptions.propagated.ThrowableGroup;
import com.base.presentation.exceptions.references.validators.InvalidNullValueException;
import com.base.presentation.exceptions.references.validators.InvalidValueException;
import com.base.abstraction.interfaces.Emptyable;
import com.base.abstraction.interfaces.Validateable;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * a {@link Property} that holds validation methods
 * <p>
 * Created by Ahmed Adel on 12/31/2016.
 */
public class Validator<T> extends Property<T> {


    private LinkedHashSet<Rule<T>> rules = new LinkedHashSet<>();

    public Validator() {

    }

    public Validator(T object) {
        super(object);
    }

    /**
     * add a {@link Rule} that holds validation logic for the value in this
     * {@link Validator} instance ... it is safe to call this method multiple time
     * on the same {@link Rule} since all the passed instances will be stored in a
     * {@link Set}, so they will be stored as one
     *
     * @param rule a {@link Rule} that will be executed when {@link #validate()}
     *             is executed
     * @return {@code this} instance for chaining
     */
    public Validator<T> addRule(Rule<T> rule) {
        this.rules.add(rule);
        return this;
    }

    /**
     * remove a validation rule that was previously added through {@link #addRule(Rule)}
     *
     * @param rule the {@link Rule} that should be removed
     * @return {@code this} instance for chaining
     */
    public Validator<T> removeRule(Rule<T> rule) {
        this.rules.remove(rule);
        return this;
    }


    /**
     * check if the current stored value is considered valid or not
     *
     * @return a {@link Future} that will either hold the current value as a valid result,
     * or it will hold a {@link ThrowableGroup} of {@link RuntimeException} instances that were
     * thrown when the {@link Rule} Objects were executed
     */
    public Future<T> validate() {
        Future<T> future = new Future<>();
        if (!rules.isEmpty()) {
            executeValidationRules(future);
        } else {
            checkIfNotNull(future);
        }
        return future;
    }


    private void executeValidationRules(Future<T> future) {
        T object = get();
        ThrowableGroup throwableGroup = new ThrowableGroup();
        for (Rule<T> rule : rules) {
            try {
                object = rule.execute(object);
            } catch (Throwable e) {
                throwableGroup.add(e);
            }
        }
        if (throwableGroup.isEmpty()) {
            future.setResult(object);
        } else {
            future.setException(throwableGroup);
        }

    }

    private void checkIfNotNull(Future<T> future) {
        ThrowableGroup throwableGroup = new ThrowableGroup();
        T object = get();
        if (object == null || (object instanceof Emptyable && ((Emptyable) object).isEmpty())) {
            throwableGroup.add(new InvalidNullValueException());
        } else if (object instanceof Validateable && !((Validateable) object).isValid()) {
            throwableGroup.add(new InvalidValueException("isValid() : false"));
        }
        if (throwableGroup.isEmpty()) {
            future.setResult(object);
        } else {
            future.setException(throwableGroup);
        }
    }

    @Override
    public void clear() {
        super.clear();
        rules.clear();
    }
}
