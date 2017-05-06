package com.base.presentation.base.presentation;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.view.View;

import com.base.abstraction.annotations.interfaces.Executable;
import com.base.abstraction.annotations.interfaces.ExecutableCommand;
import com.base.abstraction.annotations.interfaces.Initializer;
import com.base.abstraction.annotations.interfaces.Load;
import com.base.abstraction.annotations.interfaces.UpdatesHandler;
import com.base.abstraction.annotations.readers.ClassAnnotationReader;
import com.base.abstraction.annotations.scanners.FieldAnnotationScanner;
import com.base.abstraction.annotations.scanners.MethodAnnotationScanner;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.abstraction.commands.executors.Executor;
import com.base.abstraction.commands.executors.generators.ExecutableCommandsGenerator;
import com.base.abstraction.commands.executors.generators.ExecutableGenerator;
import com.base.abstraction.commands.executors.generators.ExecutorClassesInitializer;
import com.base.abstraction.commands.executors.generators.ExecutorGenerator;
import com.base.abstraction.commands.executors.generators.InitializerGenerator;
import com.base.abstraction.events.Event;
import com.base.abstraction.events.Message;
import com.base.abstraction.exceptions.CheckedReferenceClearedException;
import com.base.abstraction.exceptions.TestException;
import com.base.abstraction.interfaces.Clearable;
import com.base.abstraction.interfaces.Initializable;
import com.base.abstraction.interfaces.PreInitializer;
import com.base.abstraction.logs.Logger;
import com.base.abstraction.observer.Updatable;
import com.base.abstraction.references.CheckedReference;
import com.base.presentation.references.FieldsCleaner;
import com.base.presentation.references.Property;
import com.base.abstraction.system.AppResources;
import com.base.abstraction.R;
import com.base.presentation.annotations.interfaces.OnResponse;
import com.base.presentation.annotations.interfaces.Sync;
import com.base.presentation.base.abstracts.features.AbstractActivity;
import com.base.presentation.base.abstracts.features.Feature;
import com.base.presentation.base.abstracts.features.ViewBinder;
import com.base.presentation.base.commands.KeyboardHiderView;
import com.base.presentation.interfaces.ActivityHosted;
import com.base.presentation.interfaces.DestroyableClient;
import com.base.presentation.interfaces.FeatureHosted;
import com.base.presentation.models.Model;
import com.base.presentation.references.Entity;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;

/**
 * an {@link Executor} that is responsible for handling the {@link View} related methods invocation,
 * it should hold all the required data to update the {@link View Views} as reference variables
 * or primitives, and when the Views should be updated, the method {@link #invalidateViews()}
 * should be invoked, hence all the invalidate commands annotated with {@link Executable}
 * or {@link ExecutableCommand} will execute when any of the ids mentioned in the annotation
 * are included in this instance ({@link ExecutableCommand} is not fully implemented yet)
 * <p/>
 * there should be no way for any class to access the Views directly, this class is the only way
 * <p>
 * if you want to divide this class into several sub-classes (based on responsibilities), you
 * can create a {@link ViewModelInvalidationHandler} and add it as a non-private member variable in this
 * class, then annotate it with {@link com.base.presentation.annotations.interfaces.ViewModelExecutor}
 * <p/>
 * <u>optional annotations :</u><br>
 * {@link Executable} : mark the methods that will execute when any of the event ids mentioned in
 * the annotation is triggered<br>
 * {@link OnResponse} : cause a method that is annotated with {@link Executable} or
 * {@link Initializer} to be invoked only after this response is received and available
 * in the {@link Model} class in one of it's {@link Entity} variables<br>
 * {@link ExecutableCommand} : not fully implemented yet<br>
 * {@link Sync} : mark the member variables that will be updated when the {@link PresenterUpdater}
 * takes action<br>
 * {@link UpdatesHandler} : an {@link Executor} that will handle {@link #onUpdate(Event)}
 * invocations<br>
 * {@link Initializer} : mark the methods that will be invoke only once to initialize a
 * {@link View} with this annotation, you should supply the id of this {@link View} as the
 * id for the annotation, this way, this method will invoke only in the first
 * {@link #invalidateViews()}, and then wont be invoked again ... for {@link Presenter} classes
 * that are annotated with {@link Load}, they will cause {@link #invalidateViews()} to be
 * invoked in the first {@code onResume()}, which will cause the {@link Initializer} methods
 * to be invoked in this point only, so take this in consideration when depending on
 * data coming from server in these methods<br>
 * {@link View} id as the
 * <p>
 * Created by Ahmed Adel on 9/18/2016.
 *
 * @see UpdatesHandler
 * @see ExecutableCommand
 * @see Executable
 * @see Initializer
 * @see OnResponse
 * @see Sync
 */
public class ViewModel implements
        Initializable<ViewBinder>,
        Updatable,
        FeatureHosted,
        PreInitializer,
        Clearable,
        ActivityHosted,
        DestroyableClient {


    static final String EMPTY_STRING = "";

    Set<Long> viewsIds;
    Map<Long, WeakReference<View>> viewsReferences;
    Executor<View> invalidateCommands;
    ViewsEntities viewsEntities;

    private KeyboardHiderView keyboardHiderView;
    private Executor<View> initializerCommands;
    private ViewBinder viewBinder;
    private String title;
    private Executor<Message> onUpdatesHandler;
    private boolean destroyed;


    public void preInitialize() {
        // template method
    }

    public ViewModel() {

    }

    @Deprecated
    public ViewModel(ViewBinder viewBinder) {
        initialize(viewBinder);
    }


    @Override
    @CallSuper
    public void initialize(ViewBinder viewBinder) {
        this.viewBinder = viewBinder;
        this.viewsIds = new HashSet<>();
        this.viewsReferences = new HashMap<>();
        this.invalidateCommands = new Executor<>();
        this.initializerCommands = new Executor<>();
        this.keyboardHiderView = new KeyboardHiderView();
        preInitialize();
        this.onUpdatesHandler = createOnUpdatesHandler();
        this.initializerCommands.putAll(createInitializerCommands());
        this.invalidateCommands.putAll(createInvalidateCommands());
        this.viewsEntities = new ViewsEntitiesMapper().execute(this);
        addExecutableViewIds();
        addExecutableCommandsViewIds();
        addAllViewsFromViewBinder();

    }


    private Executor<Message> createOnUpdatesHandler() {
        Executor<Message> onUpdatesHandler = new Executor<>();
        try {
            ClassAnnotationReader<UpdatesHandler> reader;
            reader = new ClassAnnotationReader<>(UpdatesHandler.class);
            Class<?>[] clss = reader.execute(this).value();
            onUpdatesHandler = new ExecutorClassesInitializer(this).execute(clss);
        } catch (Throwable e) {
            Logger.getInstance().info(getClass(), e);
        }
        return onUpdatesHandler;
    }

    private Executor<View> createInitializerCommands() {
        return new InitializerGenerator<>(initializerCommands).execute(this);
    }

    /**
     * @deprecated use {@link Executable} and {@link ExecutableCommand} annotations for methods \
     * that will be used to invalidate the views
     */
    @Deprecated
    protected CommandExecutor<Long, View> createInvalidateCommands() {
        Executor<View> executor = new Executor<>();
        executor = new ExecutableGenerator<>(executor).execute(this);
        executor = new ExecutableCommandsGenerator<>(executor).execute(this);
        executor = new ExecutorGenerator<>(executor).execute(this);
        executor = new ViewModelInvalidationHandlerScanner(executor).execute(this);
        return executor;
    }


    private void addExecutableViewIds() {
        new MethodAnnotationScanner<Executable>(Executable.class) {

            @Override
            protected void onAnnotationFound(Method element, Executable annotation) {
                if (annotation.value().length > 1) {
                    for (long viewId : annotation.value()) {
                        viewsIds.add(viewId);
                    }
                } else {
                    viewsIds.add(annotation.value()[0]);
                }

            }
        }.execute(this);
    }

    private void addExecutableCommandsViewIds() {
        new MethodAnnotationScanner<ExecutableCommand>(ExecutableCommand.class) {

            @Override
            protected void onAnnotationFound(Method element, ExecutableCommand annotation) {
                if (annotation.value().length > 1) {
                    for (long viewId : annotation.value()) {
                        viewsIds.add(viewId);
                    }
                } else {
                    viewsIds.add(annotation.value()[0]);
                }

            }
        }.execute(this);
    }


    /**
     * add all detected views with the ids that are present in {@link #viewsIds}
     */
    final void addAllViewsFromViewBinder() {
        final CheckedReference<ViewBinder> viewBinderRef = new CheckedReference<>(viewBinder);
        new FieldAnnotationScanner<BindView>(BindView.class) {
            @Override
            protected void onAnnotationFound(Field element, BindView annotation) {
                processFieldAnnotations(element, viewBinderRef);
            }
        }.execute(viewBinder);
    }

    private void processFieldAnnotations(
            Field field,
            CheckedReference<ViewBinder> viewBinderRef) {
        try {
            View view = (View) field.get(viewBinderRef.get());
            addView(keyboardHiderView.execute(view));
        } catch (CheckedReferenceClearedException e) {
            Logger.getInstance().exception(e);
        } catch (Throwable e) {
            new TestException().execute(e);
        }
    }


    @SuppressWarnings("unchecked")
    @Override
    public final <T extends Model> Feature<T> getFeature() {
        return viewBinder.getFeature();
    }

    /**
     * adds a {@link View} to the value {@link ViewModel}
     *
     * @param view the {@link View} to add
     */
    public void addView(@NonNull View view) {
        int viewId = view.getId();
        viewsReferences.put((long) viewId, new WeakReference<>(view));
        new AddFocusToViews().execute(view);

    }


    /**
     * invalidate / draw all the {@link View Views} of this {@link ViewModel}
     */
    public void invalidateViews() {
        invalidateCommands.execute((long) R.id.containerView, getFeature().getView());
        for (Map.Entry<Long, WeakReference<View>> entry : viewsReferences.entrySet()) {
            doInvalidateViews(entry);
        }
    }

    private void doInvalidateViews(Map.Entry<Long, WeakReference<View>> entry) {
        View v = entry.getValue().get();
        if (v != null) {
            long viewId = entry.getKey();
            try {
                initializeAndInvalidateView(v, viewId);
            } catch (Throwable e) {
                Logger.getInstance().exception(e);
            }
        }
    }

    private void initializeAndInvalidateView(View v, long viewId) {

        if (viewsEntities.hasEmptyEntities(viewId)) {
            logEntityNotReadyError((int) viewId);
            return;
        }

        boolean initialized;
        Command<View, ?> command = initializerCommands.get(viewId);

        if (initialized = (command != null)) {
            command.execute(v);
        }

        command = invalidateCommands.get(viewId);
        if (command != null) {
            command.execute(v);
        }


        if (initialized) {
            initializerCommands.remove(viewId);
        }
    }


    private void logEntityNotReadyError(int viewId) {
        Logger.getInstance().error(getClass(), "Entity not ready yet for : " +
                AppResources.resourceEntryName(viewId));
    }

    /**
     * invalidate a specific view by it's {@code id} ... this is useful with progress bars
     * where it will need to be invalidated multiple times in a very short time
     *
     * @param viewId the id of the {@code View}
     */
    public void invalidateView(long viewId) {
        invalidateSingleView(invalidateCommands, viewId);
    }

    final void invalidateSingleView(CommandExecutor<Long, View> executor, long viewId) {
        View v;
        WeakReference<View> viewRef = viewsReferences.get(viewId);
        if (viewRef != null && (v = viewRef.get()) != null) {
            try {
                executor.execute(viewId, v);
            } catch (Throwable e) {
                Logger.getInstance().exception(e);
            }
        }
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    /**
     * request one of the views to
     *
     * @param viewId the id of the {@link View} clicked
     */
    public void invokeOnClick(long viewId) {
        View v;
        WeakReference<View> viewRef = viewsReferences.get(viewId);
        if (viewRef != null && (v = viewRef.get()) != null) {
            v.performClick();
        }
    }

    /**
     * notice that this method sets all the member varaibels of the sub-class to {@code null},
     * and clears the {@link Property} variables as well
     */
    @Override
    public void clear() {
        destroyed = true;
        onUpdatesHandler.clear();
        invalidateCommands.clear();
        viewsReferences.clear();
        onDestroy();
        viewsEntities.clear();
        viewBinder = null;
        keyboardHiderView = null;
        new FieldsCleaner().execute(this);
    }

    /**
     * @deprecated you can call {@code super.}{@link #clear()} at the end of your
     * {@link #clear()} method instead of overriding this method
     */
    @Deprecated
    @Override
    public void onDestroy() {
        // template method
    }


    @Override
    public void onUpdate(Event event) throws RuntimeException {
        onUpdatesHandler.execute(event.getId(), event.getMessage());
    }


    @Override
    public <T extends Model> AbstractActivity<T> getHostActivity() {
        return getFeature().getHostActivity();
    }

    @Override
    public boolean isDestroyed() {
        return destroyed;
    }

}
