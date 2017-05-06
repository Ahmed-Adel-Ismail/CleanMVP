package io.reactivex.properties;


import io.reactivex.annotations.NonNull;
import io.reactivex.properties.exceptions.StateIsMovingToNullException;

/**
 * a {@link Property} that holds a {@link SwitchableState}, this Object does not accept {@code null}
 * values
 * <p>
 * Created by Ahmed Adel on 12/29/2016.
 */
public class State<T extends SwitchableState<T>> extends Property<T> implements SwitchableState<T> {

    State() {

    }

    public State(@NonNull T object) {
        super(object);
    }

    @Override
    public T set(@NonNull T object) {
        super.set(object);
        return object;
    }

    @NonNull
    @Override
    public T get() {
        return super.get();
    }

    @Override
    public T back() {
        T state = get().back();
        if (state != null) {
            set(state);
        } else {
            throw new StateIsMovingToNullException();
        }
        return get();
    }


    @Override
    public T next() {
        T state = get().next();
        if (state != null) {
            set(state);
        } else {
            throw new StateIsMovingToNullException();
        }
        return get();
    }


}
