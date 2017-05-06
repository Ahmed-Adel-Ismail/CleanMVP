package com.base.presentation.base.presentation;

import com.base.abstraction.R;
import com.base.presentation.listeners.OnTouchParams;
import com.base.presentation.models.Model;

/**
 * a {@link PresenterHandler} for {@link R.id#onTouch} events, it holds methods that will receive
 * {@link OnTouchParams}
 * <p>
 * Created by Ahmed Adel on 1/5/2017.
 */
public class PresenterOnTouchesHandler<
        P extends Presenter<P, V, M>,
        V extends ViewModel,
        M extends Model> extends PresenterHandler<OnTouchParams, P, V, M> {
}
