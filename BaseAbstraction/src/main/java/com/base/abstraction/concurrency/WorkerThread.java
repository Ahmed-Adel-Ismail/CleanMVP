package com.base.abstraction.concurrency;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.MessageQueue;

import com.base.abstraction.commands.Command;
import com.base.abstraction.interfaces.Clearable;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * a {@link HandlerThread} that is used as a {@link Command} as well, remember to invoke
 * {@link #clear()} instead of {@link #quit()} to fully release resources
 * <p>
 * <u>Note:</</u> this class does not use {@link ThreadPoolExecutor} or any similar classes,
 * which means that this class is not optimized to be initialized too many times, it is
 * preferred to initialize this class using {@link #WorkerThread(Looper)} constructor, and
 * giving it an actual {@link Looper} to work with
 * <p>
 * Created by Ahmed Adel on 11/15/2016.
 */
public class WorkerThread extends HandlerThread implements
        Command<Runnable, Void>,
        Clearable {

    private Handler handler;

    /**
     * create a {@link WorkerThread} that will run on it's separate {@link Thread}
     */
    public WorkerThread() {
        this(null);
    }

    /**
     * creatae a {@link WorkerThread} that will run on a given {@link Looper}
     *
     * @param looper the {@link Looper} to control the {@link MessageQueue} of this worker thread,
     *               if {@code null} was passed, this {@link WorkerThread} will work on it's own
     *               background {@link Thread}
     */
    public WorkerThread(Looper looper) {
        super("WorkerThread : " + Thread.currentThread().getId());
        start();
        handler = new Handler((looper == null) ? getLooper() : looper);
    }

    /**
     * get the {@link Looper} that will host the execution of the passed {@link Runnable} instances
     *
     * @return the {@link Looper} associated with the {@link Handler} that executes the
     * {@link Runnable} instances, this method may return different {@link Looper} than
     * {@link #getLooper()} method
     */
    public Looper getExecutionLooper() {
        return handler.getLooper();
    }

    @Override
    public Void execute(Runnable runnable) {
        if (handler != null) {
            handler.post(runnable);
        }
        return null;
    }

    @Override
    public void clear() {
        quit();
        if (handler != null) {
            handler.removeCallbacks(null);
            handler = null;
        }

    }


}
