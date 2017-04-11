package com.base.presentation.commands;

import com.base.abstraction.commands.Command;
import com.base.abstraction.events.Event;
import com.base.abstraction.exceptions.CheckedReferenceClearedException;
import com.base.abstraction.exceptions.propagated.ThrowableGroup;
import com.base.abstraction.interfaces.Clearable;
import com.base.abstraction.logs.Logger;
import com.base.abstraction.observer.Observable;
import com.base.abstraction.observer.Observer;
import com.base.abstraction.references.CheckedReference;
import com.base.presentation.R;
import com.base.usecases.events.ResponseMessage;

/**
 * A {@link Command} used by {@link RequestBasedCommand} ... it is responsible for making a
 * request if required by a {@link RequestBasedCommand} and maintaining the state of this request
 * until it is done
 * <br>
 * you will need to override {@link #request()} where this method starts a request
 * <br>
 * optionally you can {@code Override} {@link #onResponse(ResponseMessage)} if you want to
 * handle something when the response is received
 * <p/>
 * Created by Ahmed Adel on 10/4/2016.
 */
public abstract class RequesterCommand implements
        Command<ResponseMessage, Void>,
        Clearable,
        Observable,
        Observer {

    private final Observable.Implementer observableImp = new Observable.Implementer();
    private boolean cleared;
    private boolean successfulResponse;
    private boolean requesting;
    private long requestId;
    private CheckedReference<Observable> hostObservableRef;

    protected RequesterCommand(Observable hostObservable, long requestId) {
        this.requestId = requestId;
        this.hostObservableRef = new CheckedReference<>(hostObservable);
        hostObservable.addObserver(this);
    }

    @Override
    public final Void execute(ResponseMessage p) {
        requesting = false;
        successfulResponse = p.isSuccessful();
        if (!cleared) {
            onResponse(p);
        }
        return null;
    }

    @Override
    public void onUpdate(Event event) throws RuntimeException {
        if (event.getId() == R.id.onRepositoryResponse
                && event.getMessage().getId() == requestId) {
            execute((ResponseMessage) event.getMessage());
        }
    }

    /**
     * this method is invoked when a response is received for the request
     *
     * @param message a {@link ResponseMessage}
     */
    private Void onResponse(ResponseMessage message) {
        Event event = new Event.Builder(R.id.onRequesterCommandEvent)
                .message(message).build();
        notifyObservers(event);
        return null;
    }

    /**
     * check if there is a request that is currently being made ...
     * used only by {@link RequestBasedCommand}
     *
     * @return {@code true} if this instance is busy processing a request, else {@code false}
     */
    final boolean isRequesting() {
        return requesting;
    }

    /**
     * check if the response received is successful or not ...
     * used only by {@link RequestBasedCommand}
     *
     * @return {@code true} if the response is successful (which means that no requests will be
     * done again), else {@code false}
     */
    boolean isSuccessfulResponse() {
        return successfulResponse;
    }

    /**
     * start a new request ... it is safe to invoke this method multiple times as it
     * will handle it's state ... it wont do a request when there is a successful one
     * already
     * <p/>
     * you may invoke this method to start a new request manually, but this is not recommended
     */
    public final void startRequest() {
        if (cleared || requesting || isSuccessfulResponse()) {
            return;
        }
        successfulResponse = false;
        requesting = request();
    }

    /**
     * start a new request, this method is only triggered only if there is no previous successful
     * requests have been made, and there is no request is currently processing as well, if a
     * request is successful once, this method wont be triggered again
     *
     * @return {@code true} if the request is requested.
     */
    protected abstract boolean request();

    @Override
    public void addObserver(Observer observer) {
        observableImp.addObserver(observer);
    }

    @Override
    public void notifyObservers(Event event) throws RuntimeException {
        observableImp.notifyObservers(event);
    }

    @Override
    public void removeObserver(Observer observer) {
        observableImp.removeObserver(observer);
    }

    @Override
    public void onMultipleExceptionsThrown(ThrowableGroup throwableGroup) {
        observableImp.onMultipleExceptionsThrown(throwableGroup);
    }

    @Override
    public void clear() {
        cleared = true;
        observableImp.clear();
        try {
            hostObservableRef.get().removeObserver(this);
        } catch (CheckedReferenceClearedException e) {
            Logger.getInstance().exception(e);
        }

    }
}
