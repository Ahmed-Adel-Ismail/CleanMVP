package com.base.presentation.base.presentation;

import android.support.annotation.CallSuper;

import com.base.abstraction.actors.base.ActorAddressee;
import com.base.abstraction.annotations.interfaces.Load;
import com.base.abstraction.commands.executors.Executor;
import com.base.abstraction.events.Event;
import com.base.abstraction.interfaces.Initializable;
import com.base.presentation.base.abstracts.features.AbstractActivity;
import com.base.presentation.base.abstracts.features.Feature;
import com.base.presentation.interfaces.ActivityHosted;
import com.base.presentation.models.Model;

/**
 * the base class for {@link Executor} classes for {@link Presenter Presenters}
 * <p>
 * Created by Ahmed Adel on 11/24/2016.
 */
@SuppressWarnings("unchecked")
@Load
class PresenterHandler<
        T,
        P extends Presenter<P, V, M>,
        V extends ViewModel,
        M extends Model>
        extends Executor<T>
        implements
        Initializable<Presenter<?, ?, ?>>,
        ActivityHosted,
        ActorAddressee {

    private V viewModel;
    private M model;
    private P presenter;

    @Override
    @CallSuper
    public void initialize(Presenter<?, ?, ?> presenter) {
        this.presenter = (P) presenter;
        this.viewModel = (V) presenter.getViewModel();
        this.model = (M) presenter.getModel();

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

    protected final void notifyObservers(Event event) {
        presenter.notifyObservers(event);
    }

    protected final void onUpdate(Event event) {
        presenter.onUpdate(event);
    }

    /**
     * invoke {@link ViewModel#invalidateViews()}
     */
    protected final void invalidateViews() {
        getViewModel().invalidateViews();
    }

    /**
     * get the {@link AbstractActivity} for the {@link Presenter}
     *
     * @return the {@link AbstractActivity}
     */
    public AbstractActivity<M> getHostActivity() {
        return presenter.getHostActivity();
    }

    /**
     * get the {@link Feature} that is hosting the current {@link Presenter}
     *
     * @return the {@link Feature} that hosts the current presenter
     */
    protected Feature<M> getFeature() {
        return presenter.getFeature();
    }

    /**
     * invoke {@link Presenter#updateViewModel()}
     */
    protected void updateViewModel() {
        presenter.updateViewModel();
    }

    /**
     * invoke {@link Presenter#updateModel()}
     */
    protected void updateModel() {
        presenter.updateModel();
    }


    @Override
    @CallSuper
    public void clear() {
        super.clear();
        model = null;
        viewModel = null;
    }

    @Override
    public long getActorAddress() {
        return presenter.getActorAddress();
    }
}
