package com.base.presentation.repos.base;

import android.support.annotation.CallSuper;

import com.base.abstraction.annotations.interfaces.Load;
import com.base.abstraction.commands.executors.Executor;
import com.base.abstraction.interfaces.Initializable;
import com.base.usecases.events.RequestMessage;

/**
 * a class that is responsible for processing requests for the {@link Repository}
 * <p>
 * Created by Ahmed Adel on 11/24/2016.
 */
@Load
public class RepositoryRequestsHandler<R extends Repository> extends Executor<RequestMessage>
        implements Initializable<R> {

    private R repository;

    protected R getRepository() {
        return repository;
    }

    @Override
    @CallSuper
    public void initialize(Repository repository) {
        this.repository = (R) repository;
    }
}
