package com.base.presentation.behaviors;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.SwipeDismissBehavior;
import android.view.View;

/**
 * Created by Ahmed Adel on 12/28/2016.
 */
public class DisableSnackbarSwipeBehavior extends SwipeDismissBehavior<Snackbar.SnackbarLayout> {
    @Override
    public boolean canSwipeDismissView(@NonNull View view) {
        return false;
    }
}
