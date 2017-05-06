package com.base.presentation.repos.base;

import android.support.annotation.NonNull;

import com.base.abstraction.commands.Command;
import com.base.abstraction.events.Event;
import com.base.abstraction.exceptions.failures.Failure;
import com.base.abstraction.interfaces.Clearable;
import com.base.abstraction.interfaces.Initializable;
import com.base.abstraction.logs.Logger;
import com.base.usecases.callbacks.Callback;

import java.util.HashSet;

/**
 * a list of {@link Repository} classes that
 * <p>
 * Created by Ahmed Adel on 1/4/2017.
 */
public class RepositoriesGroup extends HashSet<Repository> implements
        Command<Event, Void>,
        Initializable<Callback>,
        Clearable {

    private volatile static Class<? extends Repository> healingRepository;

    @Override
    public void initialize(Callback callback) {
        for (Repository repository : this) {
            repository.initialize(callback);
            repository.setGroupedRepository(true);
        }
    }

    @Override
    public Void execute(Event event) {

        if (healingRepository == null) {
            executeAllRepositories(event);
        } else {
            Logger.getInstance().error(getClass(), "ignored event : " + event);
            Logger.getInstance().error(getClass(), "a repository is currently healing it's " +
                    "failure : " + healingRepository.getSimpleName());
        }
        return null;
    }

    private void executeAllRepositories(Event event) {
        for (Repository repository : this) {
            repository.execute(event);
        }
    }


    @Override
    public void clear() {
        super.clear();
        for (Repository repository : this) {
            repository.clear();
        }
    }

    /**
     * set a {@link Repository} that is currently facing {@link Failure} and healing itself,
     * if this value is set to a {@link Repository}, this {@link RepositoriesGroup} will be
     * paused until this {@link Repository} is healed ... when this {@link Repository} is healed,
     * it should invoke this method again and set the value to {@code null} for this
     * {@link RepositoriesGroup} to resume receiving updates
     *
     * @param repositoryClass the {@link Repository} class that is currently healing, or
     *                        {@code null} if no {@link Repository} is currently facing issues
     */
    public static void setHealingRepository(Class<? extends Repository> repositoryClass) {
        healingRepository = repositoryClass;
    }


    static boolean hasHealingRepository() {
        boolean result = healingRepository != null;
        if (result) {
            logFailureHandlingCancelled();
        }
        return result;
    }


    private static void logFailureHandlingCancelled() {
        Logger.getInstance().error(RepositoriesGroup.class,
                "currently there is a repository that is " +
                        "healing from failure : " + healingRepository.getSimpleName());
    }

    static void removeHealingRepository(@NonNull Class<? extends Repository> repositoryClass) {
        Class<? extends Repository> currentlyHealingRepository;
        currentlyHealingRepository = healingRepository;
        if (currentlyHealingRepository != null
                && repositoryClass.equals(currentlyHealingRepository)) {
            RepositoriesGroup.setHealingRepository(null);
        }
    }


}
