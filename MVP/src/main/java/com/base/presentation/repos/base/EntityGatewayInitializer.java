package com.base.presentation.repos.base;

import com.base.abstraction.api.usecases.RequestUrlLocator;
import com.base.abstraction.commands.Command;
import com.base.abstraction.exceptions.TestException;
import com.base.abstraction.exceptions.annotations.AnnotationNotDeclaredException;
import com.base.abstraction.logs.Logger;
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

        EntityRequester requester = new Initializer<EntityRequester>().execute(annotation.value());
        if (requester instanceof ServerRequester) {
            requester.initialize(requestUrlLocator(requester));
        } else {
            requester.initialize(null);
        }
        return createEntityGatewayFromClass(annotation.gateway(), requester);
    }

    private RequestUrlLocator requestUrlLocator(EntityRequester requester) {

        try {
            IntegrationHandler useCasesApi = App.getInstance().getIntegrationHandler();
            return useCasesApi.getServerRequesterUrlLocator(requester);
        } catch (AnnotationNotDeclaredException e) {
            Logger.getInstance().exception(e);
            return null;
        }
    }

    private EntityGateway createEntityGatewayFromClass(
            Class<? extends EntityGateway> gatewayClass,
            EntityRequester requester) {

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
