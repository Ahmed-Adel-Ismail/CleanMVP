package com.base.presentation.views.validators;

import com.base.abstraction.commands.Command;
import com.base.abstraction.interfaces.Clearable;
import com.base.presentation.base.presentation.ValidatorViewModel;

/**
 * A {@link Command} that holds default validation for a View
 * <p/>
 * Created by Ahmed Adel on 10/9/2016.
 */
public abstract class ValidatorCommand<T> implements
        Command<T, Void>,
        Clearable {

    private int viewId;
    private ValidatorViewModel viewModel;

    public ValidatorCommand(int viewId, ValidatorViewModel viewModel) {
        this.viewId = viewId;
        this.viewModel = viewModel;
    }


    /**
     * get the initial {@link android.view.View} id
     *
     * @return the id of the view
     */
    protected final int getViewId() {
        return viewId;
    }

    /**
     * get the {@link ValidatorViewModel} casted to the desired sub-class
     *
     * @param <V> the sub-class type
     * @return the {@link ValidatorViewModel} casted to this type of sub-class
     * @throws ClassCastException if the casting operation failed
     */
    @SuppressWarnings("unchecked")
    public final <V extends ValidatorViewModel> V getViewModel() throws ClassCastException {
        return (V) viewModel;
    }

    /**
     * set the state of the initial {@link android.view.View} weather it is an error or not
     *
     * @param error pass {@code true} if it is an error, else pass {@code false}
     */
    protected final void setError(boolean error) {
        getViewModel().setError(viewId, error);
    }

    @Override
    public void clear() {
        viewModel = null;
    }
}
