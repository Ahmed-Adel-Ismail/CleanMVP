package com.base.presentation.interfaces;

/**
 * implement this interface if your class will have multiple types based on Values, where
 * each type can be gained through it's value, and each value can be mapped to a type ..
 * the basic usage for such interface is in {@code enum} where every enum Type can be mapped
 * to a value, and we can get the type through this value
 * <p/>
 * Created by Ahmed Adel on 10/3/2016.
 *
 * @param <Type>  the type to be mapped to a value ... in {@code enum} this will be the
 *                {@code enum} type
 * @param <Value> the value mapped to a type
 */
public interface Valueable<Type, Value> {

    /**
     * get the Type based on the value passed
     *
     * @param value the value to get the Type
     * @return the Type mapped to the given value
     */
    Type getType(Value value);

    /**
     * get the value mapped to the value Type
     *
     * @return the value mapped to the value type
     */
    Value getValue();

}
