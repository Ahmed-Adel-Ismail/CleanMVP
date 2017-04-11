package com.base.presentation.references;

/**
 * a {@link Property} for {@code boolean} values
 * <p>
 * Created by Ahmed Adel on 12/28/2016.
 */
public class BooleanProperty extends Property<Boolean> {

    public BooleanProperty() {
        super(false);
    }

    public BooleanProperty(Boolean object) {
        super(object);
    }

    /**
     * check if the {@code boolean} stored is {@code true} or not
     *
     * @return {@code true} of the stored object is {@code true}, else {@code false}
     */
    public boolean isTrue() {
        return object != null && object;
    }

    /**
     * set the current {@link BooleanProperty} to {@code true}
     *
     * @return {@code this} instance for chaining
     */
    public BooleanProperty asTrue() {
        set(true);
        return this;
    }

    /**
     * set the current {@link BooleanProperty} to {@code false}
     *
     * @return {@code this} instance for chaining
     */
    public BooleanProperty asFalse() {
        set(false);
        return this;
    }

}
