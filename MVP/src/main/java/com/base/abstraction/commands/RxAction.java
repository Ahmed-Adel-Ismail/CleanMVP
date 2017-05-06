package com.base.abstraction.commands;

import io.reactivex.functions.Action;

/**
 * a Class that uses a {@link Command} as an executor in the {@link Action} interface implementer
 * <p>
 * Created by Ahmed Adel on 12/23/2016.
 */
public abstract class RxAction implements Action, Command<Void, Void> {

    @Override
    public final void run() throws Exception {
        execute(null);
    }

}
