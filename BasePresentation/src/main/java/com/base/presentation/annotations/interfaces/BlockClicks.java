package com.base.presentation.annotations.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * mention the Views Ids that may cause un-expected behavior when clicked with each other
 * at the same time, the Ids mentioned will block each other, if one is clicked, the others clicks
 * will be ignored if clicked in a certain time interval
 * <p>
 * Created by Ahmed Adel on 11/27/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BlockClicks {

    int[] value();
}
