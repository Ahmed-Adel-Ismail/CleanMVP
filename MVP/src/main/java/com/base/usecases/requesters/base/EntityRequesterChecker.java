package com.base.usecases.requesters.base;

import android.support.annotation.NonNull;

import com.base.abstraction.annotations.scanners.FieldAnnotationScanner;
import com.base.abstraction.commands.Command;
import com.base.abstraction.exceptions.TestException;
import com.base.abstraction.reflections.Initializer;
import com.base.abstraction.system.App;
import com.base.abstraction.system.Behaviors;
import com.base.usecases.annotations.Mock;
import com.base.usecases.requesters.server.mocks.MockedEntity;
import com.base.entities.annotations.MockEntity;
import com.base.entities.cached.Null;
import com.base.usecases.callbacks.Callback;
import com.base.usecases.requesters.server.mocks.MockRequester;
import com.base.usecases.requesters.server.mocks.MockedEntitiesRegistry;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * a class that checks the {@link EntityRequester} and returns a valid {@link EntityRequesterChecker},
 * weather a normal one or a {@link MockRequester}
 * <p>
 * Created by Ahmed Adel on 12/13/2016.
 */
class EntityRequesterChecker implements Command<EntityRequester, EntityRequester> {

    private Callback callback;

    EntityRequesterChecker(@NonNull Callback callback) {
        this.callback = callback;
    }

    @Override
    public EntityRequester execute(EntityRequester requester) {
        EntityRequester actualRequester = requester;
        if (App.getInstance().isBehaviorAccepted(Behaviors.MOCKING)) {
            actualRequester = createMockRequesterIfRequired(actualRequester, callback);
        }
        return actualRequester;
    }

    private EntityRequester createMockRequesterIfRequired(EntityRequester actualRequester, Callback callback) {
        final MockedEntitiesRegistry mockedEntitiesRegistry = new MockedEntitiesRegistry();

        new FieldAnnotationScanner<Mock>(Mock.class) {
            @Override
            protected void onAnnotationFound(Field element, Mock annotation) {
                if (!mockedEntitiesRegistry.contains(annotation.value())) {
                    readMockedAnnotations(element, annotation, mockedEntitiesRegistry);
                }
            }
        }.execute(callback);

        if (!mockedEntitiesRegistry.isEmpty()) {
            actualRequester = createMockRequester(actualRequester, mockedEntitiesRegistry);
        }

        return actualRequester;
    }


    private void readMockedAnnotations(Field element, Mock annotation,
                                       MockedEntitiesRegistry mockedEntitiesRegistry) {
        element.setAccessible(true);
        Class<?> klass = element.getType();
        try {
            MockedEntity mockEntity = createMockEntity(annotation, klass);
            mockedEntitiesRegistry.put(annotation.value(), mockEntity);
        } catch (Throwable e) {
            new TestException().execute(e);
        }
    }

    private MockedEntity createMockEntity(Mock annotation, Class<?> klass)
            throws InstantiationException, IllegalAccessException, NoSuchMethodException {

        MockedEntity mockedEntity = createMockedEntity(annotation);
        if (klass.isAnnotationPresent(MockEntity.class)) {
            mockedEntity = getMockEntitiesFromAnnotation(klass, mockedEntity);
        } else {
            mockedEntity = getEmptyMockedEntities(klass, mockedEntity);
        }
        return mockedEntity;
    }

    @NonNull
    private MockedEntity createMockedEntity(Mock annotation) {
        MockedEntity mockedEntity = new MockedEntity();
        mockedEntity.setRequestId(annotation.value());
        mockedEntity.setStatusCode(annotation.statusCode());
        return mockedEntity;
    }

    private MockedEntity getMockEntitiesFromAnnotation(Class<?> klass, MockedEntity mockedEntity)
            throws InstantiationException, IllegalAccessException, NoSuchMethodException {
        MockEntity mockEntity = klass.getAnnotation(MockEntity.class);

        Serializable entity = createSuccessEntity(klass, mockEntity);
        mockedEntity.setSuccessResponse(entity);

        entity = new Initializer<Serializable>().execute(mockEntity.error());
        mockedEntity.setErrorResponse(entity);

        return mockedEntity;
    }

    private Serializable createSuccessEntity(Class<?> klass, MockEntity mockEntity)
            throws InstantiationException, IllegalAccessException {
        Serializable successEntity;
        if (mockEntity.value().equals(Null.class)) {
            successEntity = (Serializable) new Initializer<>().execute(klass);
        } else {
            successEntity = new Initializer<Serializable>().execute(mockEntity.value());
        }
        return successEntity;
    }

    private MockedEntity getEmptyMockedEntities(Class<?> klass, MockedEntity mockedEntity)
            throws InstantiationException, IllegalAccessException {
        Serializable entity = (Serializable) new Initializer<>().execute(klass);
        mockedEntity.setSuccessResponse(entity);
        mockedEntity.setErrorResponse(entity);
        return mockedEntity;
    }

    @NonNull
    private EntityRequester createMockRequester(EntityRequester actualRequester, MockedEntitiesRegistry mockedEntitiesRegistry) {
        MockRequester mockRequester = new MockRequester();
        mockRequester.setOriginalRequester(actualRequester);
        mockRequester.putAllMockedEntitiesRegistry(mockedEntitiesRegistry);
        mockedEntitiesRegistry.clear();
        return mockRequester;
    }

}
