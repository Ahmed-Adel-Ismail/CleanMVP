package com.base.abstraction.actors.registries;

import android.content.Context;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.LongSparseArray;

import com.base.abstraction.actors.base.Actor;
import com.base.abstraction.messaging.AbstractMailbox;
import com.base.abstraction.messaging.Mailbox;
import com.base.abstraction.aggregates.AggregateAddable;
import com.base.abstraction.aggregates.AggregateRemovable;
import com.base.abstraction.aggregates.KeyAggregateGettable;
import com.base.abstraction.events.Event;
import com.base.abstraction.events.Event.Builder;
import com.base.abstraction.events.Message;
import com.base.abstraction.logs.Logger;
import com.base.abstraction.system.AppResources;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * a class that manages {@link ActorsAddresses} and the addresses for the {@link Actor Actors}
 * <p>
 * Created by Ahmed Adel on 10/27/2016.
 */
public class ActorSystem implements
        AggregateAddable<Actor, Actor>,
        KeyAggregateGettable<Long, AbstractMailbox<Event>>,
        AggregateRemovable<Boolean, Actor> {

    private static final long MAIN_THREAD_ID = Looper.getMainLooper().getThread().getId();

    private final NullActor nullActor;
    private final ActorsAddresses concreteRegistry;
    private final ActorsAddresses weakRegistry;
    private final LongSparseArray<ActorsAddresses> actorsTypes;
    private final Map<Long, Long> actorsByBackgroundThreadsIds;
    private final Map<Long, Long> threadsByActorsAddresses;

    public ActorSystem() {
        this.nullActor = new NullActor();
        this.concreteRegistry = new ConcreteActorsAddresses();
        this.weakRegistry = new WeakActorsAddresses();
        this.actorsTypes = new LongSparseArray<>();
        this.actorsByBackgroundThreadsIds = new HashMap<>();
        this.threadsByActorsAddresses = new HashMap<>();
    }

    /**
     * add an {@link Actor} to an address that may keep live in memory unless it is set to
     * {@code null} ... notice that {@link Context} classes are added in {@link WeakReference}
     * by default
     *
     * @param actor the {@link Actor} to be available
     * @return the added {@link Actor}
     */
    public Actor add(Actor actor) {

        long threadId = MAIN_THREAD_ID;

        if (actor == null) {
            actor = nullActor;
        } else {
            threadId = actor.getMailbox().getThreadId();
        }

        if (threadId != MAIN_THREAD_ID) {
            mapActorToBackgroundThread(actor.getActorAddress(), threadId);
        }


        if (actor instanceof Context || actor instanceof Fragment) {
            return weakAdd(actor);
        } else {
            return concreteAdd(actor);
        }
    }

    private void mapActorToBackgroundThread(long actorAddress, long threadId) {
        actorsByBackgroundThreadsIds.put(threadId, actorAddress);
        threadsByActorsAddresses.put(actorAddress, threadId);
    }


    /**
     * add an {@link Actor} to an address that will be garbage collected as soon as no other class
     * refers to it, it will be stored in a {@link WeakReference}... this method
     * is intended for {@link Context} related classes
     *
     * @param actor the {@link Actor} to be available
     * @return the added {@link Actor}
     */
    private Actor weakAdd(Actor actor) {
        long address = actor.getActorAddress();
        actorsTypes.put(address, weakRegistry);
        return weakRegistry.put(address, actor);
    }

    private Actor concreteAdd(Actor actor) {
        long address = actor.getActorAddress();
        actorsTypes.put(address, concreteRegistry);
        return concreteRegistry.put(address, actor);
    }

    @Override
    public Boolean remove(Actor actor) {
        if (actor == null) {
            return false;
        }
        long address = actor.getActorAddress();
        ActorsAddresses addresses = actorsTypes.get(address);
        boolean sameInstanceInMemory = addresses != null && actor == get(address);
        if (sameInstanceInMemory) {
            doRemoveActor(address, addresses);
        }
        return sameInstanceInMemory;
    }

    private void doRemoveActor(long address, ActorsAddresses addresses) {
        addresses.put(address, nullActor);
        Long threadId = threadsByActorsAddresses.get(address);
        if (threadId != null) {
            unmapActorToBackgroundThread(address, threadId);
        }
    }

    private void unmapActorToBackgroundThread(long address, long threadId) {
        threadsByActorsAddresses.remove(address);
        actorsByBackgroundThreadsIds.remove(threadId);
    }

    /**
     * get the {@link Mailbox} attached to the {@link Actor} so that you can call it's
     * {@link Mailbox#execute(Object)}
     *
     * @param address the address of the {@link Actor}
     * @return it's {@link Mailbox}
     * @deprecated use {@link #send(Event)} instead
     */
    @NonNull
    @Override
    @Deprecated
    public AbstractMailbox<Event> get(Long address) {
        ActorsAddresses registry = actorsTypes.get(address);
        if (registry == null) {
            return nullActor.getMailbox();
        } else {
            return retrieveOrRemoveIfNull(address, registry);
        }
    }

    private AbstractMailbox<Event> retrieveOrRemoveIfNull(Long address, ActorsAddresses registry) {
        AbstractMailbox<Event> mailbox = registry.get(address);
        if (mailbox != null) {
            return mailbox;
        } else {
            doRemoveActor(address, registry);
            return nullActor.getMailbox();
        }
    }

    /**
     * get the {@link Actor} {@link Mailbox} based on the {@link Thread background Thread} passed
     *
     * @param thread a background {@link Thread} that hosts an {@link Actor}
     * @return the {@link Actor} that runs on this background {@link Thread}, or
     * {@link NullActor} if non is available
     */
    public AbstractMailbox<Event> getByBackgroundThread(@NonNull Thread thread) {

        long threadId = thread.getId();
        if (threadId == MAIN_THREAD_ID) {
            return throwUnsupportedOperationException();
        }

        Long actorAddress = actorsByBackgroundThreadsIds.get(threadId);
        if (actorAddress != null) {
            return get(actorAddress);
        } else {
            return nullActor.getMailbox();
        }
    }

    private Mailbox<Event> throwUnsupportedOperationException() {
        throw new UnsupportedOperationException("getByBackgroundThread() : the thread passed " +
                "to parameter is the main thread, this method supports background threads" +
                "only");
    }

    /**
     * send a message to a the {@link Actor} addresses set in
     * {@link Builder#receiverActorAddresses(long...)}
     *
     * @param event the {@link Event} that holds the {@link Message}, you should setVariable
     *              {@link Builder#receiverActorAddresses(long...)} to know the destination of
     *              the message
     * @throws UnsupportedOperationException if no actors addresses were set in
     *                                       {@link Builder#receiverActorAddresses(long...)}
     */
    public void send(Event event) {
        long[] receiversAddresses = event.getReceiverActorAddresses();
        if (receiversAddresses != null && receiversAddresses.length != 0) {
            sendToAddresses(event, receiversAddresses);
        } else {
            throw new UnsupportedOperationException("you should set at least one receiver actor");

        }
    }

    private void sendToAddresses(Event event, long[] receiversAddresses) {
        for (long receiverAddress : receiversAddresses) {
            try {
                get(receiverAddress).execute(event);
            } catch (NullPointerException e) {
                logInspectedBugForActor(receiverAddress, e);
            } catch (Throwable e) {
                Logger.getInstance().exception(e);
            }
        }
    }

    private void logInspectedBugForActor(long receiverAddress, NullPointerException e) {
        Logger.getInstance().error(getClass(), "inspected bug @ " +
                AppResources.resourceEntryName((int) receiverAddress));
        Logger.getInstance().exception(e);
    }

}
