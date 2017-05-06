package com.base.abstraction.system.readers;

import com.base.abstraction.annotations.interfaces.ApplicationLoader;
import com.base.abstraction.commands.Command;
import com.base.abstraction.logs.Logger;
import com.base.abstraction.reflections.Initializer;
import com.base.abstraction.system.App;
import com.base.abstraction.system.AppLoader;

class LoaderAnnotationReader implements Command<App, Void> {

    @Override
    public Void execute(App app) {
        if (app.getClass().isAnnotationPresent(ApplicationLoader.class)) {
            readAppLoaderAnnotation(app);
        } else {
            initializeDefaultAppLoader(app);
        }
        return null;
    }


    private void readAppLoaderAnnotation(App app) {
        ApplicationLoader applicationLoader = app.getClass().getAnnotation(ApplicationLoader.class);
        app.setAppLoader(new Initializer<AppLoader>().execute(applicationLoader.value()));
    }

    private void initializeDefaultAppLoader(App app) {
        Logger.getInstance().error(app.getClass(), "no @"
                + ApplicationLoader.class.getSimpleName() + " declared");
        AppLoader appLoader = new AppLoader();
        appLoader.setFirebaseTokenRefreshed(true);
        app.setAppLoader(appLoader);
    }

}
