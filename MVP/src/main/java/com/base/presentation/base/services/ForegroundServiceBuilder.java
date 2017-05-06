package com.base.presentation.base.services;

import android.app.Notification;

import com.base.abstraction.annotations.readers.ClassAnnotationReader;
import com.base.abstraction.commands.Command;
import com.base.abstraction.exceptions.TestException;
import com.base.abstraction.exceptions.annotations.AnnotationNotDeclaredException;
import com.base.presentation.annotations.interfaces.Foreground;
import com.base.presentation.annotations.interfaces.Id;

/**
 * a class that builds a {@link Notification} to be shown by the {@link AbstractService} when it is
 * set as foreground service, the subclasses of this class are used in {@link Foreground} annotation
 * value
 * <p>
 * <u>mandatory annotations :</u><br>
 * {@link Id} : the Id of the notification that will be created<br>
 * Created by Ahmed Adel on 1/17/2017.
 */
public abstract class ForegroundServiceBuilder implements Command<AbstractService<?>, Notification> {

    private long id;

    public ForegroundServiceBuilder() {
        try {
            id = new ClassAnnotationReader<>(Id.class).execute(this).value();
        } catch (AnnotationNotDeclaredException e) {
            new TestException().execute(e);
        }
    }

    public final long getId() {
        return id;
    }

    @Override
    public abstract Notification execute(AbstractService<?> service);
}
