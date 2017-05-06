package com.base.presentation.repos.base;

import android.support.annotation.CallSuper;

import com.base.abstraction.annotations.interfaces.Load;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.Executor;
import com.base.abstraction.interfaces.Initializable;
import com.base.usecases.events.ResponseMessage;
import com.base.presentation.repos.json.JsonResponse;

/**
 * the {@link Executor} that is responsible for handling responses in a {@link Repository}
 * <p>
 * Created by Ahmed Adel on 11/24/2016.
 */
@Load
public class RepositoryResponsesHandler<R extends Repository> extends Executor<ResponseMessage>
        implements Initializable<Repository> {

    private R repository;

    protected R getRepository() {
        return repository;
    }


    @Override
    @CallSuper
    @SuppressWarnings("unchecked")
    public void initialize(Repository repository) {
        this.repository = (R) repository;
        for (Command<ResponseMessage, ?> command : commands.values()) {
            if (command instanceof JsonResponse) {
                ((JsonResponse<?>) command).initialize(repository);
            }
        }
    }
}
