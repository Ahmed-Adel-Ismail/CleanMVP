package com.base.presentation.references;

import com.base.abstraction.commands.Command;
import com.base.abstraction.exceptions.TestException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Converter<T> implements Command<Class<? extends Property<T>>, Property<T>> {

    private PropertiesBuilder<T> propertiesBuilder;

    public Converter(PropertiesBuilder<T> propertiesBuilder) {
        this.propertiesBuilder = propertiesBuilder;
    }

    @Override
    public Property<T> execute(Class<? extends Property<T>> propertyClass) {
        if (propertiesBuilder.variations.containsKey(propertyClass)) {
            return propertiesBuilder.variations.get(propertyClass);
        } else if (propertiesBuilder.property.object != null) {
            try {
                return createNewPropertyVariation(propertyClass);
            } catch (Throwable e) {
                new TestException().execute(e);
            }
        } else {
            throw new UnsupportedOperationException("cannot create a variation of "
                    + propertyClass.getSimpleName() + " before setting a value to the original");
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private Property<T> createNewPropertyVariation(Class<? extends Property<T>> propertyClass)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        Constructor c = propertyClass.getDeclaredConstructor();
        c.setAccessible(true);
        Property<T> propertyVariation = (Property<T>) c.newInstance();
        propertyVariation.set(propertiesBuilder.property.object);
        propertiesBuilder.variations.put(propertyClass, propertyVariation);
        return propertyVariation;
    }
}
