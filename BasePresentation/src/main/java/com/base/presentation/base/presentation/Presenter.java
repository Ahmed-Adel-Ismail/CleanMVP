package com.base.presentation.base.presentation;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;

import com.base.abstraction.annotations.interfaces.Executable;
import com.base.abstraction.annotations.interfaces.ExecutableCommand;
import com.base.abstraction.annotations.interfaces.Load;
import com.base.abstraction.annotations.interfaces.UpdatesHandler;
import com.base.abstraction.annotations.readers.ClassAnnotationReader;
import com.base.abstraction.annotations.scanners.ClassAnnotationScanner;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.CommandExecutorInitializer;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.abstraction.commands.executors.Executor;
import com.base.abstraction.commands.executors.generators.ExecutableCommandsGenerator;
import com.base.abstraction.commands.executors.generators.ExecutableGenerator;
import com.base.abstraction.commands.executors.generators.ExecutorClassesInitializer;
import com.base.abstraction.commands.executors.generators.ExecutorGenerator;
import com.base.abstraction.events.Event;
import com.base.abstraction.events.Message;
import com.base.abstraction.exceptions.annotations.AnnotationNotDeclaredException;
import com.base.abstraction.exceptions.propagated.ThrowableGroup;
import com.base.abstraction.interfaces.Clearable;
import com.base.abstraction.interfaces.Initializable;
import com.base.abstraction.interfaces.PreInitializer;
import com.base.abstraction.logs.Logger;
import com.base.abstraction.observer.EventsManager;
import com.base.abstraction.observer.EventsSubscriber;
import com.base.abstraction.observer.Observable;
import com.base.abstraction.observer.Observer;
import com.base.abstraction.reflections.GenericTypeInitializer;
import com.base.presentation.R;
import com.base.presentation.annotations.interfaces.OnChecksChangedHandler;
import com.base.presentation.annotations.interfaces.OnClickHandler;
import com.base.presentation.annotations.interfaces.OnItemSelectedHandler;
import com.base.presentation.annotations.interfaces.OnNavigationMenuItemClickHandler;
import com.base.presentation.annotations.interfaces.OnTouchesHandler;
import com.base.presentation.annotations.interfaces.Sync;
import com.base.presentation.base.abstracts.features.AbstractActivity;
import com.base.presentation.base.abstracts.features.Feature;
import com.base.presentation.base.abstracts.features.ViewBinder;
import com.base.presentation.base.abstracts.system.ManifestAccessor;
import com.base.presentation.base.abstracts.system.SystemAccessor;
import com.base.presentation.base.commands.LifeCycleValidator;
import com.base.presentation.commands.RequesterCommand;
import com.base.presentation.interfaces.ActivityHosted;
import com.base.presentation.interfaces.DestroyableClient;
import com.base.presentation.interfaces.ModelClient;
import com.base.presentation.listeners.OnCheckedChangedParams;
import com.base.presentation.listeners.OnItemSelectedParam;
import com.base.presentation.listeners.OnTouchParams;
import com.base.presentation.models.Model;
import com.base.presentation.system.ManifestPermissions;
import com.base.presentation.system.SystemServices;
import com.base.usecases.annotations.ResponsesHandler;
import com.base.usecases.events.ResponseMessage;

import java.util.LinkedList;
import java.util.List;

/**
 * the base class for all Presenters all over the Application<br>
 * the role of a Presenter is to get the data from a {@link Model}, and ask the
 * {@link ViewModel} to display these data ... any validations or logic to this data is done
 * in this class, like if the {@code String} value will be displayed in a certain color, the
 * presenter will know this color, and set it in the {@link ViewModel}, then calls
 * {@link ViewModel#invalidateViews()} so it will be drawn with the new color
 * <p/>
 * <u>Presenters should not be assigned to any member variables in your class, they can
 * only subscribe to your {@link ViewBinder} through declaring them as variables annotated
 * with {@link com.base.presentation.annotations.interfaces.Presenter}</u>
 * <p/>
 * to update the views and model you will need to make a {@link PresenterUpdater} and set it
 * through {@link #setUpdater(PresenterUpdater)}
 * <p>
 * <u>optional annotations :</u><br>
 * {@link Executable} : mark the methods that will be invoked in {@link #onUpdate(Event)}<br>
 * {@link ExecutableCommand} : mark the methods that will be invoked in {@link #onUpdate(Event)} ... these methods
 * should return {@link Command} instance<br>
 * {@link OnClickHandler} : the {@link PresenterClicksHandler} that will handle clicks<br>
 * {@link OnTouchesHandler} : the {@link PresenterOnTouchesHandler} that will handle touches<br>
 * {@link OnChecksChangedHandler} : the {@link PresenterCheckChangesHandler} that will handle
 * onCheckedChanged events<br>
 * <p>
 * {@link ResponsesHandler} : to handle responses from the repository<br>
 * {@link Load} : make this class call {@link ViewModel#invalidateViews()} on every
 * {@link R.id#onResume}<br>
 * <p>
 * Created by Ahmed Adel on 8/31/2016.
 *
 * @param <P> the direct sub-class type
 * @param <V> the subclass type of {@link ViewModel}
 * @param <M> the subclass type of {@link Model}
 * @see Executable
 * @see ExecutableCommand
 * @see OnClickHandler
 * @see OnTouchesHandler
 * @see OnChecksChangedHandler
 * @see ResponsesHandler
 * @see Load
 */
public abstract class Presenter<P extends Presenter<P, V, M>, V extends ViewModel, M extends Model>
        implements
        Initializable<V>,
        PresentationUpdater,
        ActivityHosted,
        ModelClient<M>,
        SystemAccessor,
        ManifestAccessor,
        DestroyableClient,
        PreInitializer,
        EventsSubscriber {

    private static final int VIEW_MODEL_GENERIC_ARG_INDEX = 1;
    private final Observable.Implementer observableImpl;
    private final Observable.Implementer observable;
    private final List<RequesterCommand> requesterCommands;

    private Feature<M> feature;
    private V viewModel;
    private Executor<MenuItem> menuItemsClickExecutor;
    private LifeCycleValidator lifeCycleValidator;
    private Command<Event, Void> lifeCycleValidatorOnComplete;
    private CommandExecutor<Long, Message> commandExecutor;
    private CommandExecutor<Long, OnCheckedChangedParams> onChecksChangedExecutor;
    private CommandExecutor<Long, OnItemSelectedParam> onItemSelectedExecutor;
    private CommandExecutor<Long, OnTouchParams> onTouchesExecutor;
    private CommandExecutor<Long, View> onClickCommandExecutor;
    private CommandExecutor<Long, ResponseMessage> onResponseCommands;
    private PresenterUpdater<P, V, M> presenterUpdater;
    private boolean annotatedWithLoaded;


    /**
     * @deprecated use {@link #Presenter()} instead and then call {@link #initialize(ViewModel)}
     */
    @Deprecated
    @SuppressWarnings("unchecked")
    public Presenter(ViewBinder viewBinder) {
        this();
        ViewModel viewModel = new NullViewModel();
        viewModel.initialize(viewBinder);
        initialize((V) viewModel);
    }

    @Deprecated
    public Presenter(V viewModel) {
        this();
        initialize(viewModel);
    }

    public Presenter() {
        this.observableImpl = new Observable.Implementer();
        this.observable = new Observable.Implementer();
        this.requesterCommands = new LinkedList<>();
        this.onChecksChangedExecutor = new Executor<>();
        this.onTouchesExecutor = new Executor<>();
        this.onItemSelectedExecutor = new Executor<>();
        this.viewModel = createViewModelFromGenericArguments();
    }

    @SuppressWarnings("unchecked")
    private V createViewModelFromGenericArguments() {
        try {
            return new GenericTypeInitializer<V>(VIEW_MODEL_GENERIC_ARG_INDEX).execute(getClass());
        } catch (Throwable e) {
            Logger.getInstance().error(getClass(), e.getMessage());
        }
        return null;
    }


    @Override
    @CallSuper
    public void initialize(V viewModel) {
        this.viewModel = viewModel;
        this.feature = viewModel.getFeature();
        this.preInitialize();


        this.menuItemsClickExecutor = createMenuItemsExecutor();
        this.commandExecutor = createMainCommandExecutor();

        this.onChecksChangedExecutor.putAll(createOnChecksChangedExecutor());
        this.onTouchesExecutor.putAll(createOnTouchesExecutor());
        this.onItemSelectedExecutor.putAll(createOnItemSelectedExecutor());

        this.onClickCommandExecutor = initialize(createOnClickCommandExecutor());
        this.onResponseCommands = initialize(createResponseCommands());

        CommandExecutor<Long, Message> commandExecutor = createCommandExecutor();
        if (commandExecutor != null) {
            this.commandExecutor.putAll(commandExecutor);
        }

        this.presenterUpdater = createPresenterUpdater();
        this.lifeCycleValidator = new LifeCycleValidator();
        this.lifeCycleValidatorOnComplete = createViewModelUpdaterOnComplete();

        readLoadedAnnotation();

        if (annotatedWithLoaded) {
            getViewModel().invalidateViews();
        }

        Event event = new Event.Builder(R.id.onCreate).build();
        onUpdate(event);
    }


    private <T> CommandExecutor<Long, T> initialize(CommandExecutor<Long, T> exec) {
        return new CommandExecutorInitializer<Long, T>().execute(exec);
    }

    /**
     * set the {@link PresenterUpdater} that will transfer data between
     * {@link Model} and {@link ViewModel} and refresh data when needed
     *
     * @param updater the {@link PresenterUpdater}
     * @deprecated use {@link Sync} instead of creating {@link PresenterUpdater}
     */
    @SuppressWarnings("unchecked")
    @Deprecated
    protected final void setUpdater(PresenterUpdater<P, V, M> updater) {
        this.presenterUpdater = updater;
        if (updater != null) {
            this.presenterUpdater.initialize((P) this);
        }
    }

    public void preInitialize() {
        // template method
    }


    private CommandExecutor<Long, Message> createMainCommandExecutor() {
        CommandExecutor<Long, Message> cmdExec = new CommandExecutor<>();

        Command<Message, ?> command = createOnClickCommand();
        cmdExec.put((long) R.id.onClick, command);

        command = createOnNavigationItemSelectedCommand();
        cmdExec.put((long) R.id.onNavigationItemSelected, command);

        command = createOnCheckedChangedCommand();
        cmdExec.put((long) R.id.onCheckedChanged, command);

        command = createOnTouchCommand();
        cmdExec.put((long) R.id.onTouch, command);

        command = createOnItemSelectedCommand();
        cmdExec.put((long) R.id.onItemSelected, command);

        command = createOnRepositoryResponseCommand();
        cmdExec.put((long) R.id.onRepositoryResponse, command);

        command = createOnResumeCommand();
        cmdExec.put((long) R.id.onResume, command);

        command = createOnRefreshCommand();
        cmdExec.put((long) R.id.onRefresh, command);

        command = createOnPauseCommand();
        cmdExec.put((long) R.id.onPause, command);

        command = createOnUpdateModelCommand();
        cmdExec.put((long) R.id.onUpdateModel, command);

        command = createOnDestroyCommand();
        cmdExec.put((long) R.id.onDestroy, command);


        return cmdExec;
    }


    @NonNull
    private Command<Message, Void> createOnClickCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                long id = message.getId();
                View view = message.getContent();
                onClickCommandExecutor.execute(id, view);
                return null;
            }
        };
    }

    private Command<Message, Void> createOnNavigationItemSelectedCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                long menuItemId = message.getId();
                MenuItem menuItem = message.getContent();
                menuItemsClickExecutor.execute(menuItemId, menuItem);
                return null;
            }
        };
    }

    private Command<Message, Void> createOnCheckedChangedCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                OnCheckedChangedParams p = message.getContent();
                onChecksChangedExecutor.execute(message.getId(), p);
                return null;
            }
        };
    }

    private Command<Message, Void> createOnTouchCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                OnTouchParams params = message.getContent();
                onTouchesExecutor.execute(message.getId(), params);
                return null;
            }
        };
    }

    private Command<Message, Void> createOnItemSelectedCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                OnItemSelectedParam params = message.getContent();
                onItemSelectedExecutor.execute(message.getId(), params);

                return null;
            }
        };
    }

    @NonNull
    private Command<Message, Void> createOnRepositoryResponseCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                onUpdateViewModel();
                onResponseCommands.execute(message.getId(), (ResponseMessage) message);
                return null;
            }
        };
    }

    private Command<Message, Void> createOnRefreshCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message p) {
                onRefresh();
                return null;
            }
        };
    }

    /**
     * the default implementation for {@link R.id#onRefresh} event
     */
    protected void onRefresh() {
        onUpdateModel();
        onUpdateViewModel();
    }

    private Command<Message, Void> createOnResumeCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message p) {
                onResume();
                return null;
            }
        };
    }

    /**
     * the default implementation for {@link R.id#onResume} event
     */
    protected final void onResume() {
        onUpdateViewModel();
        if (annotatedWithLoaded) {
            getViewModel().invalidateViews();
        }
    }

    @NonNull
    private Command<Message, Void> createOnDestroyCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message p) {
                clear();
                return null;
            }
        };
    }

    private Command<Message, Void> createOnPauseCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message p) {
                onPause();
                return null;
            }
        };
    }

    private Command<Message, Void> createOnUpdateModelCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message p) {
                onUpdateModel();
                return null;
            }
        };
    }

    /**
     * the default implementation for {@link R.id#onPause} event
     */
    protected final void onPause() {
        onUpdateModel();
    }


    /**
     * get the {@link ViewModel} subclass that handles the {@link android.view.View Views}
     * attached to this presenter
     *
     * @return the subclass of the {@link ViewModel}
     */
    public final V getViewModel() {
        return viewModel;
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
    public final void onUpdate(Event event) {
        if (lifeCycleValidator != null) {
            lifeCycleValidator.execute(event).onComplete(lifeCycleValidatorOnComplete);
        }

        commandExecutor.execute(event.getId(), event.getMessage());
        observable.notifyObservers(event);
    }


    @Override
    public long getActorAddress() {
        return feature.getActorAddress();
    }

    /**
     * same as {@link Clearable#clear()} but should not be called from outside
     */
    private void clear() {
        onDestroy();
        if (presenterUpdater != null) {
            presenterUpdater.clear();
            presenterUpdater = null;
        }
        menuItemsClickExecutor.clear();
        onClickCommandExecutor.clear();
        viewModel.clear();
        observableImpl.clear();
        observable.clear();
        onResponseCommands.clear();
        commandExecutor.clear();
        requesterCommands.clear();
        onTouchesExecutor.clear();
        onChecksChangedExecutor.clear();
        onItemSelectedExecutor.clear();

        lifeCycleValidator = null;
        lifeCycleValidatorOnComplete = null;
    }

    @Override
    public final AbstractActivity<M> getHostActivity() {
        return feature.getHostActivity();
    }

    /**
     * get the {@link Feature} hosting this {@link Presenter}
     *
     * @return the {@link Feature} weather it is an {@link AbstractActivity} or an
     * {@link com.base.presentation.base.abstracts.features.AbstractFragment} or any
     * other implementer if found
     */
    public Feature<M> getFeature() {
        return feature;
    }

    @Override
    public final SystemServices getSystemServices() {
        return feature.getSystemServices();
    }

    @Override
    public final ManifestPermissions getManifestPermissions() {
        return feature.getManifestPermissions();
    }

    @NonNull
    @Override
    public final M getModel() {
        return feature.getModel();
    }

    @Override
    public final boolean isDestroyed() {
        return feature == null || feature.isDestroyed();
    }

    private Executor<OnCheckedChangedParams> createOnChecksChangedExecutor() {
        Executor<OnCheckedChangedParams> executor = null;
        try {
            ClassAnnotationReader<OnChecksChangedHandler> reader;
            reader = new ClassAnnotationReader<>(OnChecksChangedHandler.class);
            Class<?>[] classes = reader.execute(this).value();
            executor = new ExecutorClassesInitializer(this).execute(classes);
        } catch (AnnotationNotDeclaredException e) {
            // do nothing
        } catch (Throwable e) {
            Logger.getInstance().exception(e);
        }
        return executor;
    }

    private CommandExecutor<Long, OnTouchParams> createOnTouchesExecutor() {
        Executor<OnTouchParams> executor = null;
        try {
            ClassAnnotationReader<OnTouchesHandler> reader;
            reader = new ClassAnnotationReader<>(OnTouchesHandler.class);
            Class<?>[] classes = reader.execute(this).value();
            executor = new ExecutorClassesInitializer(this).execute(classes);
        } catch (AnnotationNotDeclaredException e) {
            // do nothing
        } catch (Throwable e) {
            Logger.getInstance().exception(e);
        }
        return executor;
    }

    private Executor<OnItemSelectedParam> createOnItemSelectedExecutor() {
        Executor<OnItemSelectedParam> executor = null;
        try {
            ClassAnnotationReader<OnItemSelectedHandler> reader;
            reader = new ClassAnnotationReader<>(OnItemSelectedHandler.class);
            Class<?>[] classes = reader.execute(this).value();
            executor = new ExecutorClassesInitializer(this).execute(classes);
        } catch (AnnotationNotDeclaredException e) {
            // do nothing
        } catch (Throwable e) {
            Logger.getInstance().exception(e);
        }
        return executor;
    }

    /**
     * implement this method if you will handle {@link R.id#onClick} events
     *
     * @return the {@link CommandExecutor} to be used in {@code onClicks}
     * @deprecated use {@link OnClickHandler} annotation instead
     */
    @Deprecated
    protected CommandExecutor<Long, View> createOnClickCommandExecutor() {
        Executor<View> executor = null;
        try {
            ClassAnnotationReader<OnClickHandler> reader;
            reader = new ClassAnnotationReader<>(OnClickHandler.class);
            Class<?>[] classes = reader.execute(this).value();
            executor = new ExecutorClassesInitializer(this).execute(classes);
        } catch (AnnotationNotDeclaredException e) {
            // do nothing
        } catch (Throwable e) {
            Logger.getInstance().exception(e);
        }
        return executor;
    }

    private Executor<MenuItem> createMenuItemsExecutor() {
        Executor<MenuItem> executor = new Executor<>();
        try {
            ClassAnnotationReader<OnNavigationMenuItemClickHandler> reader;
            reader = new ClassAnnotationReader<>(OnNavigationMenuItemClickHandler.class);
            Class<?>[] classes = reader.execute(this).value();
            executor = new ExecutorClassesInitializer(this).execute(classes);
        } catch (AnnotationNotDeclaredException e) {
            // do nothing
        } catch (Throwable e) {
            Logger.getInstance().exception(e);
        }
        return executor;
    }

    /**
     * createNativeMethod the {@link CommandExecutor} that will handle responses received upon
     * {@link R.id#onRepositoryResponse}
     *
     * @return the {@link CommandExecutor} to be used
     * @deprecated use {@link ResponsesHandler} annotation instead
     */
    @Deprecated
    protected CommandExecutor<Long, ResponseMessage> createResponseCommands() {
        Executor<ResponseMessage> executor = null;
        try {
            ClassAnnotationReader<ResponsesHandler> reader;
            reader = new ClassAnnotationReader<>(ResponsesHandler.class);
            Class<?>[] classes = reader.execute(this).value();
            executor = new ExecutorClassesInitializer(this).execute(classes);
        } catch (AnnotationNotDeclaredException e) {
            // do nothing
        } catch (Throwable e) {
            Logger.getInstance().exception(e);
        }
        return executor;
    }

    /**
     * preInitialize the {@link CommandExecutor} that will handle the Actions coming to
     * this presenter, notice that {@link R.id#onDestroy} is handled in {@link #onDestroy()},
     * and not in the normal {@link CommandExecutor}
     *
     * @return the initialized {@link CommandExecutor} with all it's commands
     * @deprecated use {@link UpdatesHandler}, {@link Executable} annotation instead
     */
    @Deprecated
    protected CommandExecutor<Long, Message> createCommandExecutor() {
        Executor<Message> executor = new Executor<>();
        executor = new ExecutableGenerator<>(executor).execute(this);
        executor = new ExecutableCommandsGenerator<>(executor).execute(this);
        executor = new ExecutorGenerator<>(executor).execute(this);
        executor = putAllOnUpdatesExecutors(executor);
        return executor;
    }

    private Executor<Message> putAllOnUpdatesExecutors(final Executor<Message> executor) {
        new ClassAnnotationScanner<UpdatesHandler>(UpdatesHandler.class) {
            @Override
            protected void onAnnotationFound(UpdatesHandler annotation) {
                try {
                    Class<?>[] classes = annotation.value();
                    Executor temporaryExec = new ExecutorClassesInitializer(Presenter.this)
                            .execute(classes);
                    executor.putAll(temporaryExec);
                } catch (Throwable e) {
                    Logger.getInstance().exception(e);
                }
            }
        }.execute(this);
        return executor;
    }


    private PresenterUpdater<P, V, M> createPresenterUpdater() {
        PresenterUpdater<P, V, M> presenterUpdater = new PresenterUpdater<>();
        presenterUpdater.initialize((P) this);
        return presenterUpdater;
    }

    private Command<Event, Void> createViewModelUpdaterOnComplete() {
        return new Command<Event, Void>() {
            @Override
            public Void execute(Event event) {
                if (event != null && viewModel != null) {
                    viewModel.onUpdate(event);
                }
                return null;
            }
        };
    }

    @Override
    public final void onUpdateModel() {
        if (presenterUpdater != null) {
            presenterUpdater.onUpdateModel();
        }
    }

    @Override
    public final void onUpdateViewModel() {
        if (presenterUpdater != null) {
            presenterUpdater.onUpdateViewModel();
        }
    }

    @Override
    public void onDestroy() {
        // template method
    }

    protected final Implementer getObservable() {
        return observable;
    }


    private void readLoadedAnnotation() {
        new ClassAnnotationScanner<Load>(Load.class) {
            @Override
            protected void onAnnotationFound(Load annotation) {
                annotatedWithLoaded = true;
            }
        }.execute(this);
    }

    /**
     * invoke {@link ViewModel#invalidateViews()}
     */
    protected final void invalidateViews() {
        getViewModel().invalidateViews();
    }
}
