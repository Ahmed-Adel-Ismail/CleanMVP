package io.reactivex.properties;


import io.reactivex.properties.exceptions.CheckedReferenceClearedException;

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
            // should not happen
        }
        return object;
    }

    @Override
    public boolean isEmpty() {
        return reference.isEmpty();
    }
}
