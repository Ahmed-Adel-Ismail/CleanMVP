package com.base.abstraction.system.readers;

import com.base.abstraction.commands.Command;
import com.base.abstraction.system.App;

/**
 * a class that reads tha annotations for the {@link App} class
 * <p>
 * Created by Ahmed Adel on 2/13/2017.
 */
public enum AppAnnotationsReader implements Command<App, AppAnnotationsReader> {

    FACTORY {
        @Override
        public AppAnnotationsReader execute(App app) {
            return this;
        }
    },
    BEHAVIOR {
        @Override
        public AppAnnotationsReader execute(App app) {
            new BehaviorAnnotationReader().execute(app);
            return FACTORY;
        }
    },
    INTEGRATION {
        @Override
        public AppAnnotationsReader execute(App app) {
            new IntegrationAnnotationReader().execute(app);
            return FACTORY;
        }
    },
    LOADER {
        @Override
        public AppAnnotationsReader execute(App app) {
            new LoaderAnnotationReader().execute(app);
            return FACTORY;
        }
    },
    ACTOR {
        @Override
        public AppAnnotationsReader execute(App app) {
            new ActorAnnotationReader().execute(app);
            return FACTORY;
        }
    },
    BROADCASTS_EXECUTOR {
        @Override
        public AppAnnotationsReader execute(App app) {
            new BroadcastsAnnotationReader().execute(app);
            return FACTORY;
        }
    };


    @Override
    public abstract AppAnnotationsReader execute(App app);
}
