package com.base.abstraction.reflections;

import com.base.abstraction.commands.Command;
import com.base.abstraction.exceptions.GenericTypeInitializerException;

import java.lang.reflect.ParameterizedType;

/**
 * a class that creates an instance from a Generic type argument
 * <p>
 * Created by Ahmed Adel on 12/29/2016.
 */
public class GenericTypeInitializer<T> implements Command<Class<?>, T> {

    private int genericArgumentIndex;

    public GenericTypeInitializer(int genericArgumentIndex) {
        this.genericArgumentIndex = genericArgumentIndex;
    }

    /**
     * get a new instance of the Generic type
     *
     * @param hostClass the class that holds the Generic parameters
     * @return the new instance of the type in the Generic parameters argument
     */
    @SuppressWarnings("unchecked")
    @Override
    public T execute(Class<?> hostClass) {
        try {
            ParameterizedType t = (ParameterizedType) hostClass.getGenericSuperclass();
            Class<T> klass = (Class<T>) t.getActualTypeArguments()[genericArgumentIndex];
            return (T) new Initializer().execute(klass);
        } catch (Throwable e) {
            throw new GenericTypeInitializerException(e);
        }

    }
}
