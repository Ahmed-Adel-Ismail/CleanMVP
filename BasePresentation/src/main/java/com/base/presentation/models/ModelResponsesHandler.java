package com.base.presentation.models;

import android.support.annotation.CallSuper;

import com.base.abstraction.annotations.interfaces.Load;
import com.base.abstraction.commands.executors.Executor;
import com.base.abstraction.interfaces.Initializable;
import com.base.presentation.repos.base.Repository;
import com.base.usecases.events.RequestMessage;
import com.base.usecases.events.ResponseMessage;

/**
 * a helper class for the {@link Model}, it is responsible for handling updates from the
 * {@link Repository}, for any
 * sub-class, they should provide no-args / default constructor
 * <p>
 * for sub-classes, you can access the {@link Model} through {@link #getModel()}
 * <p>
 * Created by Ahmed Adel on 11/23/2016.
 *
 * @param <M> the type of the {@link Model} that uses this {@link Executor}
 */
@SuppressWarnings("unchecked")
@Load
public class ModelResponsesHandler<M extends Model> extends
        Executor<ResponseMessage> implements Initializable<M> {

    private M model;

    @Override
    @CallSuper
    public final void initialize(Model model) {
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
    protected void requestFromRepository(long requestId, RequestMessage requestMessage) {
        model.requestFromRepository(requestId, requestMessage);
    }

    /**
     * same as {@link Model#notifyOnRepositoryResponse(ResponseMessage)}
     *
     * @param responseMessage the {@link ResponseMessage}
     */
    public final void notifyOnRepositoryResponse(ResponseMessage responseMessage) {
        model.notifyOnRepositoryResponse(responseMessage);
    }

    @Override
    @CallSuper
    public void clear() {
        super.clear();
        model = null;
    }
}
