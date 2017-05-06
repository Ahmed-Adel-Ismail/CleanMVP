package com.base.presentation.interfaces;

import android.content.Intent;

/**
 * an interface implemented by Classes that can hold an {@link Intent} that caused them to be
 * created / opened ... like {@link android.app.Activity}, {@link android.app.Service}, or
 * any similar class
 * <p>
 * Created by Ahmed Adel on 2/1/2017.
 */
public interface IntentHolder {

    /**
     * get the {@link Intent} that started the current instance
     *
     * @return the {@link Intent} if available, or {@code null}
     */
    Intent getIntent();

}
