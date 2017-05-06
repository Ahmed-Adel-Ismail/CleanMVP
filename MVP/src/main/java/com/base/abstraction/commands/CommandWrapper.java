package com.base.abstraction.commands;

import com.base.abstraction.interfaces.Clearable;
import com.base.abstraction.logs.Logger;

/**
 * a class that acts as a wrapper class around another command {@link Command}, it is stored in
 * a normal member variable, if your {@link Command} is a {@link android.content.Context} or should
 * not be referred to by other classes other than it's owner, use {@link WeakCommandWrapper} instead
 * <p>
 * Created by Ahmed Adel on 11/6/2016.
 */
public class CommandWrapper<Parameter, Return> implements
        Clearable,
        Command<Parameter, Return> {

    private Command<Parameter, Return> command;

    /**
     * initialize an empty {@link CommandWrapper}, to make use of this Object, you should
     * call {@link #setCommand(Command)}
     */
    public CommandWrapper() {
    }

    /**
     * create a {@link CommandWrapper} with the passed {@link Command}, you can change it through
     * invoking {@link #setCommand(Command)}
     *
     * @param command the {@link Command} to be executed
     */
    public CommandWrapper(Command<Parameter, Return> command) {
        this.command = command;
    }


    @Override
    public Return execute(Parameter p) {
        return (command != null) ? command.execute(p) : logExecutionAndReturnNull();
    }

    private Return logExecutionAndReturnNull() {
        Logger.getInstance().error(getClass(), "no command to be execute");
        return null;
    }

    public void setCommand(Command<Parameter, Return> command) {
        this.command = command;
    }

    @Override
    public void clear() {
        command = null;
    }
}
