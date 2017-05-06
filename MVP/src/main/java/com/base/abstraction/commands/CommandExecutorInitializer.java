package com.base.abstraction.commands;

import com.base.abstraction.commands.executors.CommandExecutor;

/**
 * a class to initialize the {@link CommandExecutor}, it guarantees that it will return an instance,
 * and not a {@code null} value
 * <p>
 * Created by Ahmed Adel on 11/22/2016.
 */
public class CommandExecutorInitializer<Key, CommandParam> implements
        Command<CommandExecutor<Key, CommandParam>, CommandExecutor<Key, CommandParam>> {

    @Override
    public CommandExecutor<Key, CommandParam> execute(CommandExecutor<Key, CommandParam> commandExecutor) {

        return commandExecutor != null ? commandExecutor : new CommandExecutor<Key, CommandParam>();
    }
}
