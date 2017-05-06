package com.base.abstraction.observer;

/**
 * an interface that holds the removing methods for an Observable pattern
 * <p>
 * Created by Ahmed Adel on 9/19/2016.
 */
public interface ObservableRemove {
    /**
     * remove an Observer from the observers list
     *
     * @param observer the {@link Observer} that was previously added by
     *                 {@link Observable#addObserver(Observer)}
     */
    void removeObserver(Observer observer);
}
