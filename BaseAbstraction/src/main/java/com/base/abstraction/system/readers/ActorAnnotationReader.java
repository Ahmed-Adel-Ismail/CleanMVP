package com.base.abstraction.system.readers;

import com.base.abstraction.annotations.interfaces.Actor;
import com.base.abstraction.annotations.scanners.FieldAnnotationScanner;
import com.base.abstraction.commands.Command;
import com.base.abstraction.exceptions.TestException;
import com.base.abstraction.system.App;

import java.lang.reflect.Field;

class ActorAnnotationReader implements Command<App, Void> {


    @Override
    public Void execute(final App app) {
        new FieldAnnotationScanner<Actor>(Actor.class) {
            @Override
            protected void onAnnotationFound(
                    Field element,
                    Actor annotation) {
                try {
                    processActorAnnotationOnField(app, element);
                } catch (Throwable e) {
                    new TestException().execute(e);
                }
            }
        }.execute(app);
        return null;
    }

    @SuppressWarnings("unchecked")
    private void processActorAnnotationOnField(App app, Field element)
            throws
            NoSuchMethodException,
            IllegalAccessException,
            InstantiationException,
            ClassCastException {

        Class<? extends com.base.abstraction.actors.base.Actor> actor =
                (Class<? extends com.base.abstraction.actors.base.Actor>) element.getType();
        actor.getDeclaredConstructor().setAccessible(true);
        app.getActorSystem().add(actor.newInstance());
    }

}
