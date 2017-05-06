package com.base.presentation.base.presentation;

import com.base.abstraction.annotations.interfaces.Load;
import com.base.abstraction.commands.executors.Executor;
import com.base.abstraction.interfaces.Initializable;

/**
 * a generic {@link Executor} that can be used in {@link ViewModel} classes through
 * annotations
 * <p>
 * Created by Ahmed Adel on 12/20/2016.
 */
@SuppressWarnings("unchecked")
@Load
class ViewModelHandler<T, V extends ViewModel> extends Executor<T>
        implements Initializable<ViewModel> {

    private V viewModel;

    @Override
    public void initialize(ViewModel viewModel) {
        this.viewModel = (V) viewModel;
    }


    protected void invalidateView(long viewId) {
        viewModel.invalidateView(viewId);
    }

    protected void invalidateViews() {
        viewModel.invalidateViews();
    }

    protected final V getViewModel() {
        return viewModel;
    }


}
