package com.base.abstraction.reflections;

import com.base.abstraction.commands.Command;
import com.base.abstraction.exceptions.CheckedReferenceClearedException;
import com.base.abstraction.exceptions.TestException;
import com.base.abstraction.logs.Logger;
import com.base.abstraction.references.CheckedReference;

import java.lang.reflect.Field;

/**
 * a class that initializes a {@link Field} from it's empty constructor
 * <p>
 * Created by Ahmed Adel on 11/30/2016.
 */
public class FieldInitializer<T> implements Command<Field, T> {

    private CheckedReference<Object> executingObject;

    public FieldInitializer(Object executingObject) {
        this.executingObject = new CheckedReference<>(executingObject);
    }

    @SuppressWarnings("unchecked")
    @Override
    public T execute(Field field) {
        T value = null;
        try {
            Class<?> clss = field.getType();
            value = (T) new Initializer<>().execute(clss);
            field.setAccessible(true);
            field.set(executingObject.get(), value);
        } catch (CheckedReferenceClearedException e) {
            Logger.getInstance().exception(e);
        } catch (Throwable e) {
            new TestException().execute(e);
        }
        return value;
    }
}
