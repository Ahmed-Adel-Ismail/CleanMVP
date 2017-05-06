package com.base.abstraction.observer;

import com.base.abstraction.events.Event;
import com.base.abstraction.exceptions.propagated.ThrowableGroup;
import com.base.abstraction.interfaces.Clearable;
import com.base.abstraction.interfaces.Equalable;

import java.util.LinkedList;

/**
 * implement this interface if your class will play the role of an observable
 * <p/>
 * Created by Ahmed Adel on 8/30/2016.
 */
public interface Observable extends
        ObservableAdd,
        ObservableRemove,
        ObservableNotify,
        MultipleExceptionable {


    /**
     * This class provides default implementation for the {@link Observable}
     * interface that deals with {@link Observer} objects, simply when
     * implementing the {@link Observable} interface, invoke the methods of this
     * class in the interface's methods, and it will do all the work (Strategy)
     */
    class Implementer implements Observable, Clearable {

        private final MultipleExceptionable.Implementer multiExceptionsImplementer;
        private LinkedList<Observer> observers = null;
        private boolean clearing;

        {
            multiExceptionsImplementer = new MultipleExceptionable.Implementer();
        }

        @Override
        public final void addObserver(Observer observer) {
            if (observer == null) {
                return;
            }
            if (observers == null) {
                observers = new LinkedList<>();
            } else {
                for (Observer o : observers) {
                    if (observer.equals(o) || isEqualable(observer)) {
                        return;
                    }
                }
            }
            observers.add(observer);
        }

        private boolean isEqualable(Observer o) {
            return (o instanceof Equalable && ((Equalable) o).isEqual(o));
        }

        @Override
        public final void removeObserver(Observer observer) {
            if (observers != null && observers.contains(observer)) {
                observers.remove(observer);
            }
        }

        @Override
        public final void notifyObservers(Event event) throws RuntimeException {
            if (observers != null) {
                updateObservers(event);
            }
        }

        private void updateObservers(Event event) throws RuntimeException {
            LinkedList<Observer> temp = new LinkedList<>(observers);
            ThrowableGroup throwableGroup = new ThrowableGroup();
            for (Observer observer : temp) {
                if (clearing) {
                    throwableGroup.clear();
                    return;
                }
                try {
                    if (event.isConsumable(observer.getActorAddress())) {
                        observer.onUpdate(event);
                    }
                } catch (Throwable e) {
                    throwableGroup.add(e);
                }
            }
            temp.clear();

            if (!throwableGroup.isEmpty()) {
                onMultipleExceptionsThrown(throwableGroup);
            }
        }


        @Override
        public final void onMultipleExceptionsThrown(ThrowableGroup throwableGroup) {
            multiExceptionsImplementer.onMultipleExceptionsThrown(throwableGroup);
        }

        @Override
        public void clear() {
            if (observers != null) {
                clearing = true;
                observers.clear();
                clearing = false;
            }
        }
    }
}

