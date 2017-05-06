package com.base.presentation.annotations.interfaces;

import com.base.presentation.references.Property;
import com.base.presentation.base.presentation.PresenterUpdater;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * annotate variables that holds data that are in {@link com.base.presentation.models.Model} and
 * {@link com.base.presentation.base.presentation.ViewModel}, so they can be updated when the
 * {@link PresenterUpdater} triggers it's updating methods
 * <p>
 * you should supply a unique {@code String} for each pair of synced variables, so the
 * updater class can map both variables to each other and update them as required
 * <p>
 * for {@link Property} variables, you can mark them as {@code final} if you like, this
 * annotation causes the value inside them to be synced, so the variable itself is not changed,
 * only the value in it
 * <p>
 * Created by Ahmed Adel on 12/5/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Sync {

    String value();
}
