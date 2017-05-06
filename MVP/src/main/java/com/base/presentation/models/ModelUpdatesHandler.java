package com.base.presentation.models;

import android.support.annotation.CallSuper;

import com.base.abstraction.annotations.interfaces.Load;
import com.base.abstraction.commands.executors.Executor;
import com.base.abstraction.events.Message;
import com.base.abstraction.interfaces.Initializable;
import com.base.usecases.events.RequestMessage;

/**
 * a helper class for the {@link Model}, it is responsible for handling updates from the
 * presentation layer, for any  sub-class, they should provide no-args / default constructor
 * <p>
 * for sub-classes, you can access the model through {@link #getModel()}
 * <p>
 * Created by Ahmed Adel on 11/23/2016.
 *
 * @param <M> the type of the {@link Model} that uses this {@link Executor}
 */
@Load
@SuppressWarnings("unchecked")
public class ModelUpdatesHandler<M extends Model> extends Executor<Message>
        implements Initializable<M> {

    private M model;


    @Override
    @CallSuper
    public void initialize(Model model) {
        this.model = (M) model;
    }

    protected final M getModel() {
        return model;
    }

    /**
     * same as {@link Model#requestFromRepository(long, RequestMessage)}
     *
     * @param requestId      the request id
     * @param requestMessage the {@link RequestMessage}
     */
    protected final void requestFromRepository(long requestId, RequestMessage requestMessage) {
        model.requestFromRepository(requestId, requestMessage);
    }

    @Override
    @CallSuper
    public void clear() {
        super.clear();
        model = null;
    }
}
