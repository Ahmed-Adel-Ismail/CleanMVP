package com.base.abstraction.actors.base;

import android.os.Looper;

import com.base.abstraction.exceptions.NoLooperException;

/**
 * implement this interface if your class is controlled in a {@link Looper}
 * <p>
 * Created by Ahmed Adel on 12/14/2016.
 */
public interface ThreadHosted {

    /**
     * get the {@link Thread} id
     *
     * @return current the {@link Thread} id
     * @throws NoLooperException if the current instance has no {@link Looper} associated
     *                           with it to get the thread id from it, this is caused if the
     *                           current instance is on the main thread without a looper
     */
    long getThreadId() throws NoLooperException;

}
