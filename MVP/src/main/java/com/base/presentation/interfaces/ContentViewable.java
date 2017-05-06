package com.base.presentation.interfaces;

/**
 * implement this interface if your class will have an xml layout file that represents it's content
 * on screen
 * <p>
 * Created by Ahmed Adel on 11/22/2016.
 */
public interface ContentViewable {

    /**
     * get the xml layout id to be used in {@link android.app.Activity#setContentView(int)},
     * or related methods in other classes
     *
     * @return the xml layout id
     */
    int getContentView();

}
