package com.base.abstraction.exceptions.propagated;

import android.support.annotation.NonNull;

import com.base.abstraction.aggregates.AggregateAddable;
import com.base.abstraction.aggregates.AggregateContainable;
import com.base.abstraction.interfaces.Clearable;
import com.base.abstraction.interfaces.Emptyable;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * a Class that can hold Multiple {@link Throwable} instances, and can be propagated throw the
 * {@code throws} keyword, this is useful when you want to find out multiple {@link Throwable}
 * instances thrown in a loop
 * <p/>
 * Created by Ahmed Adel on 9/6/2016.
 */
public class ThrowableGroup extends DuckableException implements
        Clearable,
        Emptyable,
        AggregateContainable<Class<? extends Throwable>>,
        AggregateAddable<Boolean, Throwable>, Iterable<Throwable> {

    private final LinkedList<Throwable> throwableList = new LinkedList<>();

    @Override
    public Boolean add(Throwable throwable) {
        if (throwable instanceof ThrowableGroup) {
            ThrowableGroup newThrowable = (ThrowableGroup) throwable;
            return throwableList.addAll(newThrowable.throwableList);
        } else {
            return throwableList.add(throwable);
        }
    }

    /**
     * search if the passed {@link Throwable} has the same name of any {@link Throwable}
     * instance
     *
     * @param throwableClass the Class to search for
     * @return {@code true} if any {@link Throwable} is from the same type of the passed
     * {@link Throwable} Class
     */
    @Override
    public boolean contains(@NonNull Class<? extends Throwable> throwableClass) {
        Class<? extends Throwable> foundThrowableClass;
        for (Throwable throwable : this) {
            foundThrowableClass = throwable.getClass();
            if (throwableClass.isAssignableFrom(foundThrowableClass)) {
                return true;
            }
        }
        return false;
    }

    /**
     * get the related {@link Exception} if found
     *
     * @param throwableClass the {@link Exception} to search for it, or any of it's sub-classes
     * @return the {@link Exception} or it's sub-class if found, or {@code null{}}
     */
    public Class<? extends Throwable> getRelatedException(
            @NonNull Class<? extends Throwable> throwableClass) {

        Class<? extends Throwable> foundThrowableClass;
        for (Throwable throwable : this) {
            foundThrowableClass = throwable.getClass();
            if (throwableClass.isAssignableFrom(foundThrowableClass)) {
                return foundThrowableClass;
            }
        }

        return null;
        
    }

    @Override
    public void clear() {
        throwableList.clear();
    }

    @Override
    public String toString() {
        return throwableList.toString();
    }

    @Override
    public boolean isEmpty() {
        return throwableList.isEmpty();
    }

    @Override
    public Iterator<Throwable> iterator() {
        return throwableList.iterator();
    }
}
