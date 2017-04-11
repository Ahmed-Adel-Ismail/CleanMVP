package com.base.abstraction.converters;

import android.support.annotation.Nullable;

/**
 * A class that can be used ti increment / decrement {@link Number} sub-classes, where
 * {@code null} values are considered {@code zero} values
 * <p/>
 * Created by Ahmed Adel on 10/5/2016.
 */
public class Incrementer {

    public static Integer increment(@Nullable Integer v) {
        return (v != null) ? ++v : 1;
    }

    public static Integer decrement(@Nullable Integer v) {
        return (v != null) ? --v : -1;
    }

    public static Long increment(@Nullable Long v) {
        return (v != null) ? ++v : 1;
    }

    public static Long decrement(@Nullable Long v) {
        return (v != null) ? --v : -1;
    }

    public static Float increment(@Nullable Float v) {
        return (v != null) ? ++v : 1;
    }

    public static Float decrement(@Nullable Float v) {
        return (v != null) ? --v : -1;
    }

    public static Double increment(@Nullable Double v) {
        return (v != null) ? ++v : 1;
    }

    public static Double decrement(@Nullable Double v) {
        return (v != null) ? --v : -1;
    }


}
