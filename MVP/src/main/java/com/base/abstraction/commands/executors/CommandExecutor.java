package com.base.abstraction.commands.executors;

import android.support.annotation.CallSuper;

import com.base.abstraction.aggregates.AggregateContainable;
import com.base.abstraction.aggregates.AggregateKeySet;
import com.base.abstraction.aggregates.AggregateRemovable;
import com.base.abstraction.aggregates.KeyAggregate;
import com.base.abstraction.aggregates.KeyAggregateAddable;
import com.base.abstraction.aggregates.KeyAggregateAllAddable;
import com.base.abstraction.aggregates.KeyAggregateGettable;
import com.base.abstraction.commands.Command;
import com.base.abstraction.interfaces.Clearable;
import com.base.abstraction.interfaces.Emptyable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * an Object to execute {@link Command} instances based on the Key found, it is a cleaner way
 * other than making a {@code switch-case} statements
 * <p>
 * Created by Ahmed Adel on 9/1/2016.
 *
 * @param <Key>          the key that will map to a {@link Command} instance
 * @param <CommandParam> the parameter that will be passed to a Command
 * @see Command
 */
public class CommandExecutor<Key, CommandParam>
        implements
        Clearable,
        Emptyable,
        KeyAggregate,
        KeyAggregateGettable<Key, Command<CommandParam, ?>>,
        AggregateKeySet<Key>,
        AggregateRemovable<Void, Key>,
        AggregateContainable<Key>,
        KeyAggregateAllAddable<Void, CommandExecutor<Key, CommandParam>>,
        KeyAggregateAddable<Key, Command<CommandParam, ?>> {

    protected final Map<Key, Command<CommandParam, ?>> commands;

    public CommandExecutor() {
        commands = new LinkedHashMap<>();
    }

    @Override
    public Command<CommandParam, ?> put(Key key, Command<CommandParam, ?> command) {
        return commands.put(key, command);
    }

    @Override
    public Void putAll(CommandExecutor<Key, CommandParam> p) {
        if (p != null && p.commands.size() > 0) {
            commands.putAll(p.commands);
        }
        return null;
    }

    @Override
    public Command<CommandParam, ?> get(Key key) {
        return commands.get(key);
    }

    @Override
    public Void remove(Key key) {
        commands.remove(key);
        return null;
    }

    @Override
    public Set<Key> keySet() {
        return commands.keySet();
    }

    @Override
    public boolean contains(Key object) {
        return commands.containsKey(object);
    }

    @Override
    public boolean isEmpty() {
        return commands.isEmpty();
    }

    @Override
    @CallSuper
    public void clear() {
        commands.clear();
    }


    /**
     * execute the command mapped to the given Key
     *
     * @param key the key that maps to a {@link Command}
     * @param p   the parameters to be passed to this Command
     * @throws RuntimeException an Exception indicating an error in the flow
     */
    public void execute(Key key, CommandParam p) throws RuntimeException {
        Command<CommandParam, ?> targetCommand = commands.get(key);
        if (targetCommand != null) {
            targetCommand.execute(p);
        }
    }


}
