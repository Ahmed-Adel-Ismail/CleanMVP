package com.base.presentation.models;

import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.params.Args2;

/**
 * A class responsible for copying the non-serializable members of the old {@link Model}
 * to the new {@link Model}
 * <p/>
 * Created by Ahmed Adel on 10/16/2016.
 */
public class ModelExchanger<T extends Model> implements Command<Args2<T, T>, T> {

    /**
     * copy the {@code transient} members from the old {@link Model} to the new {@link Model}
     *
     * @param args2 the {@link Args2} that will hold the old {@link Model} in it's
     *             {@link Args2#first()} and the new {@link Model} in {@link Args2#second()}
     *             argument
     * @return the new {@link Model} after being updated
     */
    @Override
    public T execute(Args2<T, T> args2) {
        T oldModel = args2.first();
        T newModel = args2.second();
        newModel.initialize(oldModel.viewsReference.get());
        return newModel;
    }
}
