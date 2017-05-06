package com.base.abstraction.reflections;

import com.base.abstraction.commands.Command;

import java.lang.reflect.Constructor;

/**
 * a {@link Command} to create an instance from an {@code default} constructor in a {@link Class}
 * instance
 * <p>
 * Created by Ahmed Adel on 12/11/2016.
 */
public class Initializer<T> implements Command<Class<? extends T>, T> {

    @Override
    public T execute(Class<? extends T> objectClass) {
        try {
            Constructor<?> constructor = objectClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            return (T) constructor.newInstance();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}


