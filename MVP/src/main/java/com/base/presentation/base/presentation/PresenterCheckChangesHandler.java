package com.base.presentation.base.presentation;

import com.base.abstraction.R;
import com.base.presentation.listeners.OnCheckedChangedParams;
import com.base.presentation.models.Model;

/**
 * a {@link PresenterHandler} for {@link R.id#onCheckedChanged} events, it holds methods that will
 * receive {@link OnCheckedChangedParams}
 * <p>
 * Created by Ahmed Adel on 1/5/2017.
 */
public class PresenterCheckChangesHandler<
        P extends Presenter<P, V, M>,
        V extends ViewModel,
        M extends Model> extends PresenterHandler<OnCheckedChangedParams, P, V, M> {
}
