package com.base.presentation.base.abstracts.system;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;

import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.abstraction.events.Event;
import com.base.abstraction.events.Message;
import com.base.abstraction.exceptions.propagated.ThrowableGroup;
import com.base.abstraction.interfaces.ClearableParent;
import com.base.abstraction.observer.EventsSubscriber;
import com.base.abstraction.observer.Observable;
import com.base.abstraction.observer.Observer;
import com.base.abstraction.R;
import com.base.presentation.base.abstracts.features.AbstractActivity;
import com.base.presentation.base.abstracts.features.Feature;
import com.base.presentation.base.abstracts.features.ViewBinder;
import com.base.presentation.base.presentation.Presenter;

/**
 * The parent class for All {@link BroadcastReceiver} classes that will joint sending Events to
 * it's Observers (like {@link ViewBinder} and {@link Presenter} classes), this Class is usually
 * initialized in the {@link ViewBinder} class, and it should be added through
 * <p/>
 * Created by Ahmed Adel on 9/15/2016.
 */
public abstract class AbstractReceiver extends BroadcastReceiver implements
        ClearableParent,
        EventsSubscriber {

    private IntentFilter intentFilter;
    private Feature<?> feature;
    private final Observable.Implementer observable = new Observable.Implementer();
    private final CommandExecutor<Long, Message> commandExecutor;

    public AbstractReceiver(Feature<?> feature, String action) {
        this.feature = feature;
        intentFilter = new IntentFilter(action);
        commandExecutor = createCommandExecutor();
    }

    private CommandExecutor<Long, Message> createCommandExecutor() {
        CommandExecutor<Long, Message> commandExecutor = new CommandExecutor<>();
        Command<Message, Void> command = createOnResumeCommand();
        commandExecutor.put((long) R.id.onResume, command);
        command = createOnPauseCommand();
        commandExecutor.put((long) R.id.onPause, command);
        command = createOnDestroyCommand();
        commandExecutor.put((long) R.id.onDestroy, command);
        return commandExecutor;
    }

    private Command<Message, Void> createOnDestroyCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message p) {
                onClear();
                intentFilter = null;
                feature = null;
                observable.clear();
                commandExecutor.clear();
                return null;
            }
        };
    }

    private Command<Message, Void> createOnPauseCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message p) {
                onUnregister();
                feature.getHostActivity().unregisterReceiver(AbstractReceiver.this);
                return null;
            }
        };
    }

    /**
     * override this method if you want to do something directly before the call to
     * {@link AbstractActivity#unregisterReceiver(BroadcastReceiver)}
     */
    protected void onUnregister() {
        // do nothing;
    }

    private Command<Message, Void> createOnResumeCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message p) {
                feature.getHostActivity().registerReceiver(AbstractReceiver.this, intentFilter);
                onRegister();
                return null;
            }
        };
    }

    /**
     * override this method if you want to do something just after the call to
     * {@link AbstractActivity#registerReceiver(BroadcastReceiver, IntentFilter)}
     */
    protected void onRegister() {
        // do nothing
    }


    @Override
    public final void onUpdate(Event event) throws RuntimeException {
        commandExecutor.execute(event.getId(), event.getMessage());
        if (event.getId() == R.id.onDestroy) {
            clear();
        }
    }

    public final void clear() {
        commandExecutor.clear();
        observable.clear();
        feature = null;
        intentFilter = null;
    }

    @Override
    public final void notifyObservers(Event event) throws RuntimeException {
        observable.notifyObservers(event);
    }

    @Override
    public final void addObserver(Observer observer) {
        observable.addObserver(observer);
    }

    @Override
    public final void removeObserver(Observer observer) {
        observable.removeObserver(observer);
    }

    @Override
    public final void onMultipleExceptionsThrown(ThrowableGroup throwableGroup) {
        observable.onMultipleExceptionsThrown(throwableGroup);
    }

    public Feature<?> getFeature() {
        return feature;
    }

    @Override
    public long getActorAddress() {
        return R.id.addressBroadcastReceiver;
    }
}
