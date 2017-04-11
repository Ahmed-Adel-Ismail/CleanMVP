package com.base.presentation.base.presentation;

import com.base.presentation.R;
import com.base.presentation.listeners.OnItemSelectedParam;
import com.base.presentation.models.Model;

/**
 * a {@link PresenterHandler} for {@link R.id#onItemSelected} events, it holds methods that will
 * receive {@link OnItemSelectedParam}
 * <p>
 * Created by Ahmed Adel on 1/5/2017.
 */
public class PresenterItemSelectedHandler<
        P extends Presenter<P, V, M>,
        V extends ViewModel,
        M extends Model> extends PresenterHandler<OnItemSelectedParam, P, V, M> {
}
