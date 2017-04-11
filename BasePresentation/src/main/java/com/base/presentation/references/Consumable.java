package com.base.presentation.references;

import com.base.presentation.exceptions.references.ConsumeException;

/**
 * a Class that holds an Object that is used only once, you will need to invoke {@link #set(Object)}
 * to set an object, and as soon as {@link #get()} or {@link #consume()} is invoked,
 * the reference to this Object is set to {@code null}, you can invoke {@link #set(Object)}
 * again and so on
 * <p>
 * Created by Ahmed Adel on 12/28/2016.
 */
public class Consumable<T> extends Property<T> {

    public Consumable() {
    }

    public Consumable(T object) {
        super(object);
    }

    /**
     * set an consumable Object that it's reference will be cleared as soon as a call to
     * {@link #consume()} or {@link #get()} is invoked
     *
     * @param object the object to be consumed later on
     */
    @Override
    public T set(T object) {
        super.set(object);
        return object;
    }

    /**
     * get the consumable Object if exists, if it is already consumed, this method will return
     * {@code null}
     *
     * @return the consumable object, or {@code null}
     */
    @Override
    public T get() {
        T newObject = object;
        object = null;
        return newObject;
    }

    /**
     * get the consumable Object if exists, if it is already consumed, this method will throw
     * a {@link ConsumeException}
     *
     * @return the consumable object
     * @throws ConsumeException if the consumable object is {@code null}
     */
    public T consume() throws ConsumeException {
        if (object != null) {
            return get();
        } else {
            throw new ConsumeException();
        }
    }


}
