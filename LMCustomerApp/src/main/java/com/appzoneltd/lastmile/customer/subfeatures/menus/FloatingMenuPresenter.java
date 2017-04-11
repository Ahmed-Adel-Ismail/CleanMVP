package com.appzoneltd.lastmile.customer.subfeatures.menus;

import android.support.annotation.NonNull;

import com.base.presentation.base.abstracts.features.ViewBinder;
import com.base.presentation.base.presentation.Presenter;

/**
 * a {@link Presenter} to handle Floating Menu Buttons
 * <p/>
 * Created by Ahmed Adel on 9/14/2016.
 */
public abstract class FloatingMenuPresenter extends Presenter {

    private final FloatingMenuViewModel floatingMenuViewModel;

    @Deprecated
    public FloatingMenuPresenter(ViewBinder viewBinder,
                                 @NonNull FloatingMenuViewModel floatingMenuViewModel) {
        super(viewBinder);
        this.floatingMenuViewModel = floatingMenuViewModel;
        FloatingMenuParamsGroup floatingMenuParamsGroup = createInitialFloatingMenuParamsGroup();
        if (floatingMenuParamsGroup != null) {
            floatingMenuViewModel.draw(floatingMenuParamsGroup);
        }
    }


    protected FloatingMenuViewModel getFloatingMenuDrawer() {
        return floatingMenuViewModel;
    }

    /**
     * createNativeMethod the initial {@link FloatingMenuParamsGroup} to be used in drawing the initial state
     * for the floating menu buttons
     *
     * @return the initial {@link FloatingMenuParamsGroup}, or {@code null} if you dont want to
     * preInitialize the views upon Construction
     */
    protected abstract FloatingMenuParamsGroup createInitialFloatingMenuParamsGroup();


}
