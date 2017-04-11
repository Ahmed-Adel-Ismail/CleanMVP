package com.base.presentation.commands;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.base.abstraction.commands.Command;

import java.lang.ref.WeakReference;

/**
 * A {@link Command} that executes it's code in a delayed matter, it waits for some milli-seconds
 * then triggers {@link #delayedExecute(View)} ... the default delay time is
 * {@link #DEFAULT_DELAY_MILLIS}
 * <p/>
 * <b>note</b> that this class is a {@link Handler} that may run on a UI onThread, so you will
 * need to declare it's sub-class in a {@code static} inner class or in a separate class,
 * and pass the host Class (the class that will use it) to this constructor
 * <p/>
 * Created by Ahmed Adel on 10/12/2016.
 *
 * @param <T> the type of the {@code host} class (the class that uses this one)
 * @param <V> the sub-class of the {@link View} that will be handled in {@link #delayedExecute(View)}
 */
public abstract class DelayedCommand<T, V extends View> implements
        Command<View, Void> {

    private final WeakReference<T> weakReference;

    protected DelayedCommand(T host) {
        this.weakReference = new WeakReference<T>(host);
    }

    /**
     * the default value of delay in milli-seconds, it is 20 milli-second
     */
    private static final int DEFAULT_DELAY_MILLIS = 20;
    private int delayMillis = DEFAULT_DELAY_MILLIS;

    @SuppressWarnings("unchecked")
    @Override
    public final Void execute(View view) {
        WeakReference<V> viewWeakReference = new WeakReference<>((V) view);
        new Handler().postDelayed(createDelayedExecuteRunnable(viewWeakReference), delayMillis);
        return null;
    }

    @NonNull
    private Runnable createDelayedExecuteRunnable(final WeakReference<V> viewRef) {
        return new Runnable() {

            @Override
            public void run() {
                V view = viewRef.get();
                if (view != null) {
                    delayedExecute(view);
                }
            }
        };
    }

    /**
     * change the delay milli-seconds, the default is {@link #DEFAULT_DELAY_MILLIS}
     *
     * @param delayMillis the new milli-seconds value
     * @return {@code this} instance for chaining
     */
    public final DelayedCommand<T, V> setDelayMillis(int delayMillis) {
        this.delayMillis = delayMillis;
        return this;
    }

    /**
     * get the host of this {@link DelayedCommand}, this is stored in a weak reference so it
     * may be {@code null} at any time
     *
     * @return the host of this {@link DelayedCommand}, or {@code null}
     */
    @Nullable
    protected final T getHost() {
        return weakReference.get();
    }

    /**
     * implement this method instead of {@link #execute(View)}, this method will be invoked after
     * the {@link #delayMillis} pass ... to change the {@link #delayMillis}, you can use
     * {@link #setDelayMillis(int)} ... the default delay milliseconds is {@link #DEFAULT_DELAY_MILLIS}
     *
     * @param view the {@link View} sub-class to be used
     */
    protected abstract void delayedExecute(V view);
}
