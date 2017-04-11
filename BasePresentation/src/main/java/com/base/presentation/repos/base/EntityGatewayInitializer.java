package com.base.presentation.repos.base;

import com.base.abstraction.commands.Command;
import com.base.abstraction.exceptions.TestException;
import com.base.abstraction.exceptions.annotations.AnnotationNotDeclaredException;
import com.base.abstraction.reflections.Initializer;
import com.base.abstraction.system.App;
import com.base.presentation.annotations.interfaces.Requester;
import com.base.usecases.api.IntegrationHandler;
import com.base.usecases.callbacks.Callback;
import com.base.usecases.requesters.base.EntityGateway;
import com.base.usecases.requesters.base.EntityRequester;
import com.base.usecases.requesters.server.base.ServerRequester;

import java.lang.reflect.Constructor;

class EntityGatewayInitializer implements Command<Requester, EntityGateway> {

    private Repository repository;

    EntityGatewayInitializer(Repository repository) {
        this.repository = repository;
    }

    @Override
    public EntityGateway execute(Requester annotation) {
        IntegrationHandler useCasesApi = App.getInstance().getIntegrationHandler();
        ServerRequester requester = new Initializer<ServerRequester>().execute(annotation.value());
        try {
            requester.initialize(useCasesApi.getUrlLocator(requester));
        } catch (AnnotationNotDeclaredException e) {
            new TestException().execute(e);
        }
        Class<? extends EntityGateway> gatewayClass = annotation.gateway();
        return createEntityGatewayFromClass(gatewayClass, requester);
    }

    private EntityGateway createEntityGatewayFromClass(
            Class<? extends EntityGateway> gatewayClass,
            ServerRequester requester) {

        EntityGateway entityGateway;
        try {
            Constructor<?> constructor;
            constructor = gatewayClass.getDeclaredConstructor(EntityRequester.class, Callback.class);
            constructor.setAccessible(true);
            entityGateway = (EntityGateway) constructor.newInstance(requester, repository);
        } catch (Throwable e) {
            new TestException().execute(e);
            entityGateway = new EntityGateway(requester, repository);
        }
        return entityGateway;
    }
}
