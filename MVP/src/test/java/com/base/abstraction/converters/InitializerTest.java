package com.base.abstraction.converters;

import com.base.abstraction.reflections.Initializer;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by ESC on 12/11/2016.
 */
public class InitializerTest {

    public InitializerTest() {
    }

    @Test
    public void execute_classA$$a_not_null() throws Exception {
        Class<A> klass = A.class;
        A a = new Initializer<A>().execute(klass);
        System.out.println("instance from class : " + a);
        Assert.assertTrue(a != null);
    }


    static class A {


    }

}