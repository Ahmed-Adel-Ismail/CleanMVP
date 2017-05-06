package com.base.presentation.base.loaders;

import com.base.presentation.annotations.interfaces.Splash;
import com.base.presentation.base.abstracts.features.AbstractActivity;
import com.base.presentation.base.abstracts.features.Feature;
import com.base.presentation.base.abstracts.features.ViewBinder;
import com.base.presentation.interfaces.ContentViewable;

/**
 * the parent class for the error view binders classes that will be responsible for handling the
 * screen in case of loading data was required
 * <p>
 * you must implement {@link #createSplashHandler()}
 * <p>
 * Created by Ahmed Adel on 11/22/2016.
 */
public abstract class AbstractLoaderViewBinder extends ViewBinder implements
        ContentViewable,
        SplashHandlerClient {

    private SplashHandler splashHandler;


    public void initialize(Feature<?> feature) {
        super.initialize(feature);
        splashHandler = createSplashHandler();
    }

    protected SplashHandler getSplashHandler() {
        return splashHandler;
    }

    @Override
    public final int getContentView() {
        if (getHostActivity().getClass().isAnnotationPresent(Splash.class)) {
            return splashHandler.getContentView();
        } else {
            return getLoaderContentView();
        }

    }

    /**
     * proceed to the next stage, weather invoke the {@link SplashHandler#start()} or the
     * {@link AbstractActivity#recreate()} based on the type of the {@link AbstractActivity}
     *
     * @throws UnsupportedOperationException if the Host {@link AbstractActivity} is destroyed
     */
    protected final void proceed() throws UnsupportedOperationException {
        try {
            if (getHostActivity().getClass().isAnnotationPresent(Splash.class)) {
                getSplashHandler().start();
            } else {
                getHostActivity().recreate();
            }
        } catch (Throwable e) {
            throw new UnsupportedOperationException("Host Activity not available any more");
        }
    }

    /**
     * get the error view xml layout id
     *
     * @return the loading UI, by default this is the splash UI, you can override this method to
     * show different xml layout
     */
    protected int getLoaderContentView() {
        return splashHandler.getContentView();
    }


    @Override
    public void onDestroy() {
        if (splashHandler != null) {
            splashHandler.onDestroy();
            splashHandler = null;
        }
        super.onDestroy();
    }


}
