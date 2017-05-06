package io.reactivex.properties;


import java.lang.ref.WeakReference;

import io.reactivex.annotations.NonNull;
import io.reactivex.properties.exceptions.CheckedReferenceClearedException;

/**
 * a class that acts as a {@link WeakReference}, but if it's {@link #get()} method is invoked
 * after the Object is removed from heap, it will throw a {@link CheckedReferenceClearedException}
 * instead of returning {@code null}
 * <p>
 * Created by Ahmed Adel on 10/30/2016.
 *
 * @param <T> the type of the Object that will be referred by a {@link WeakReference} in this
 *            Object
 */
public class CheckedReference<T> implements Emptyable {

    private WeakReference<T> weakReference;

    /**
     * create an instance of {@link CheckedReference}
     *
     * @param weakReferenceObject the Object that will be held in a {@link WeakReference}
     */
    public CheckedReference(T weakReferenceObject) {
        this.weakReference = new WeakReference<>(weakReferenceObject);
    }

    /**
     * get the Object refered to by the {@link WeakReference} of this {@link CheckedReference}
     *
     * @return the Object
     * @throws CheckedReferenceClearedException if the Object is removed from memory
     */
    @NonNull
    public T get() throws CheckedReferenceClearedException {
        T t = weakReference.get();
        if (t == null) {
            throw new CheckedReferenceClearedException();
        }
        return t;
    }

    /**
     * set the Object in the {@link WeakReference}
     *
     * @param weakReferenceObject the Object that will be referred by the {@link WeakReference}
     */
    public void set(T weakReferenceObject) {
        this.weakReference = new WeakReference<>(weakReferenceObject);
    }

    @Override
    public boolean isEmpty() {
        return weakReference.get() == null;
    }
}
