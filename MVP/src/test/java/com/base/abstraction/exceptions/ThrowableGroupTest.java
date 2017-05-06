package com.base.abstraction.exceptions;

import com.base.abstraction.exceptions.propagated.ThrowableGroup;

import junit.framework.Assert;

import org.junit.Test;


public class ThrowableGroupTest {

    public ThrowableGroupTest() {
    }

    @Test
    public void add_SubClass$contains_ParentClass$$true() throws Exception {
        ThrowableGroup throwables = new ThrowableGroup();
        throwables.add(new SubException());
        boolean isInstanceOf = throwables.contains(ParentException.class);
        Assert.assertTrue(isInstanceOf);
    }


    private static class ParentException extends RuntimeException {

    }

    private static class SubException extends ParentException {

    }

}