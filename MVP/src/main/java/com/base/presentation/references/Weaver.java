package com.base.presentation.references;

import android.support.annotation.NonNull;

import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.Executor;
import com.base.abstraction.concurrency.Future;
import com.base.abstraction.system.AppResources;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * a class that can add extra behaviors to the given objects through adding variables and methods
 * at runtime
 * <p>
 * to add a variable you can invoke {@link #setVariable(long, Object)}, and to get the value
 * of this variable you can use {@link #getVariable(long)} method
 * <p>
 * to execute a void method, you should call {@link #method(long)}, if your {@link Command}
 * returns a value through a {@link Future}, you can invoke {@link #method(long, Future)}
 * instead
 * <p>
 * it is important to notice that it is the responsibility of the API user to handle
 * passing {@link Future} objects to the proper methods, passing a {@link Future}
 * object to a {@link Command} that does not handle this {@link Future} may cause leaks or long
 * living objects in memory
 * <p>
 * Created by Ahmed Adel on 2/15/2017.
 */
public class Weaver<T> extends Property<T> {

    private final HashMap<Long, Object> variables = new HashMap<>();
    final Executor<WeaverTuple<T>> methods = new Executor<>();

    public Weaver() {
    }

    public Weaver(T object) {
        super(object);
    }

    /**
     * set a variable mapped to the given id
     *
     * @param id    the id of the variable
     * @param value the value of the variable
     * @return {@code this} instance for chaining
     */
    public Weaver<T> setVariable(long id, Object value) {
        variables.put(id, value);
        return this;
    }

    /**
     * get a variable value mapped to the given id
     *
     * @param id  the id of the variable
     * @param <V> the expected type of the value
     * @return the value casted to the expected return type
     * @throws ClassCastException if the type did-not match
     */
    @SuppressWarnings("unchecked")
    public <V> V getVariable(long id) throws ClassCastException {
        return (V) variables.get(id);
    }

    /**
     * add a {@link Command} that will be executed as a method, make sure that this {@link Command}
     * will handle the {@link Future} passed to it if the implementation requires so
     *
     * @param id      the id of the {@link Command}
     * @param command the {@link Command} to be executed
     * @return {@code this} instance for chaining
     */
    public Weaver<T> setMethod(long id, Command<WeaverTuple<T>, ?> command) {
        methods.put(id, command);
        return this;
    }

    /**
     * start invoking a method that does not have any return value, and wont handle any
     * {@link Future} instances passed
     *
     * @param id the id of the method
     * @return the {@link WeaverMethod} instance to complete the invokation of the required
     * {@link Command}
     * @throws UnsupportedOperationException if no methods are set yet through
     *                                       {@link #setMethod(long, Command)}
     */
    public WeaverMethod<T, ?> method(long id) throws UnsupportedOperationException {
        return method(id, null);
    }

    /**
     * start invoking a method that will handle a {@link Future} and set it's result in this
     * {@link Future} ... the actual call to the method is done on invoking
     * {@link WeaverMethod#call()}
     *
     * @param id     the id of the method
     * @param future a {@link Future} instance to hold the result or {@link Exception} caused by
     *               {@link Command#execute(Object)}
     * @param <F>    the type of the {@link Future} passed to / from the {@link Command}
     * @return the {@link WeaverMethod} instance to complete the invokation of the required
     * {@link Command}
     * @throws UnsupportedOperationException if no methods are set yet through
     *                                       {@link #setMethod(long, Command)}
     * @see #method(long)
     */
    public <F extends Future<?>> WeaverMethod<T, F> method(long id, F future)
            throws UnsupportedOperationException {
        if (methods.isEmpty()) {
            throw new UnsupportedOperationException("no functions added yet");
        }
        return new WeaverMethod<>(this, id, future);
    }

    @Override
    public void clear() {
        variables.clear();
        methods.clear();
        super.clear();
    }

    @Override
    public String toString() {
        List<String> variablesNames = Observable
                .fromIterable(variables.entrySet())
                .map(asString())
                .toList()
                .blockingGet();

        List<String> methodsNames = Observable
                .fromIterable(methods.keySet())
                .map(asMethodString())
                .toList()
                .blockingGet();

        LinkedList<String> toStringList = new LinkedList<>();
        toStringList.add("Object=" + get());
        toStringList.addAll(variablesNames);
        toStringList.addAll(methodsNames);

        return toStringList.toString();
    }

    @NonNull
    private Function<Long, String> asMethodString() {
        return new Function<Long, String>() {
            @Override
            public String apply(Long id) throws Exception {
                try {
                    return AppResources.resourceEntryName((int) (long) id) + "=()";
                } catch (Throwable e) {
                    return id + "=()";
                }
            }
        };
    }

    @NonNull
    private Function<Map.Entry<Long, Object>, String> asString() {
        return new Function<Map.Entry<Long, Object>, String>() {
            @Override
            public String apply(Map.Entry<Long, Object> entry) throws Exception {
                try {
                    return AppResources.resourceEntryName((int) (long) entry.getKey())
                            + "=" + entry.getValue();
                } catch (Throwable e) {
                    return entry.getKey() + "=" + entry.getValue();
                }

            }
        };
    }


}


