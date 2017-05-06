package com.base.presentation.references;

import com.base.abstraction.interfaces.Clearable;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Ahmed Adel on 2/14/2017.
 */
public class FieldsCleanerTest {


    public FieldsCleanerTest() {
    }


    @Test
    public void execute_on_private_variables$$all_null() throws Exception {
        B b = new B();
        new FieldsCleaner().execute(b);
        Assert.assertTrue(b.a.equals("a") && b.b == null);

    }

    @Test
    public void execute_on_final_clearable_and_static_variables$$all_not_null_and_clear_invoked() throws Exception {
        Property<Integer> inspection = new Property<>(0);
        C c = new C(inspection);
        new FieldsCleaner(true).execute(c);
        Assert.assertTrue(c.c != null && C.i == 1 && inspection.get() == -1);

    }

    @Test
    public void execute_on_property_variables$$all_get_null() throws Exception {
        D d = new D();
        new FieldsCleaner().execute(d);
        Assert.assertTrue(d.d.get() == null);
    }

    @Test
    public void execute_on_primitives_variables$$primitives_not_changed() throws Exception {
        E e = new E();
        new FieldsCleaner().execute(e);
        Assert.assertTrue(e.e == 5);
    }

    @Test
    public void execute_on_all_variables_types_$$primitives_not_changed_all_other_null() throws Exception {
        F f = new F();
        new FieldsCleaner(true).execute(f);
        Assert.assertTrue(f.a == null && f.b == null && f.c == null
                && f.d == null && f.i == 5);
    }


    static class A {
        String a = "a";
    }


    static class B extends A {
        private String b = "b";
    }

    static class C {
        private final Clearable c;
        private static int i = 1;

        C(final Property<Integer> property) {
            c = new Clearable() {

                int i = 10;

                @Override
                public void clear() {
                    i = 0;
                    property.set(-1);
                }
            };
        }
    }

    static class D implements Clearable {

        private final Property<Integer> d = new Property<>(10);

        @Override
        public void clear() {
            d.clear();
        }
    }


    static class E {
        private int e = 5;
    }

    static class F {
        private A a;
        private B b;
        private C c;
        private D d;
        private int i;

        F() {
            a = new A();
            b = new B();
            c = new C(new Property<>(0));
            d = new D();
            i = 5;
        }
    }


}