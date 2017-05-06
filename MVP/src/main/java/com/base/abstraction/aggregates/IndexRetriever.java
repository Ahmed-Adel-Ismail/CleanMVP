package com.base.abstraction.aggregates;

import android.support.annotation.NonNull;

import com.base.abstraction.commands.Command;
import com.base.abstraction.exceptions.aggregates.InvalidIndexException;
import com.base.abstraction.interfaces.Clearable;

import java.util.Collection;
import java.util.List;

/**
 * a {@link Command} that validates any passed index to any {@link List}, if
 * the index is invalid, it will throw a {@link InvalidIndexException}
 * <p>
 * Created by Ahmed Adel Ismail on 4/24/2017.
 *
 * @param <T> the type of the items stored in the {@link List}
 * @param <R> the expected return type, it should be the same type if you did not change
 *            it's type in {@link #onValidIndexFound(Command)}
 */
public class IndexRetriever<T, R> implements Command<List<T>, R>, Clearable {

    private final int index;
    private Command<T, R> onValidIndexFound;

    /**
     * initialize {@link IndexRetriever} {@link Command}
     *
     * @param index the index of the item to look up for
     */
    public IndexRetriever(int index) {
        this.index = index;
    }

    /**
     * add a {@link Command} to be executed when the index is found to be valid, the return of
     * this {@link Command} will be the return of the {@link #execute(List)} method if
     * the index is valid, this method will map the found result to a different type
     *
     * @param onValidIndex the {@link Command} to be executed
     * @return {@code this} instance for chaining
     */
    public IndexRetriever<T, R> onValidIndexFound(Command<T, R> onValidIndex) {
        this.onValidIndexFound = onValidIndex;
        return this;
    }

    /**
     * invoke the {@link IndexRetriever} Command
     *
     * @param list the {@link List} to look up for
     * @return the valid item
     * @throws InvalidIndexException if the index was not valid
     */
    @SuppressWarnings("unchecked warning")
    @Override
    public R execute(List<T> list) throws InvalidIndexException {
        if (index < 0 || index >= list.size()) {
            return throwInvalidIndexException(list);
        } else if (onValidIndexFound != null) {
            return onValidIndexFound.execute(list.get(index));
        } else {
            return (R) list.get(index);
        }
    }

    @NonNull
    private R throwInvalidIndexException(Collection<?> c) {
        throw new InvalidIndexException("invalid index @ " + c.getClass().getSimpleName());
    }

    @Override
    public void clear() {
        onValidIndexFound = null;
    }
}
