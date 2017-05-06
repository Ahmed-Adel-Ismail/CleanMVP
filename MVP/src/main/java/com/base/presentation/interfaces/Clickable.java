package com.base.presentation.interfaces;

import android.view.View;

/**
 * implement this interface if your Class will listen to clicks from xml file,
 * just set the {@code "android:onClick"} tag to "onClick" and implement the
 * {@link #onClick(View)}
 * method in your class
 * <p>
 * Created by Ahmed Adel on 9/4/2016.
 */
public interface Clickable {

    /**
     * the listener to clicks from xml layout file, you should set the {@code "android:onClick"}
     * tag to {@code "onClick"}
     *
     * @param view the {@link View clicked}
     */
    void onClick(View view);
}
