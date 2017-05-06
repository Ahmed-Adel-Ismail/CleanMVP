package com.base.usecases.callbacks;

import com.base.abstraction.events.Event;
import com.base.abstraction.interfaces.Clearable;
import com.base.usecases.requesters.base.Requester;

import java.lang.ref.WeakReference;

/**
 * implement this interface if your Class will dispatch or notify Callbacks to a
 * {@link Callback} class ... notice that for some classes the
 * {@link Callback} is mandatory to work properly, look for the Java-doc of the Implementers
 * of this interface
 * <p/>
 * Created by Ahmed Adel on 9/21/2016.
 *
 * @see Callback
 * @see CallbackHolder
 * @see Requester
 */
public interface CallbackDispatcher extends CallbackHolder {

    /**
     * notify the {@link Callback} listener
     *
     * @param event the {@link Event} to notify this {@link Callback} with
     */
    void notifyCallback(Event event);


    /**
     * an implementer for the {@link CallbackDispatcher} interface,
     * when {@link #setCallback(Callback)} is invoked, it stores the
     * passed {@link Callback} in a {@link WeakReference}, and when {@link #notifyCallback(Event)}
     * is invoked, it notifies this {@link Callback} if it is still available
     */
    class WeakImplementer implements CallbackDispatcher, Clearable {

        private WeakReference<Callback> callbackReference;

        @Override
        public final void setCallback(Callback callback) {
            callbackReference = new WeakReference<>(callback);
        }

        @Override
        public final void notifyCallback(Event event) {
            Callback callback = (callbackReference != null) ? callbackReference.get() : null;
            if (callback != null) {
                callback.onCallback(event);
            }
        }

        @Override
        public final void clear() {
            callbackReference = null;
        }
    }

    /**
     * an implementer for the {@link CallbackDispatcher} interface,
     * when {@link #setCallback(Callback)} is invoked, it stores the
     * passed {@link Callback}, and when {@link #notifyCallback(Event)}
     * is invoked, it notifies this {@link Callback} if it is still available
     */
    class ConcreteImplementer implements CallbackDispatcher, Clearable {

        private Callback callback;

        @Override
        public final void setCallback(Callback callback) {
            this.callback = callback;
        }

        public final Callback getCallback() {
            return callback;
        }

        @Override
        public final void notifyCallback(Event event) {
            if (callback != null) {
                callback.onCallback(event);
            }
        }

        @Override
        public final void clear() {
            callback = null;
        }
    }

}
