package com.base.presentation.references;

import android.support.annotation.CallSuper;

import com.base.abstraction.commands.Command;
import com.base.abstraction.interfaces.Clearable;
import com.base.abstraction.interfaces.Emptyable;

/**
 * a class that acts as a property, it holds it's {@link #set(Object)} and {@link #get()}
 * methods for the stored value
 * <p>
 * Created by Ahmed Adel on 12/28/2016.
 */
public class Property<T> implements Clearable, Emptyable {


    T object;
    private Command<T, T> onSet;
    private Command<T, T> onGet;
    private Command<T, ?> onUpdate;
    private PropertiesBuilder<T> propertiesBuilder = new PropertiesBuilder<>(this);


    public Property() {
    }

    public Property(T object) {
        this.object = object;
    }

    /**
     * set an Object as the value of this property
     *
     * @param object the object to be stored
     * @return the stored object after being updated
     */
    public T set(T object) {
        if (onSet != null) {
            this.object = onSet.execute(object);
        } else {
            this.object = object;
        }

        if (onUpdate != null) {
            onUpdate.execute(this.object);
        }
        return object;
    }

    /**
     * get the Object referenced as the value of this property
     *
     * @return the value if stored, or {@code null} if nothing is stored
     */
    public T get() {
        if (onGet != null) {
            return onGet.execute(object);
        } else {
            return object;
        }
    }

    /**
     * set a {@link Command} that will be executed when {@link #get()} method is invoked
     *
     * @param onGet the {@link Command} that will be executed every time {@link #get()} method is
     *              invoked, it will take the original value stored as a parameter, and it will
     *              return the updated value as it's return value (which will then be returned
     *              by the {@link #get()} method)
     * @param <S>   the sub-class of this {@link Property}
     * @return the sub-class of this {@link Property} to be used for chaining
     */
    @SuppressWarnings("unchecked")
    public <S extends Property<T>> S onGet(Command<T, T> onGet) {
        this.onGet = onGet;
        return (S) this;
    }

    /**
     * set a {@link Command} that will be executed when {@link #set(Object)} method is invoked
     *
     * @param onSet the {@link Command} that will be executed every time {@link #set(Object)}
     *              method is invoked, it will take the original value passed as a parameter, and it
     *              will return the updated value to be stored in this instance as it's return
     * @param <S>   the sub-class of this {@link Property}
     * @return the sub-class of this {@link Property} to be used for chaining
     */
    @SuppressWarnings("unchecked")
    public <S extends Property<T>> S onSet(Command<T, T> onSet) {
        this.onSet = onSet;
        return (S) this;
    }

    /**
     * set a {@link Command} that will be executed when {@link #set(Object)} method finishes it's
     * invocation and the value is updated
     *
     * @param onUpdate the {@link Command} that will be executed every time {@link #set(Object)}
     *                 method is invoked and finished, it will take the final value updated in this
     *                 instance, notice that this is invoked after the value is updated
     * @param <S>      the sub-class of this {@link Property}
     * @return the sub-class of this {@link Property} to be used for chaining
     */
    @SuppressWarnings("unchecked")
    public <S extends Property<T>> S onUpdate(Command<T, ?> onUpdate) {
        this.onUpdate = onUpdate;
        return (S) this;
    }

    /**
     * return the current {@link Property} as another {@link Property} or any of it's sub-classes,
     * notice that if the passed type has already been created, it will return the
     * created instance
     *
     * @return the expected type holding reference to the same value in this {@link Property}
     */
    public PropertiesBuilder<T> as() {
        return propertiesBuilder;
    }

    @Override
    public boolean isEmpty() {
        return object == null;
    }

    @Override
    @CallSuper
    public void clear() {
        object = null;
        onSet = null;
        onGet = null;
        onUpdate = null;
        if (propertiesBuilder != null) {
            propertiesBuilder.clear();
        }
        propertiesBuilder = null;
    }
}
