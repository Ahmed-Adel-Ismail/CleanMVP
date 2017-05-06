package com.base.presentation.base.presentation;

import com.base.abstraction.annotations.interfaces.UpdatesHandler;
import com.base.abstraction.events.Event;
import com.base.abstraction.events.Message;
import com.base.abstraction.observer.Updatable;

/**
 * a {@link ViewModelHandler} that is responsible for handling {@link Message} objects passed
 * in {@link Updatable#onUpdate(Event)} ... used through annotation {@link UpdatesHandler}
 * <p>
 * Created by Ahmed Adel on 12/20/2016.
 */
public class ViewModelUpdatesHandler<V extends ViewModel> extends ViewModelHandler<Message, V> {
}
