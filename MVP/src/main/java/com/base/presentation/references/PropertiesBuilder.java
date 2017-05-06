package com.base.presentation.references;

import android.support.annotation.CallSuper;

import com.base.abstraction.interfaces.Clearable;
import com.base.abstraction.state.SwitchableState;

import java.util.HashMap;

/**
 * a class that holds the abstract layer properties
 * <p>
 * Created by Ahmed Adel on 2/21/2017.
 */
public class PropertiesBuilder<T> implements Clearable {

    final HashMap<Class<? extends Property<T>>, Property<T>> variations = new HashMap<>();
    Property<T> property;

    PropertiesBuilder(Property<T> property) {
        this.property = property;
    }

    public Validator<T> validator() {
        return build(Validator.class);
    }

    public Consumable<T> consumable() {
        return build(Consumable.class);
    }

    public CheckedProperty<T> checkedProperty() {
        return build(CheckedProperty.class);
    }

    public Property<T> property() {
        return build(Property.class);
    }

    public Weaver<T> weaver() {
        return build(Weaver.class);
    }

    public Entity<T> entity() {
        return build(Entity.class);
    }

    @SuppressWarnings("unchecked")
    public MultiPartEntity<T> multiPartEntity() {
        return build(MultiPartEntity.class);
    }

    public Requester<T> requester() {
        return build(Requester.class);
    }

    public <S extends SwitchableState<S>> State<S> state() throws UnsupportedOperationException {
        if (!(property.get() instanceof SwitchableState)) {
            throw new UnsupportedOperationException("the type of the property must implement "
                    + SwitchableState.class.getSimpleName());
        }
        return (State<S>) build(State.class);
    }


    @SuppressWarnings("unchecked")
    protected <P extends Property<T>> P build(Class<? extends Property> propertyType)
            throws UnsupportedOperationException {
        return (P) new Converter<>(this).execute((Class<? extends Property<T>>) propertyType);
    }

    @Override
    @CallSuper
    public void clear() {
        for (Property<T> property : variations.values()) {
            property.clear();
        }
        variations.clear();
    }
}
