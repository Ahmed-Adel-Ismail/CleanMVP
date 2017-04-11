package com.base.presentation.base.loaders;

/**
 * implement this interface if your class will use {@link SplashHandler} in it's implementation
 * <p>
 * Created by Ahmed Adel on 11/22/2016.
 */
public interface SplashHandlerClient {

    /**
     * create the {@link SplashHandler} that will be used
     *
     * @return the implementer of {@link SplashHandler}
     */
    SplashHandler createSplashHandler();
}
