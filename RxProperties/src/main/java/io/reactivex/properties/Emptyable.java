package io.reactivex.properties;

/**
 * implemented by Classes that can be Logically Empty, like {@link java.util.Collection} classes
 * for example
 * <p/>
 * Created by Ahmed Adel on 9/6/2016.
 */
interface Emptyable {

    /**
     * check if the current instance is logically empty or not
     *
     * @return {@code true} if it is empty, else {@code false}
     */
    boolean isEmpty();
}
