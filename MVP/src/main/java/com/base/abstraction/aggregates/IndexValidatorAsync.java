package com.base.abstraction.aggregates;

import android.support.annotation.NonNull;

import com.base.abstraction.commands.Command;
import com.base.abstraction.concurrency.ExecutionThread;
import com.base.abstraction.concurrency.Future;
import com.base.abstraction.concurrency.FutureTask;
import com.base.abstraction.exceptions.aggregates.InvalidIndexException;

import java.util.Collection;

/**
 * a {@link Command} that validates any passed index to any {@link Collection}, it takes a
 * {@link FutureTask} that will either invoke it's {@link Future#onComplete(Command)} with
 * the passed index, or pass an
 * {@link InvalidIndexException} to it's {@link Future#onException(Command)}
 * <p>
 * Created by Ahmed Adel Ismail on 4/24/2017.
 */
public class IndexValidatorAsync implements Command<Collection<?>, Future<Integer>> {

    private final int index;

    public IndexValidatorAsync(int index) {
        this.index = index;
    }

    @Override
    public Future<Integer> execute(Collection<?> c) {
        Future<Integer> future = new Future<>();
        if (index < 0 || index >= c.size()) {
            future.setException(invalidIndexException(c));
        } else {
            future.setResult(index);
        }
        return future;
    }

    @NonNull
    private InvalidIndexException invalidIndexException(Collection<?> c) {
        return new InvalidIndexException("invalid index @ " + c.getClass().getSimpleName());
    }
}
