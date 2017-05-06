package com.base.abstraction.converters;

import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.method.NumberKeyListener;
import android.widget.ProgressBar;

import com.base.presentation.references.Property;

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

    /**
     * increment the value inside a given {@link Property} class
     *
     * @param property the class that holds a numeric value
     * @param <N>      the numeric value type, which is a {@link Number}
     * @param <T>      the {@link Property} class (or sub-class)
     * @return the {@link Property} passed after incrementing the numeric value inside it,
     * or {@code null} if the passed value was {@code null}
     * @throws UnsupportedOperationException if the value in the passed {@link Property} is not
     *                                       supported by this class
     */
    @SuppressWarnings("unchecked")
    public static <N extends Number, T extends Property<N>> T increment(T property) {

        if (property == null) {
            return null;
        }

        N object = property.get();

        if (object == null) {
            return property;
        } else if (object instanceof Integer) {
            return (T) incrementInt((Property<Integer>) property);
        } else if (object instanceof Long) {
            return (T) incrementLong((Property<Long>) property);
        } else if (object instanceof Double) {
            return (T) incrementDouble((Property<Double>) property);
        } else if (object instanceof Float) {
            return (T) incrementFloat((Property<Float>) property);
        } else {
            throw new UnsupportedOperationException("unsupported numeric type");
        }
    }

    /**
     * decrement the value inside a given {@link Property} class
     *
     * @param property the class that holds a numeric value
     * @param <N>      the numeric value type, which is a {@link Number}
     * @param <T>      the {@link Property} class (or sub-class)
     * @return the {@link Property} passed after decrementing the numeric value inside it,
     * or {@code null} if the passed value was {@code null}
     * @throws UnsupportedOperationException if the value in the passed {@link Property} is not
     *                                       supported by this class
     */
    @SuppressWarnings("unchecked")
    public static <N extends Number, T extends Property<N>> T decrement(T property) {

        if (property == null) {
            return null;
        }

        N object = property.get();

        if (object == null) {
            return property;
        } else if (object instanceof Integer) {
            return (T) decrementInt((Property<Integer>) property);
        } else if (object instanceof Long) {
            return (T) decrementLong((Property<Long>) property);
        } else if (object instanceof Double) {
            return (T) decrementDouble((Property<Double>) property);
        } else if (object instanceof Float) {
            return (T) decrementFloat((Property<Float>) property);
        } else {
            throw new UnsupportedOperationException("unsupported numeric type");
        }
    }

    private static <T extends Property<Integer>> T incrementInt(@NonNull T property) {
        property.set(increment(property.get()));
        return property;
    }

    private static <T extends Property<Integer>> T decrementInt(@NonNull T property) {
        property.set(decrement(property.get()));
        return property;
    }

    private static <T extends Property<Long>> T incrementLong(@NonNull T property) {
        property.set(increment(property.get()));
        return property;
    }

    private static <T extends Property<Long>> T decrementLong(@NonNull T property) {
        property.set(decrement(property.get()));
        return property;
    }

    private static <T extends Property<Double>> T incrementDouble(@NonNull T property) {
        property.set(increment(property.get()));
        return property;
    }

    private static <T extends Property<Double>> T decrementDouble(@NonNull T property) {
        property.set(decrement(property.get()));
        return property;
    }

    private static <T extends Property<Float>> T incrementFloat(@NonNull T property) {
        property.set(increment(property.get()));
        return property;
    }

    private static <T extends Property<Float>> T decrementFloat(@NonNull T property) {
        property.set(decrement(property.get()));
        return property;
    }


}
