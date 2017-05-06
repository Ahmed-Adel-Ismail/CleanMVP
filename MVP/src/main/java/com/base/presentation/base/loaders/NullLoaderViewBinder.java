package com.base.presentation.base.loaders;

import com.base.abstraction.system.AppLoader;

/**
 * a class that indicates a {@code null} {@link AbstractLoaderViewBinder}, this is the default
 * {@link AbstractLoaderViewBinder} for applications that do not use {@link AppLoader} classes
 * <p>
 * Created by Ahmed Adel Ismail on 4/19/2017.
 */
public class NullLoaderViewBinder extends AbstractLoaderViewBinder {
    @Override
    public SplashHandler createSplashHandler() {
        return new SplashHandler() {
            @Override
            public void start() {
                // do nothing
            }

            @Override
            public int getContentView() {
                return 0;
            }

            @Override
            public void onDestroy() {
                // do nothing
            }
        };
    }
}
