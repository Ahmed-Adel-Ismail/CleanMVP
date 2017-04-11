package com.base.abstraction.system.readers;

import com.base.abstraction.annotations.interfaces.Integration;
import com.base.abstraction.api.usecases.AbstractIntegrationHandler;
import com.base.abstraction.commands.Command;
import com.base.abstraction.reflections.Initializer;
import com.base.abstraction.system.App;

class IntegrationAnnotationReader implements Command<App, Void> {


    @Override
    public Void execute(App app) {
        if (app.getClass().isAnnotationPresent(Integration.class)) {
            doReadAnnotation(app);
        } else {
            throw new UnsupportedOperationException(
                    "no @" + Integration.class.getSimpleName()
                            + " declared, this app wont be able to access entities through " +
                            "an integration layer ... it will only "
            );
        }
        return null;
    }

    private void doReadAnnotation(App app) {
        Integration integration = app.getClass().getAnnotation(Integration.class);
        app.setIntegrationHandler(new Initializer<AbstractIntegrationHandler>()
                .execute(integration.value()));
    }
}
