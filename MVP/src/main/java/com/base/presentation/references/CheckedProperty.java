package com.base.presentation.references;

import com.base.abstraction.exceptions.CheckedReferenceClearedException;
import com.base.abstraction.logs.Logger;
import com.base.abstraction.references.CheckedReference;

/**
 * a {@link Property} that uses a {@link CheckedReference} instead of normal reference
 * <p>
 * Created by Ahmed Adel on 12/28/2016.
 */
public class CheckedProperty<T> extends Property<T> {

    private final CheckedReference<T> reference = new CheckedReference<>(null);

    public CheckedProperty() {
    }

    public CheckedProperty(T object) {
        reference.set(object);
    }

    /**
     * get the Object if still available
     *
     * @return the Object stored
     * @throws CheckedReferenceClearedException if the Object is {@code null}
     */
    @Override
    public T get() throws CheckedReferenceClearedException {
        return reference.get();
    }

    /**
     * set the object of this {@link CheckedProperty} ... this object will be stored in a
     * {@link CheckedReference}, and can be {@code null} any time if no other Objects do refer to it
     *
     * @param object the object to be stored
     */
    @Override
    public T set(T object) {
        reference.set(object);
        try {
            notifyEmittersWithValueSet(get());
        } catch (CheckedReferenceClearedException e) {
            Logger.getInstance().error(getClass(), "should not happen at set() : " + e);
        }
        return object;
    }

    @Override
    public boolean isEmpty() {
        return reference.isEmpty();
    }
}
