package com.base.usecases.callbacks;

import java.lang.ref.WeakReference;

/**
 * implement this interface if your class will hold a reference to {@link Callback} implementer
 * <p/>
 * Created by Ahmed Adel on 9/21/2016.
 *
 * @see Callback
 * @see CallbackDispatcher
 */
public interface CallbackHolder {

    /**
     * set the {@link Callback} listener to this Class
     * <p/>
     * notice that most implementers hold there {@link Callback} in a {@link WeakReference},
     * so you might want to hold reference to this call back in your class so that it wont be setVariable
     * to {@code null} before you finish the required task
     *
     * @param callback a {@link Callback} that will be updated with results
     */
    void setCallback(Callback callback);
}
