package com.base.abstraction.converters;

import com.base.abstraction.commands.Command;
import com.base.abstraction.exceptions.NoArgsInitializationException;

import java.lang.reflect.ParameterizedType;


/**
 * create an instance from a Generic Type arguments
 * <p/>
 * Created by Ahmed Adel on 8/31/2016.
 *
 * @param <T> the type of object created from Generic argument to be returned
 * @deprecated not tested yet
 */
@SuppressWarnings("WeakerAccess")
@Deprecated
public class GenericTypeCreator<T> implements Command<T, Object> {

    private int genericArgumentIndex;

    /**
     * create a {@link GenericTypeCreator}
     *
     * @param genericArgumentIndex the index of the Generic type to create an instance from,
     *                             if it has only one generic type, pass {@code 0}
     */
    public GenericTypeCreator(int genericArgumentIndex) {
        this.genericArgumentIndex = genericArgumentIndex;
    }

    /**
     * create the instance by invoking it's default constructor, where the Generic class SHOULD
     * have a non-args constructor
     *
     * @param genericObject an instance of the class with Generic types as parameter
     * @return a new instance of the Type parameter declared
     * @throws NoArgsInitializationException if the Object failed to initialize
     */
    @SuppressWarnings("unchecked")
    public T execute(Object genericObject) {
        ParameterizedType p = (ParameterizedType) genericObject.getClass().getGenericSuperclass();
        Class<T> c = (Class<T>) p.getActualTypeArguments()[genericArgumentIndex];

        T t = null;
        try {
            t = c.newInstance();
        } catch (Exception e) {
            throw new NoArgsInitializationException(genericObject.getClass(), e);
        }
        return t;
    }
}
