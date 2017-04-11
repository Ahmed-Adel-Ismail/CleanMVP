package com.base.presentation.references;

import android.support.annotation.NonNull;

import com.base.abstraction.commands.Command;

/**
 * a {@link Property} that holds a method for updating itself
 * <p>
 * Created by Ahmed Adel on 12/29/2016.
 *
 * @deprecated use {@link Property#onUpdate(Command)} instead
 */
@Deprecated
public abstract class Updatable<T> extends Property<T> {


    public Updatable() {
    }

    public Updatable(T object) {
        super(object);
    }

    /**
     * update the current value if not {@code null}
     */
    public final void update() {
        if (get() != null) {
            set(update(get()));
        }
    }

    /**
     * invoked when a call to {@link #update()} is executed <b>AND</b> the current value
     * stored is not {@code null}
     *
     * @param object the value object stored
     * @return the new value to be stored
     */
    protected abstract T update(@NonNull T object);

}
