package com.base.abstraction.aggregates;

import com.base.abstraction.logs.Logger;

import java.util.Collection;

/**
 * a class to do a loop that guarantees that no concurrent exception is thrown
 *
 * @param <T> the type of objects in the collection
 *            <p/>
 *            Created by Ahmed Adel on 8/31/2016.
 */
public abstract class ConcurrentLooper<T> {

    private static final String TAG_LOG = "ConcurrentLooper";

    /**
     * start the loop
     *
     * @param collection the collection to loop on
     * @param o          optional parameters to be passed to
     *                   {@link ConcurrentLooper#onLoop(Object, Object...)}
     * @return {@code true} if the loop ended successfully, else {@code false}
     */
    @SuppressWarnings("unchecked")
    public boolean start(Collection<T> collection, Object... o) {
        if (collection != null) {
            Object[] copy = collection.toArray();
            for (int i = 0; i < copy.length; i++) {
                try {
                    if (!onLoop((T) copy[i], o)) {
                        return false;
                    }
                } catch (Throwable e) {
                    onException((T) copy[i], e, o);
                    return false;
                }
            }
            return true;
        } else {
            Logger.getInstance().error(TAG_LOG, "no collection to loop through in start()");
            return false;
        }

    }

    /**
     * executed on every iteration of the loop
     *
     * @param object the current object to execute it's own code
     * @param o      the optional parameters that was passed to
     *               {@link #start(Collection, Object...)}
     * @return {@code true} if the loop should continue, else {@code false} to
     * stop the loop
     */
    public abstract boolean onLoop(T object, Object... o);

    /**
     * invoked when a {@link Throwable} is thrown while executing the
     * {@link #start(Collection, Object...)} method
     *
     * @param object the object that is currently executing it's code in
     *               {@link #onLoop(Object, Object...)}
     * @param e      the {@link Throwable} that caused the loop to stop
     * @param o      the optional parameters that were passed to
     *               {@link #start(Collection, Object...)} and
     *               {@link #onLoop(Object, Object...)}
     */
    public void onException(T object, Throwable e, Object... o) {
        Logger.getInstance().error(TAG_LOG, e + " @ " + object
                + ".loop(" + o + ")");
        Logger.getInstance().exception(e);
    }
}