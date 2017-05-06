package com.base.abstraction.exceptions;

import com.base.abstraction.commands.Command;
import com.base.abstraction.logs.Logger;
import com.base.abstraction.system.App;
import com.base.abstraction.system.Behaviors;

/**
 * a class that handles any thrown {@link Throwable}, if the application is in the testing
 * environment, it lets it crash, if it is on the production environment, it just logs
 * the exception and
 * <p>
 * Created by Ahmed Adel on 11/17/2016.
 */
public class TestException implements Command<Throwable, Void> {


    @Override
    public Void execute(Throwable e) {
        Logger.getInstance().exception(e);
        if (App.getInstance().isBehaviorAccepted(Behaviors.TESTING)) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            } else {
                throw new RuntimeException(e);
            }

        }
        return null;
    }
}
