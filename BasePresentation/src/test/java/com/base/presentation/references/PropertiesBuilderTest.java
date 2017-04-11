package com.base.presentation.references;

import com.base.abstraction.exceptions.references.states.StateIsMovingToNullException;
import com.base.abstraction.exceptions.references.states.StateNotValidToMoveException;
import com.base.abstraction.state.SwitchableState;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Ahmed Adel on 2/21/2017.
 */
public class PropertiesBuilderTest {

    public PropertiesBuilderTest() {
    }


    @Test
    public void as_consumable$$newConsumable() throws Exception {
        Property<Integer> integer = new Property<>(10);
        integer.as().validator();
        Consumable<Integer> consumableInteger = integer.as().consumable();
        Assert.assertTrue(consumableInteger.get() == 10);
    }


    @Test
    public void as_consumable_thenNullConsumableValue$$propertyGetReturnNotNull() throws Exception {
        Property<Integer> integer = new Property<>(10);
        Consumable<Integer> consumableInteger = integer.as().consumable();
        consumableInteger.get();
        Assert.assertTrue(integer.get() == 10 && consumableInteger.get() == null);
    }

    @Test
    public void as_property_thenModifyValue$$propertyValueModified() throws Exception {
        Property<Property<Integer>> propertyOne = new Property<>(new Property<>(10));
        Property<Property<Integer>> propertyTwo = propertyOne.as().property();
        propertyTwo.get().set(5);
        Assert.assertTrue(propertyOne.get().get() == 5);
    }

    @Test
    public void as_validator$$newValidator() throws Exception {
        Property<Integer> integer = new Property<>(10);
        integer.as().validator()
                .addRule(new Rule<Integer>() {
                    @Override
                    public Integer execute(Integer p) {
                        return null;
                    }
                });

        Assert.assertTrue(integer.as().validator().validate() != null);
    }

    @Test
    public void as_checkedProperty$$newCheckedProperty() throws Exception {
        Property<Integer> integer = new Property<>(10);
        Assert.assertTrue(integer.as().checkedProperty().get() == 10);
    }

    @Test
    public void as_weaver$$newWeaver() throws Exception {
        Property<Integer> integer = new Property<>(10);
        Assert.assertTrue(integer.as().weaver().get().equals(10));
    }

    @Test
    public void as_state$$success() throws Exception {

        class A implements SwitchableState<A> {
            @Override
            public A back() throws StateIsMovingToNullException, StateNotValidToMoveException {
                return this;
            }

            @Override
            public A next() throws StateIsMovingToNullException, StateNotValidToMoveException {
                return this;
            }
        }

        Property<A> property = new Property<>(new A());
        State<A> state = property.as().state();
        Assert.assertTrue(state.get() != null);


    }

    @Test
    public void as_entity$$success() throws Exception {
        Property<Integer> o = new Property<>(10);
        Assert.assertTrue(o.as().entity().get().equals(10));
    }

    @Test
    public void as_requester$$success() throws Exception {
        Property<Integer> o = new Property<>(10);
        Assert.assertTrue(o.as().requester().get() == 10);
    }

    @Test
    public void as_multipartEntity$$success() throws Exception {
        Property<Integer> o = new Property<>(10);
        Assert.assertTrue(o.as().multiPartEntity().get() == 10);
    }


}