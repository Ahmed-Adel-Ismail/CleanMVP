package com.base.abstraction.exceptions;

import android.support.annotation.NonNull;

import com.base.abstraction.actors.base.ThreadHosted;

/**
 * an {@link RuntimeException} throws when {@link ThreadHosted#getLooper()} is invoked on an
 * Object that is not controlled by a looper
 * <p>
 * Created by Ahmed Adel on 12/14/2016.
 */
public class NoLooperException extends RuntimeException {

    public NoLooperException(@NonNull Object instance) {
        super("no looper controlling " + instance.getClass());
    }
}
