package com.base.abstraction.annotations;

import android.support.annotation.NonNull;

import com.base.abstraction.annotations.interfaces.Executable;
import com.base.abstraction.annotations.readers.MethodAnnotationReader;
import com.base.abstraction.exceptions.annotations.AnnotationNotDeclaredException;
import com.base.abstraction.logs.Logger;

import org.junit.Assert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Ahmed Adel on 11/24/2016.
 */
public class MethodAnnotationReaderTest {

    public MethodAnnotationReaderTest() {
    }

    @org.junit.Test
    public void executeNumbers_50$$idCorrect_methodInvoked() throws Exception {
        Tester1 t = new Tester1();
        t.executeNumbers();
        Assert.assertTrue(t.idCorrect && t.methodInvoked);
    }

    private static class Tester1 {

        private boolean idCorrect;
        private boolean methodInvoked;


        @Executable(50)
        protected void foo() {
            methodInvoked = true;
        }


        private void executeNumbers() {
            try {
                MethodAnnotationReader<Executable> reader;
                reader = createMethodAnnotationReader();
                reader.execute(this);
            } catch (AnnotationNotDeclaredException e) {
                Logger.getInstance().exception(e);
            }

        }

        @NonNull
        private MethodAnnotationReader<Executable> createMethodAnnotationReader() {
            return new MethodAnnotationReader<Executable>(Executable.class) {
                @Override
                protected void processAnnotation(Method method, Executable annotation) {
                    long id = annotation.value()[0];
                    idCorrect = id == 50;
                    try {
                        method.invoke(Tester1.this);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }


                }
            };
        }


    }


}