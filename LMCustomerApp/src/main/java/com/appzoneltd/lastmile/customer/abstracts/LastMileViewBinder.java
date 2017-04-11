package com.appzoneltd.lastmile.customer.abstracts;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.appzoneltd.lastmile.customer.interfaces.ActivityHiddenKeyboard;
import com.appzoneltd.lastmile.customer.interfaces.ActivityNoTitle;
import com.appzoneltd.lastmile.customer.interfaces.ActivityTransparentWindow;
import com.base.presentation.base.abstracts.features.Feature;
import com.base.presentation.base.abstracts.features.ViewBinder;
import com.base.presentation.models.Model;

import butterknife.ButterKnife;

/**
 * The base class for all LastMile {@link ViewBinder} classes
 * <p/>
 * Created by Ahmed Adel on 9/4/2016.
 */
public abstract class LastMileViewBinder<T extends Model> extends ViewBinder {

    public LastMileViewBinder(Feature<T> feature) {
        super(feature);

        if (feature instanceof ActivityNoTitle) {
            hideTitleBar();
        }

        if (feature instanceof ActivityTransparentWindow) {
            updateWindowTransparentBackground();
        }

        if (feature instanceof ActivityHiddenKeyboard) {
            hideKeyboard();
        }
    }

    private void hideTitleBar() {
        getHostActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    private void updateWindowTransparentBackground() {
        Activity activity = getHostActivity();
        Window window = activity.getWindow();
        Drawable transparentDrawable = new ColorDrawable(Color.TRANSPARENT);
        window.setBackgroundDrawable(transparentDrawable);
    }

    private void hideKeyboard() {
        Activity activity = getHostActivity();
        Window window = activity.getWindow();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /**
     * same as {@link #initializeViews(Bundle)} but after calling the common UI related
     * calls like {@link ButterKnife#bind(Object, Activity)} for example
     *
     * @param savedInstanceState same as {@link #initializeViews(Bundle)}
     */
    public abstract void initializeAfterBindingViews(Bundle savedInstanceState);


}
