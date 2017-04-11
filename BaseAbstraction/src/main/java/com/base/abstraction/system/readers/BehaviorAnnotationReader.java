package com.base.abstraction.system.readers;

import com.base.abstraction.annotations.interfaces.Behavior;
import com.base.abstraction.commands.Command;
import com.base.abstraction.logs.Logger;
import com.base.abstraction.system.App;
import com.base.abstraction.system.Behaviors;

class BehaviorAnnotationReader implements Command<App, Void> {


    @Override
    public Void execute(App app) {
        if (app.getClass().isAnnotationPresent(Behavior.class)) {
            readBehaviorAnnotation(app);
        } else {
            initializeDefaultBehavior(app);
        }
        return null;
    }

    private void readBehaviorAnnotation(App app) {
        Behavior behaviorAnnotation = app.getClass().getAnnotation(Behavior.class);
        app.setBehavior(behaviorAnnotation.value());
    }

    private void initializeDefaultBehavior(App app) {
        app.setBehavior(Behaviors.DEBUGGING);
        Logger.getInstance().error(app.getClass(), "no @" + Behavior.class.getSimpleName()
                + " declared, default is : " + Behaviors.DEBUGGING);
    }
}
