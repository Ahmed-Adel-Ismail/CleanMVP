package com.base.abstraction.annotations.interfaces;

import android.content.Intent;
import android.support.annotation.IntDef;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * an {@code enum} that specifies the source that the Object should load itself from
 * <p>
 * Created by Ahmed Adel on 11/30/2016.
 */
@IntDef({Source.ANNOTATIONS, Source.PREFERENCES, Source.INTENT})
@Retention(RetentionPolicy.SOURCE)
public @interface Source {
    /**
     * load from reading annotations declared in source-code, this is mainly intended
     * for classes
     */
    int ANNOTATIONS = 1;
    /**
     * load from preferences, you should provide the
     * preference key through {@link Load#value()}
     */
    int PREFERENCES = 2;
    /**
     * load from {@link Intent}, in this case the {@link Load#value()} should be a
     * {@code String} resource that represents a key to a {@link Serializable} value
     */
    int INTENT = 3;
}
