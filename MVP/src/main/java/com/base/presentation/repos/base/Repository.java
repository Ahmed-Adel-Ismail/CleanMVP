package com.base.presentation.repos.base;

import android.support.annotation.NonNull;

import com.base.abstraction.actors.base.Actor;
import com.base.abstraction.events.Message;
import com.base.abstraction.messaging.AbstractMailbox;
import com.base.abstraction.messaging.Mailbox;
import com.base.abstraction.messaging.TriggeredMailbox;
import com.base.abstraction.annotations.interfaces.Address;
import com.base.abstraction.annotations.readers.ClassAnnotationReader;
import com.base.abstraction.annotations.scanners.ClassAnnotationScanner;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.abstraction.commands.executors.Executor;
import com.base.abstraction.commands.executors.generators.ExecutorClassesInitializer;
import com.base.abstraction.events.Event;
import com.base.abstraction.events.EventBuilder;
import com.base.abstraction.exceptions.annotations.AnnotationNotDeclaredException;
import com.base.abstraction.exceptions.failures.Failure;
import com.base.abstraction.failures.UnhandledFailureException;
import com.base.abstraction.interfaces.ClearableParent;
import com.base.abstraction.interfaces.Initializable;
import com.base.abstraction.interfaces.PreInitializer;
import com.base.abstraction.logs.Logger;
import com.base.presentation.annotations.interfaces.Request;
import com.base.presentation.references.FieldsCleaner;
import com.base.abstraction.system.App;
import com.base.abstraction.system.AppResources;
import com.base.abstraction.system.Behaviors;
import com.base.presentation.annotations.interfaces.JsonRequest;
import com.base.presentation.annotations.interfaces.Requester;
import com.base.abstraction.R;
import com.base.usecases.annotations.Mock;
import com.base.usecases.annotations.RequestsHandler;
import com.base.usecases.annotations.ResponsesHandler;
import com.base.usecases.callbacks.Callback;
import com.base.usecases.callbacks.CallbackDispatcher;
import com.base.usecases.events.RequestMessage;
import com.base.usecases.events.ResponseMessage;
import com.base.usecases.requesters.base.EntityGateway;

import java.lang.annotation.Annotation;

/**
 * The Abstract RepositoriesGroup class that holds the basic methods for communication between an
 * Entities {@link com.base.usecases.requesters.base.Requester} class and the {@link Callback} that
 * requests and waits for updates
 * <p>
 * for sub-classes :<br>
 * any sub-class MUST have an empty / default constructor
 * <p>
 * when the call-back class needs any data, a
 * {@link com.base.abstraction.commands.Command} will be executed from the ones
 * created by {@link #createOnRequestCommands()}
 * <p>
 * To request from a data source you will use a {@link EntityGateway}
 * or any similar class
 * <p>
 * when a response is received , a
 * {@link com.base.abstraction.commands.Command} will be executed from the ones
 * created by {@link #createOnResponseCommands()} ... you can then invoke
 * {@link #notifyCallback(Event)} from your sub-class
 * <p>
 * <u>mandatory annotations :</u><br>
 * {@link Address} : the actor address to identify this RepositoriesGroup<br>
 * <p>
 * <u>optional annotations :</u><br>
 * {@link ResponsesHandler} : the {@link Executor} to handle {@link ResponseMessage} events,
 * this {@link Executor} will be put in {@link #createOnResponseCommands()},
 * if you used {@link JsonRequest} annotations on variables ... you can ignore this
 * annotation<br>
 * {@link RequestsHandler} : the {@link Executor} to handle {@link RequestMessage} events,
 * this {@link Executor} will be put in {@link #createOnRequestCommands()},
 * if you used {@link JsonRequest} annotations on variables ... you can ignore this
 * annotation<br>
 * {@link Requester} : make the repository initialize the default requester, this is mandatory when
 * using {@link JsonRequest} or similar annotations<br>
 * {@link JsonRequest} : declare a member variable that it will be retrieved through the declared
 * requester , notice that you should either declare {@link Requester} or
 * override {@link #getEntityGateway()} for this annotation to be active ... these requests
 * will be processed with a {@link com.base.presentation.repos.json.JsonRequest} Object by default, no need to add them again in
 * {@link Executor} used as {@link RequestsHandler} <br>
 * {@link Mock} : make the repository mock response to the given field if application
 * behavior is set to {@link Behaviors#MOCKING}<br>
 * <p>
 * Created by Ahmed Adel on 9/18/2016.
 *
 * @see Address
 * @see RequestsHandler
 * @see ResponsesHandler
 * @see Requester
 * @see JsonRequest
 * @see Mock
 */
public abstract class Repository implements
        Initializable<Callback>,
        PreInitializer,
        Actor,
        com.base.usecases.requesters.base.Requester,
        ClearableParent,
        Callback,
        CallbackDispatcher {

    private final Mailbox<Event> mailbox;
    private final CallbackDispatcher.ConcreteImplementer callbackDispatcher;
    final CommandExecutor<Long, RequestMessage> onRequestCommands;
    final CommandExecutor<Long, ResponseMessage> onResponseCommands;
    private long address = R.id.addressUnknownRepository;
    private EntityGateway entityGateway;
    com.base.presentation.repos.json.JsonRequest jsonRequest;
    private boolean groupedRepository;


    public Repository() {
        this.callbackDispatcher = new CallbackDispatcher.ConcreteImplementer();
        this.mailbox = new TriggeredMailbox<>(getClass().getSimpleName(), mailboxExecutor());
        this.entityGateway = createFromRequesterAnnotation();
        this.preInitialize();

        this.onRequestCommands = new Executor<>();
        this.onResponseCommands = new Executor<>();

        readJsonRequests();


        this.onRequestCommands.putAll(createOnRequestCommands());
        this.onResponseCommands.putAll(createOnResponseCommands());

        this.address = readAddressFromAnnotation();

        App.getInstance().getActorSystem().add(this);
    }

    private EntityGateway createFromRequesterAnnotation() {
        final EntityGatewayInitializer initializer = new EntityGatewayInitializer(this);
        new ClassAnnotationScanner<Requester>(Requester.class) {
            @Override
            protected void onAnnotationFound(Requester annotation) {
                entityGateway = initializer.execute(annotation);
            }
        }.execute(this);
        return entityGateway;
    }


    private long readAddressFromAnnotation() throws AnnotationNotDeclaredException {
        long address = getActorAddress();
        if (R.id.addressUnknownRepository == address) {
            address = readAddressAnnotation();
        }
        return address;
    }

    private long readAddressAnnotation() {
        long address;
        Address annotation = new ClassAnnotationReader<>(Address.class).execute(this);
        if (Address.NULL_VALUE != annotation.value()) {
            address = annotation.value();
        } else if (!Address.NULL_NAME.equals(annotation.name())) {
            address = AppResources.id(annotation.name());
        } else {
            Logger.getInstance().error(getClass(), "must declare value() or name()");
            throw new AnnotationNotDeclaredException(Address.class);
        }
        return address;
    }


    private com.base.usecases.requesters.base.Requester mailboxExecutor() {
        return new com.base.usecases.requesters.base.Requester() {
            @Override
            public Void execute(Event event) {
                if (groupedRepository) {
                    RepositoriesGroup.removeHealingRepository(Repository.this.getClass());
                }

                Message message = event.getMessage();

                if (message instanceof RequestMessage) {
                    onRequestCommands.execute(event.getId(), (RequestMessage) event.getMessage());
                } else if (message instanceof ResponseMessage) {
                    onCallback(event);
                } else {
                    Logger.getInstance().exception(
                            new UnsupportedOperationException("unknown message type"));
                }

                return null;
            }
        };
    }


    @NonNull
    private Void throwUnhandledFailureException() {
        throw new UnhandledFailureException("any request " + Event.class.getSimpleName() +
                " must hold a " + RequestMessage.class.getSimpleName());
    }

    @Override
    public void preInitialize() {
        // template method
    }

    @Override
    public void initialize(Callback callback) {
        callbackDispatcher.setCallback(callback);
        readOtherModelRequestsAnnotations(callback);
    }

    private void readJsonRequests() {

        if (getEntityGateway() != null) {
            new JsonRequestScanner().execute(this);
        } else {
            logReadRequestsErrorAndReturn(JsonRequest.class);
        }

    }

    private void readOtherModelRequestsAnnotations(Object model) {

        if (getEntityGateway() != null) {
            new ModelRequestsAnnotationScanner(this).execute(model);
        } else {
            Logger.getInstance().info(getClass(), "no more fields annotated with"
                    + Request.class.getSimpleName());
        }

    }

    private void logReadRequestsErrorAndReturn(Class<? extends Annotation> klass) {
        Logger.getInstance().error(getClass(), "null @ getEntityGateway() ... ignored @"
                + klass.getSimpleName());
    }

    /**
     * the commands that will be invoked when the when ever a {@link Callback} requests an
     * {@link Event}
     *
     * @return the {@link CommandExecutor} that holds the commands executed when a response is
     * received
     * @deprecated use {@link RequestsHandler} annotation instead of overriding this method
     */
    protected CommandExecutor<Long, RequestMessage> createOnRequestCommands() {
        Executor<RequestMessage> onRequestsExecutor = null;
        try {
            ClassAnnotationReader<RequestsHandler> reader;
            reader = new ClassAnnotationReader<>(RequestsHandler.class);
            Class<?>[] clss = reader.execute(this).value();
            onRequestsExecutor = new ExecutorClassesInitializer(this).execute(clss);
        } catch (Throwable e) {
            Logger.getInstance().info(getClass(), RequestsHandler.class.getSimpleName() + " - " + e);
        }
        return onRequestsExecutor;
    }


    /**
     * the commands that will be invoked when ever a data source responds
     * with an {@link Event}
     *
     * @return the {@link CommandExecutor} that holds the commands executed when a response is
     * received
     * @deprecated use {@link ResponsesHandler} annotation instead of overriding this method
     */
    protected CommandExecutor<Long, ResponseMessage> createOnResponseCommands() {
        Executor<ResponseMessage> onResponsesExecutor = null;
        try {
            ClassAnnotationReader<ResponsesHandler> reader;
            reader = new ClassAnnotationReader<>(ResponsesHandler.class);
            Class<?>[] clss = reader.execute(this).value();
            onResponsesExecutor = new ExecutorClassesInitializer(this).execute(clss);
        } catch (Throwable e) {
            Logger.getInstance().info(getClass(), ResponsesHandler.class.getSimpleName() + " - " + e);
        }
        return onResponsesExecutor;
    }

    @Override
    public final void setCallback(Callback callback) {
        callbackDispatcher.setCallback(callback);
    }

    @Override
    public final Void execute(Event event) {
        mailbox.execute(event);
        return null;
    }

    @Override
    public final void onCallback(Event event) {
        try {
            onResponseCommands.execute(event.getId(), (ResponseMessage) event.getMessage());
        } catch (Failure e) {
            if (groupedRepository) {
                catchFailure(e);
            } else {
                e.execute(new EventBuilder(R.id.onFailureException, mailbox).execute(this));
            }
        }
    }

    private void catchFailure(Failure e) {
        if (!RepositoriesGroup.hasHealingRepository()) {
            RepositoriesGroup.setHealingRepository(getClass());
            e.execute(new EventBuilder(R.id.onFailureException, mailbox).execute(this));
        }
    }

    @Override
    public final void notifyCallback(Event event) {
        callbackDispatcher.notifyCallback(event);
    }


    @Override
    public AbstractMailbox<Event> getMailbox() {
        return mailbox;
    }

    /**
     * get the default {@link EntityGateway} that do requests from server
     *
     * @return the {@link EntityGateway} that does requests from server, or {@code null}
     * if {@link Requester} was not declared, or if the sub-class did not override this
     * method
     */
    EntityGateway getEntityGateway() {
        return entityGateway;
    }

    void setGroupedRepository(boolean groupedRepository) {
        this.groupedRepository = groupedRepository;
    }

    @Override
    public final void clear() {

        RepositoriesGroup.removeHealingRepository(getClass());

        callbackDispatcher.clear();
        onClear();
        onRequestCommands.clear();
        onResponseCommands.clear();
        mailbox.clear();

        if (entityGateway != null) {
            entityGateway.clear();
            entityGateway = null;
        }

        if (jsonRequest != null) {
            jsonRequest.clear();
            jsonRequest = null;
        }

        App.getInstance().getActorSystem().remove(this);
        new FieldsCleaner(true).execute(this);
    }

    @Override
    public void onClear() {
        // template method
    }


    /**
     * @deprecated declare {@link Address} annotation to this instead of overriding this method,
     * overriding this method is accepted only for {@link Repository}
     * classes that are not able to declare Annotations with access to {@link R.id} file as
     * constants
     */
    @Override
    public long getActorAddress() {
        return address;
    }


    @Override
    public int hashCode() {
        return getClass().getName().length();
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null
                && obj instanceof Repository
                && obj.getClass().getName().equals(getClass().getName());
    }


}
