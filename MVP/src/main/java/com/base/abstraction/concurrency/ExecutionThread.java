package com.base.abstraction.concurrency;


import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * an enum that indicates which thread should hold an operation
 * <p>
 * Created by Ahmed Adel on 12/13/2016.
 */
public enum ExecutionThread {

    MAIN {
        @Override
        public Scheduler scheduler() {
            return AndroidSchedulers.mainThread();
        }
    },
    CURRENT {
        @Override
        public Scheduler scheduler() {
            return Schedulers.trampoline();
        }
    },
    BACKGROUND {
        @Override
        public Scheduler scheduler() {
            return Schedulers.io();
        }
    };

    public abstract Scheduler scheduler();
}
