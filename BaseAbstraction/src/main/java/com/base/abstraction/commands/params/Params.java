package com.base.abstraction.commands.params;

/**
 * an Object that indicates multiple parameters / arguments
 * <p>
 * Created by Ahmed Adel on 2/1/2017.
 */
@SuppressWarnings("unchecked")
public class Params {

    private Object[] array;

    public static Params create(Object... params) {
        Params p = new Params();
        p.array = params;
        return p;
    }

    /**
     * get the parameter at the given index
     *
     * @param index the index of the parameter
     * @param <T>   the expected return type
     * @return the parameter at the given index
     * @throws IndexOutOfBoundsException if no parameter stored at the given index
     * @throws ClassCastException        if the type expected does not match
     */
    public <T> T get(int index) throws IndexOutOfBoundsException, ClassCastException {
        return (T) array[index];
    }


}
