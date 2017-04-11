package com.base.presentation.base.abstracts.features;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.base.abstraction.annotations.readers.ClassAnnotationReader;
import com.base.abstraction.annotations.scanners.FieldAnnotationScanner;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.abstraction.events.Event;
import com.base.abstraction.events.Message;
import com.base.abstraction.exceptions.TestException;
import com.base.abstraction.exceptions.annotations.AnnotationNotDeclaredException;
import com.base.abstraction.exceptions.propagated.ThrowableGroup;
import com.base.abstraction.interfaces.Clearable;
import com.base.abstraction.interfaces.Initializable;
import com.base.abstraction.logs.Logger;
import com.base.abstraction.observer.EventsManager;
import com.base.abstraction.observer.EventsSubscriber;
import com.base.abstraction.observer.Observable;
import com.base.abstraction.observer.Observer;
import com.base.presentation.references.FieldsCleaner;
import com.base.abstraction.reflections.FieldInitializer;
import com.base.abstraction.system.App;
import com.base.abstraction.system.AppResources;
import com.base.presentation.R;
import com.base.presentation.annotations.interfaces.BindLayout;
import com.base.presentation.base.presentation.Presenter;
import com.base.presentation.base.presentation.ViewModel;
import com.base.presentation.interfaces.ActivityHosted;
import com.base.presentation.interfaces.ContentViewable;
import com.base.presentation.interfaces.DestroyableClient;
import com.base.presentation.interfaces.FeatureHosted;
import com.base.presentation.models.Model;

import java.lang.reflect.Field;

import butterknife.ButterKnife;

/**
 * The Implementer class to {@link AbstractActivity} & {@link AbstractFragment},
 * it's responsibility is to locate the Views on the xml layout, and
 * add it to the {@link Presenter Presenters} and there {@link ViewModel View Models}
 * <p>
 * you should annotate this class with {@link BindLayout} to be able to attach an xml layout to
 * this class
 * <p/>
 * * <u>mandatory annotations :</u><br>
 * {@link BindLayout} : the xml layout that will be displayed as the content view <br>
 * {@link com.base.presentation.annotations.interfaces.Presenter Presenter} : the
 * {@link Presenter} member variables that are attached to this instance
 * <p>
 * Created by Ahmed Adel on 9/4/2016.
 *
 * @see BindLayout
 * @see {@link com.base.presentation.annotations.interfaces.Presenter Presenter}
 */
@SuppressWarnings("unchecked")
public abstract class ViewBinder implements
        Initializable<Feature<?>>,
        ViewBindersParent,
        FeatureHosted,
        ActivityHosted,
        ContentViewable,
        DestroyableClient,
        EventsManager,
        Clearable {

    private Feature<?> feature;
    private final Observable.Implementer observableImpl = new Observable.Implementer();
    private final CommandExecutor<Long, Message> commandExecutor = new CommandExecutor<>();


    public ViewBinder() {

    }

    @Deprecated
    public ViewBinder(Feature<?> feature) {
        initialize(feature);
    }

    @Override
    public void initialize(Feature<?> feature) {
        this.feature = feature;
    }

    @Override
    public final void onUpdate(Event event) throws RuntimeException {

        ThrowableGroup throwableGroup = new ThrowableGroup();

        if (event.getId() == R.id.onCreate) {
            invokeOnCreate(event, throwableGroup);
        } else {
            invokeOnUpdate(event, throwableGroup);
        }

        if (!throwableGroup.isEmpty()) {
            onMultipleExceptionsThrown(throwableGroup);
        }
    }

    @Override
    public int getContentView() {
        int layoutId = 0;
        try {
            layoutId = readLayoutId();
        } catch (AnnotationNotDeclaredException e) {
            new TestException().execute(e);
        }
        return layoutId;
    }

    private int readLayoutId() {
        int layoutId;
        BindLayout annotation = new ClassAnnotationReader<>(BindLayout.class).execute(this);
        if (BindLayout.NULL_VALUE != annotation.value()) {
            layoutId = annotation.value();
        } else if (!BindLayout.NULL_NAME.equals(annotation.name())) {
            layoutId = AppResources.layout(annotation.name());
        } else {
            Logger.getInstance().error(getClass(), "must declare value() or name() in " +
                    "@" + BindLayout.class.getSimpleName());
            throw new AnnotationNotDeclaredException(BindLayout.class);
        }
        return layoutId;
    }


    private void invokeOnCreate(Event event, ThrowableGroup throwableGroup) {
        try {
            onCreate(event);
        } catch (RuntimeException exception) {
            throwableGroup.add(exception);
        }
    }

    private void onCreate(Event event) {
        new KeyboardWatcher().execute(feature);
        Bundle savedInstanceState = event.getMessage().getContent();
        ButterKnife.bind(this, getHostActivity());
        initializePresenters().execute(this);
        initializeAfterBindingViews(savedInstanceState);
    }


    @Override
    public void initializeAfterBindingViews(Bundle savedInstanceState) {
        // template method
    }

    private void invokeOnUpdate(Event event, ThrowableGroup throwableGroup) {
        if (isFinishingLifeCycle(event)) {
            invokeOnUpdateForEventsManagerBeforeSelf(event, throwableGroup);
        } else {
            invokeOnUpdateForSelfBeforeEventsManager(event, throwableGroup);
        }
    }

    private boolean isFinishingLifeCycle(Event event) {
        long eventId = event.getId();
        return eventId == R.id.onPause || eventId == R.id.onStop || eventId == R.id.onDestroy;
    }

    private void invokeOnUpdateForEventsManagerBeforeSelf(Event event, ThrowableGroup throwableGroup) {
        invokeEventsManagerOnUpdate(event, throwableGroup);
        invokeSelfOnUpdate(event, throwableGroup);
    }

    private void invokeOnUpdateForSelfBeforeEventsManager(Event event, ThrowableGroup throwableGroup) {
        invokeSelfOnUpdate(event, throwableGroup);
        invokeEventsManagerOnUpdate(event, throwableGroup);
    }

    private void invokeSelfOnUpdate(Event event, ThrowableGroup throwableGroup) {
        try {
            if (event.getId() == R.id.startActivityAction && feature instanceof Activity) {
                getHostActivity().startActivityActionRequest(event);
            } else if (event.getId() == R.id.onDestroy) {
                onDestroy();
            } else if (event.getId() == R.id.showToast) {
                showToast(event);
            } else {
                if (event.getId() == R.id.onResume) {
                    App.getInstance().getActorSystem().add(feature);
                }
                commandExecutor.execute(event.getId(), event.getMessage());
            }
        } catch (RuntimeException exception) {
            throwableGroup.add(exception);
        }
    }

    private void invokeEventsManagerOnUpdate(Event event, ThrowableGroup throwableGroup) {
        try {
            notifyObservers(event);
        } catch (RuntimeException exception) {
            throwableGroup.add(exception);
        }
    }

    private void showToast(Event event) {
        int id = event.getMessage().getContent();
        String text = AppResources.string(id);
        Toast.makeText(getHostActivity(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public final void notifyObservers(Event event) {
        observableImpl.notifyObservers(event);
    }

    @Override
    public final void addObserver(Observer observer) {
        observableImpl.addObserver(observer);
    }

    @Override
    public final void removeObserver(Observer observer) {
        observableImpl.removeObserver(observer);
    }

    @Override
    public void onMultipleExceptionsThrown(ThrowableGroup throwableGroup) {
        observableImpl.onMultipleExceptionsThrown(throwableGroup);
    }

    @Override
    public final void clear() {
        App.getInstance().getActorSystem().remove(feature);
        commandExecutor.clear();
        observableImpl.clear();
        feature = null;
    }

    @Override
    public final void addEventsSubscriber(EventsSubscriber eventsSubscriber) {
        addObserver(eventsSubscriber);
        eventsSubscriber.addObserver(this);
    }

    @Override
    public final void removeEventsSubscriber(EventsSubscriber eventsSubscriber) {
        removeObserver(eventsSubscriber);
        eventsSubscriber.removeObserver(this);
    }

    /**
     * add a {@link Command} to be executed when {@link #onUpdate(Event)} is invoked,
     * notice that {@link R.id#onCreate} is handled in by the super class and
     * {@link R.id#onDestroy} is handled in {@link #onDestroy()} ... and not
     * through this {@link CommandExecutor}
     *
     * @param updateId the ID of the update
     * @param command  the {@link Command} to be executed
     * @deprecated do not add any {@link Command} to the {@link ViewBinder},
     * this should be handled by a {@link Presenter}
     */
    protected final void addCommand(Long updateId, Command<Message, ?> command) {
        commandExecutor.put(updateId, command);
    }


    @Override
    public final <T extends Model> AbstractActivity<T> getHostActivity() {
        return ((Feature<T>) feature).getHostActivity();
    }

    /**
     * get the initial {@link Feature} implementer that is passed to this
     * {@link ViewBinder}
     *
     * @return the {@link Feature} to be used, this can be Activity or a Fragment
     */
    @Override
    public final <T extends Model> Feature<T> getFeature() {
        return (Feature<T>) feature;
    }

    @Override
    public long getActorAddress() {
        return feature.getActorAddress();
    }

    @Override
    public final boolean isDestroyed() {
        return feature == null || feature.isDestroyed();
    }

    @NonNull
    private FieldAnnotationScanner<com.base.presentation.annotations.interfaces.Presenter> initializePresenters() {
        return new FieldAnnotationScanner<com.base.presentation.annotations.interfaces.Presenter>(com.base.presentation.annotations.interfaces.Presenter.class) {
            @Override
            protected void onAnnotationFound(Field element, com.base.presentation.annotations.interfaces.Presenter annotation) {
                Presenter presenter;
                presenter = new FieldInitializer<Presenter>(ViewBinder.this).execute(element);
                ViewModel viewModel = presenter.getViewModel();
                viewModel.initialize(ViewBinder.this);
                presenter.initialize(viewModel);
                addEventsSubscriber(presenter);
            }
        };
    }

    @Override
    @CallSuper
    public void onDestroy() {
        new FieldsCleaner().execute(this);
    }

}
