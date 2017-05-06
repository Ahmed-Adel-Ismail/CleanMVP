package com.base.abstraction.commands;

import com.base.abstraction.exceptions.CheckedReferenceClearedException;
import com.base.abstraction.logs.Logger;
import com.base.abstraction.references.CheckedReference;

/**
 * a class that acts as a wrapper class around another command {@link Command}, it is stored in
 * a {@link CheckedReference}
 * <p>
 * Created by Ahmed Adel on 10/27/2016.
 */
public class WeakCommandWrapper<Parameter, Return> implements Command<Parameter, Return> {

    private final CheckedReference<Command<Parameter, Return>> commandReference;

    public WeakCommandWrapper(Command<Parameter, Return> command) {
        this.commandReference = new CheckedReference<>(command);
    }

    @Override
    public Return execute(Parameter p) {
        try {
            return commandReference.get().execute(p);
        } catch (CheckedReferenceClearedException e) {
            Logger.getInstance().exception(e);
        }
        return null;
    }

    public void setCommand(Command<Parameter, Return> command) {
        commandReference.set(command);
    }

}
