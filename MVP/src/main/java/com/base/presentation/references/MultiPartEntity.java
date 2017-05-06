package com.base.presentation.references;

import android.support.annotation.NonNull;

import com.base.abstraction.commands.Command;
import com.base.usecases.events.ResponseMessage;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * an {@link Entity} that cannot be requested before the request of several
 * {@link Entity} instances before it, you should invoke {@link #addDependency(Entity)} to
 * link those dependencies to this {@link Entity}, you should add them in ascending order, where
 * the first {@link Entity} to invoke it's {@link Entity#request()} should be the first one to
 * be added, and the second one next to, and so on
 * <p>
 * also do not invoke {@link Entity#onComplete(Command)} on any of the {@link Entity} objects that
 * will be added through {@link #addDependency(Entity)}, since this class invokes this method to
 * chain {@link Entity#request()} calls until it reaches it's own call
 * <p>
 * notice also that in case of failure, the failed request ID will be notifies back (and not
 * this {@link Entity#getRequestId()} ... so you may need to handle all the request Ids that
 * are included as dependencies in this Object
 * <p>
 * Created by Ahmed Adel on 1/4/2017.
 *
 * @param <T> the type of the value stored in this Entity
 */
public class MultiPartEntity<T> extends Entity<T> {

    private final LinkedList<Entity<?>> dependencies = new LinkedList<>();
    private Command<ResponseMessage, Void> onComplete;


    public MultiPartEntity() {
        onComplete = dependencyOnComplete();

    }

    @NonNull
    private Command<ResponseMessage, Void> dependencyOnComplete() {
        return new Command<ResponseMessage, Void>() {
            @Override
            public Void execute(ResponseMessage responseMessage) {
                if (responseMessage.isSuccessful()) {
                    MultiPartEntity.this.request();
                }
                return null;
            }
        };
    }

    /**
     * add an {@link Entity} that should invoke it's {@link Entity#request()} before invoking
     * self's {@link #request()} ... if the passed value has it's {@link Entity#isRequestRequired()}
     * with {@code false}, it's {@link Entity#request()} is not invoked again
     *
     * @param entity the {@link Entity} that should be requested before current request,
     *               {@link MultiPartEntity} will set this {@link Entity#onComplete(Command)}
     *               method, so do not set this method for the passed {@link Entity}
     * @return {@code this} instance for chaining
     */
    public MultiPartEntity<T> addDependency(Entity<?> entity) {
        entity.onComplete(onComplete);
        dependencies.add(entity);
        return this;
    }


    @Override
    public boolean isRequestRequired() {
        for (Entity dependency : dependencies) {
            if (dependency.isRequestRequired()) {
                return true;
            }
        }
        return super.isRequestRequired();
    }


    @Override
    public void request(Serializable messageContent) {
        for (Entity dependency : dependencies) {
            if (dependency.requestIfRequired()) {
                return;
            }
        }
        super.request(messageContent);
    }

    @Override
    public void clear() {
        super.clear();
        dependencies.clear();
        onComplete = null;
    }
}
