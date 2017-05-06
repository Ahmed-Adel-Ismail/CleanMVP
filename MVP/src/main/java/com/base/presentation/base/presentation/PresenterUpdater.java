package com.base.presentation.base.presentation;

import android.support.annotation.CallSuper;

import com.base.abstraction.interfaces.ClearableParent;
import com.base.presentation.annotations.interfaces.Sync;
import com.base.presentation.models.Model;

/**
 * A class to update values in {@link ViewModel} from the {@link Model} when
 * {@link #updateViewModel()} is called, and it also updates the {@link Model} from
 * {@link ViewModel} when {@link #updateModel()} is invoked
 * <p>
 * for sub-classes, they can {@code Override} {@link #updateModel()} and
 * {@link #updateViewModel()} to manage updating data on both sides
 * <p>
 * Created by Ahmed Adel on 10/17/2016.
 *
 * @deprecated use {@link Sync} in your {@link ViewModel} and {@link Model} instead of creating
 * instances of this class
 */
@Deprecated
public class PresenterUpdater<
        P extends Presenter<P, V, M>,
        V extends ViewModel,
        M extends Model>
        implements
        PresentationUpdater,
        ClearableParent {

    private final SyncTransactionsGroup syncTransactionsGroup = new SyncTransactionsGroup();

    private V viewModel;
    private M model;
    private P presenter;


    final void initialize(P presenter) {
        this.presenter = presenter;
        this.viewModel = presenter.getViewModel();
        this.model = presenter.getModel();
        this.syncTransactionsGroup.initialize(this);
    }

    protected final P getPresenter() {
        return presenter;
    }

    protected final M getModel() {
        return model;
    }

    protected final V getViewModel() {
        return viewModel;
    }

    /**
     * use {@link Sync} instead of adding a {@link PresentationUpdater}
     */
    @Override
    @Deprecated
    public void updateModel() {
        syncTransactionsGroup.updateModel();
    }

    @Override
    @Deprecated
    public void updateViewModel() {
        syncTransactionsGroup.updateViewModel();
    }

    @Override
    @CallSuper
    public final void clear() {
        onClear();
        syncTransactionsGroup.clear();
        model = null;
        viewModel = null;
    }

    @Override
    public void onClear() {
        // template method
    }
}
