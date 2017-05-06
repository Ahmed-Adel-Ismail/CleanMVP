package com.base.presentation.base.loaders;

import com.base.presentation.interfaces.ContentViewable;
import com.base.presentation.interfaces.Destroyable;

/**
 * implement this interface by Classes that will handle a Splash screen
 * <p>
 * Created by Ahmed Adel on 11/22/2016.
 */
public interface SplashHandler extends ContentViewable, Destroyable {

    /**
     * start the actions that will be done while splash is displayed, this will run on the
     * UI thread, so make sure to open background threads if required in your implementation
     */
    void start();


}
