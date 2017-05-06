package com.base.presentation.base.presentation;

import com.base.presentation.models.Model;
import com.base.usecases.annotations.ResponsesHandler;
import com.base.usecases.events.ResponseMessage;

/**
 * a {@link PresenterHandler} that handles the responses from repository, you can assign it
 * to a {@link Presenter} through {@link ResponsesHandler}
 * annotation
 * <p>
 * Created by Ahmed Adel on 11/29/2016.
 */
public class PresenterResponsesHandler<
        P extends Presenter<P, V, M>,
        V extends ViewModel,
        M extends Model>
        extends PresenterHandler<ResponseMessage, P, V, M> {
}
