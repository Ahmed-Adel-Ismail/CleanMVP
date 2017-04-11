package com.base.presentation.models;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.CallSuper;

import com.base.abstraction.annotations.interfaces.Load;
import com.base.abstraction.annotations.interfaces.Retry;
import com.base.abstraction.annotations.interfaces.Save;
import com.base.abstraction.annotations.interfaces.UpdatesHandler;
import com.base.abstraction.annotations.readers.ClassAnnotationReader;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.abstraction.commands.executors.Executor;
import com.base.abstraction.commands.executors.generators.ExecutorClassesInitializer;
import com.base.abstraction.events.Event;
import com.base.abstraction.events.Message;
import com.base.abstraction.exceptions.CheckedReferenceClearedException;
import com.base.abstraction.exceptions.TestException;
import com.base.abstraction.interfaces.ClearableParent;
import com.base.abstraction.interfaces.Initializable;
import com.base.abstraction.interfaces.PreInitializer;
import com.base.abstraction.logs.Logger;
import com.base.abstraction.observer.Observer;
import com.base.abstraction.references.CheckedReference;
import com.base.presentation.references.FieldsCleaner;
import com.base.abstraction.reflections.Initializer;
import com.base.presentation.annotations.interfaces.JsonRequest;
import com.base.presentation.annotations.interfaces.Repository;
import com.base.presentation.annotations.interfaces.Sync;
import com.base.presentation.base.presentation.Presenter;
import com.base.presentation.base.presentation.PresenterUpdater;
import com.base.presentation.references.Entity;
import com.base.presentation.repos.base.RepositoriesGroup;
import com.base.usecases.R;
import com.base.usecases.annotations.ResponsesHandler;
import com.base.usecases.callbacks.Callback;
import com.base.usecases.events.RequestMessage;
import com.base.usecases.events.ResponseMessage;
import com.base.usecases.requesters.base.Requester;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class for Model classes for Features (one Model per Activity),
 * where for every {@code Activity} it represents a feature in the application, and
 * for every Feature there should be a Gateway to Use-Cases layer ... which is this Class
 * <p>
 * you will send events to this class, if your update required a repositories update
 * and when it finishes it's task, it will send an {@link Event} with the id
 * {@link R.id#onRepositoryResponse}
 * <p>
 * since the main clients for this class are the {@link Presenter} classes, remember to
 * invoke {@link Presenter#onUpdateModel()} to update this {@link Model} values
 * <p>
 * for the sub-classes of this class, they will need to invoke
 * {@link #requestFromRepository(long, RequestMessage)} to start a repositories update, and
 * when it finishes, they will need to invoke
 * {@link #notifyOnRepositoryResponse(ResponseMessage)} to
 * notify the presentation layer
 * <p>
 * to serialize / de-serialize this class, you will need to use a {@link ModelExchanger} to copy
 * the {@code transient} members of this parent class
 * <p>
 * for sub-classes, they should supply a no-args constructor
 * <p>
 * <u>mandatory annotations :</u><br>
 * {@link Repository} : the default repository for this model<br>
 * <p>
 * <u>optional annotations :</u><br>
 * {@link UpdatesHandler} to handle updates from presentation layer<br>
 * {@link ResponsesHandler} to handle responses from the repositories<br><br>
 * {@link JsonRequest} : mark the {@link Entity} variables with this annotation so that
 * they are retrieved from a {@link com.base.presentation.repos.base.Repository} when the requestId
 * is triggered ... notice that member variables marked with this annotation will be initialized
 * even if they were left {@code null}, so it is guaranteed that they will not be {@code null}
 * in initialization process (but there {@link Entity#get()} will return {@code null} since they
 * are not set ... also notice that using this annotation will make using {@link UpdatesHandler}
 * and {@link ResponsesHandler} optional, since {@link Entity} classes handle this process<br>
 * {@link Load} mark member variables that will be loaded from {@link Intent} with this
 * annotation, and support the {@code String} resource that is used as key<br>
 * {@link Save} mark member variables that will be saved to {@link Intent} with this
 * annotation, and support the {@code String} resource that is used as key<br>
 * {@link Retry} : cause an {@link Entity} to keep retrying the request if failed, notice that
 * this annotation should not be put on {@link Entity} instances that handles user-input, like
 * login for example<br>
 * {@link Sync} : mark the member variables that will be updated when the {@link PresenterUpdater}
 * takes action<br><br>
 * <p>
 * Created by Ahmed Adel on 9/18/2016.
 *
 * @see Repository
 * @see UpdatesHandler
 * @see ResponsesHandler
 * @see Sync
 * @see JsonRequest
 * @see Load
 * @see Save
 * @see Retry
 */
public abstract class Model implements
        Initializable<Observer>,
        Serializable,
        PreInitializer,
        Observer,
        Cloneable,
        Callback,
        Requester,
        ClearableParent {

    private boolean cleared;
    transient RepositoriesGroup repositories;
    transient CommandExecutor<Long, Message> onViewsUpdatedCommands;
    transient CommandExecutor<Long, ResponseMessage> onRepositoryUpdatedCommands;
    transient List<Entity<?>> entities;

    transient CheckedReference<Observer> viewsReference;

    public Model() {
    }

    /**
     * @deprecated use annotations to initialize the {@link Model} then invoke
     * {@link #initialize(Observer)} instead of this constructor
     */
    @Deprecated
    public Model(Observer observer) {
        initialize(observer);
    }

    @Override
    public void preInitialize() {
        // template method
    }

    @Override
    @CallSuper
    public void initialize(Observer observer) {

        this.viewsReference = new CheckedReference<>(observer);
        this.onViewsUpdatedCommands = new Executor<>();
        this.onRepositoryUpdatedCommands = new Executor<>();
        this.entities = new ArrayList<>();
        this.preInitialize();
        this.repositories = createAllRepositories();
        this.onViewsUpdatedCommands.putAll(createOnViewsUpdatedCommands());
        this.onRepositoryUpdatedCommands.putAll(createOnRepositoryUpdatedCommands());
        new JsonRequestScanner().execute(this);
        this.repositories.initialize(this);

    }


    private RepositoriesGroup createAllRepositories() {
        RepositoriesGroup repositories = new RepositoriesGroup();
        repositories.add(createRepository());
        return repositories;
    }

    /**
     * createNativeMethod the {@link com.base.presentation.repos.base.Repository}
     * that will be used to request data from the data source
     *
     * @return the {@link com.base.presentation.repos.base.Repository} for this {@link Model} Class
     * @deprecated you should mention the the class of the
     * {@link com.base.presentation.repos.base.Repository} in the
     * {@link Repository} annotation
     */
    @Deprecated
    protected com.base.presentation.repos.base.Repository createRepository() {
        com.base.presentation.repos.base.Repository repository = null;
        try {
            ClassAnnotationReader<Repository> reader;
            reader = new ClassAnnotationReader<>(Repository.class);
            repository = new Initializer<com.base.presentation.repos.base.Repository>().execute(reader.execute(this).value());
        } catch (Throwable e) {
            new TestException().execute(e);
        }
        return repository;
    }


    /**
     * create the {@link CommandExecutor} that will execute it's
     * {@link com.base.abstraction.commands.Command} instances when an {@link Event}
     * is received indicating an update to the Presentation layer
     *
     * @return the {@link CommandExecutor} to be used
     * @deprecated you should create a {@link ModelUpdatesHandler} sub-class and
     * add it through {@link UpdatesHandler} annotation instead of overriding this method
     */
    @Deprecated
    protected CommandExecutor<Long, Message> createOnViewsUpdatedCommands() {
        Executor<Message> onViewsUpdateExecutor = new Executor<>();
        try {
            ClassAnnotationReader<UpdatesHandler> reader;
            reader = new ClassAnnotationReader<>(UpdatesHandler.class);
            Class<?>[] clss = reader.execute(this).value();
            onViewsUpdateExecutor = new ExecutorClassesInitializer(this).execute(clss);
        } catch (Throwable e) {
            Logger.getInstance().info(getClass(), e);
        }
        return onViewsUpdateExecutor;
    }


    /**
     * createNativeMethod the {@link CommandExecutor} that will execute it's
     * {@link com.base.abstraction.commands.Command} instances when an {@link Event}
     * is received indicating an update to the {@link com.base.presentation.repos.base.Repository} responded to a previous
     * request or updates itself when requested
     *
     * @return the {@link CommandExecutor} to be used
     * @deprecated you should create a {@link ModelResponsesHandler} sub-class and
     * add it through {@link ResponsesHandler} annotation instead of overriding this method
     */
    @Deprecated
    protected CommandExecutor<Long, ResponseMessage> createOnRepositoryUpdatedCommands() {
        Executor<ResponseMessage> onRepoUpdateExecutor = null;
        try {
            ClassAnnotationReader<ResponsesHandler> reader;
            reader = new ClassAnnotationReader<>(ResponsesHandler.class);
            Class<?>[] clss = reader.execute(this).value();
            onRepoUpdateExecutor = new ExecutorClassesInitializer(this).execute(clss);
        } catch (Throwable e) {
            Logger.getInstance().info(getClass(), e);
        }
        return onRepoUpdateExecutor;
    }


    @SuppressWarnings("deprecation")
    @Override
    public final Void execute(Event event) {
        onUpdate(event);
        return null;
    }

    /**
     * @see #execute(Event)
     * @deprecated used internally only, not part of the interface
     */
    @Deprecated
    @Override
    public final void onUpdate(Event event) throws RuntimeException {
        if (!cleared) {
            invokeOnUpdate(event);
        }
    }

    private void invokeOnUpdate(Event event) {
        onViewsUpdatedCommands.execute(event.getId(), event.getMessage());
    }

    /**
     * @deprecated used internally only, not part of the interface
     */
    @Deprecated
    @Override
    public final void onCallback(Event event) {
        if (!cleared) {
            onRepositoryUpdatedCommands.execute(event.getId(), (ResponseMessage) event.getMessage());
        }
    }

    @Override
    public final void clear() {
        if (cleared) {
            return;
        }
        cleared = true;
        onViewsUpdatedCommands.clear();
        onRepositoryUpdatedCommands.clear();
        onClear();
        for (Entity e : entities) {
            e.clear();
        }
        entities.clear();
        repositories.clear();
        new FieldsCleaner(true).execute(this);
    }

    @Override
    public void onClear() {
        // template method
    }

    /**
     * process a request from the {@link com.base.presentation.repos.base.Repository}
     *
     * @param requestId      the request ID
     * @param requestMessage the message that contains the object to use for processing the request
     */
    public void requestFromRepository(long requestId, RequestMessage requestMessage) {
        if (!cleared) {
            RequestMessage message = createRepositoryRequestMessage(requestId, requestMessage);
            Event.Builder eventBuilder = new Event.Builder(requestId);
            eventBuilder.message(message);
            Event event = eventBuilder.build();
            notifyOnRepositoryRequest(message);
            repositories.execute(event);
        }
    }

    private RequestMessage createRepositoryRequestMessage(long requestId, RequestMessage requestMessage) {
        RequestMessage.Builder builder;
        if (requestMessage != null) {
            builder = requestMessage.copyBuilder().id(requestId);
        } else {
            builder = new RequestMessage.Builder().id(requestId);
        }
        return builder.build();


    }

    /*
    * notify the presentation layer that there is request is fired now
     *
    * */
    private void notifyOnRepositoryRequest(RequestMessage message) {

        if (cleared) {
            return;
        }
        try {
            Observer views = viewsReference.get();
            if (views instanceof Activity) {
                Event event = new Event.Builder(R.id.onRepositoryRequest).message(message)
                        .senderActorAddress(R.id.addressActivity)
                        .build();
                views.onUpdate(event);
            }
        } catch (CheckedReferenceClearedException e) {
            Logger.getInstance().exception(e);
        }

    }

    /**
     * notify the {@link Observer} class responsible for handing the message to the Presentation
     * layer, to update them selves with a new copy of this class,
     * you should call this method to guarantee that the copy sent to the Observers wont be changed
     * when this instance is updated ... as this method just sends a {@code clone} to this instance
     * at the time of it's invocation
     */
    public void notifyOnRepositoryResponse(ResponseMessage responseMessage) {
        if (!cleared) {
            try {
                notifyPresentationObserver(responseMessage);
            } catch (CheckedReferenceClearedException e) {
                Logger.getInstance().exception(e);
            } catch (Throwable e) {
                new TestException().execute(e);
            }

        }

    }

    private void notifyPresentationObserver(ResponseMessage responseMessage)
            throws CheckedReferenceClearedException {
        Event event = new Event.Builder(R.id.onRepositoryResponse).message(responseMessage).build();
        viewsReference.get().onUpdate(event);
    }


    @Override
    public long getActorAddress() {
        return R.id.addressModel;
    }

    /**
     * get the {@link Entity} instances declared to this {@link Model}
     *
     * @return the group of {@link Entity} objects declared if available
     */
    public List<Entity<?>> getEntities() {
        return entities;
    }
}
