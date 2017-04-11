package com.base.presentation.behaviors;

import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.base.abstraction.commands.Command;
import com.base.abstraction.exceptions.CheckedReferenceClearedException;
import com.base.abstraction.logs.Logger;
import com.base.presentation.references.CheckedProperty;

/**
 * a class that disables Dismiss behavior for {@link Snackbar}
 * <p>
 * Created by Ahmed Adel on 12/29/2016.
 */
public class SnackbarWithoutDismiss implements Command<Snackbar, Snackbar> {

    private CheckedProperty<Snackbar.SnackbarLayout> layout = new CheckedProperty<>();

    @Override
    public Snackbar execute(Snackbar snackbar) {
        try {
            layout.set((Snackbar.SnackbarLayout) snackbar.getView());
            layout.get().getViewTreeObserver().addOnGlobalLayoutListener(createOnLayoutListener());
        } catch (CheckedReferenceClearedException e) {
            Logger.getInstance().error(getClass(), e);
        }

        return snackbar;
    }

    @NonNull
    private ViewTreeObserver.OnGlobalLayoutListener createOnLayoutListener() {
        return new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                try {
                    ViewGroup.LayoutParams lp = layout.get().getLayoutParams();
                    if (lp instanceof CoordinatorLayout.LayoutParams) {
                        updateLayoutParams(lp);
                    }
                    layout.get().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } catch (CheckedReferenceClearedException e) {
                    Logger.getInstance().error(SnackbarWithoutDismiss.class, e);
                }


            }
        };
    }

    private void updateLayoutParams(ViewGroup.LayoutParams lp) {
        ((CoordinatorLayout.LayoutParams) lp).setBehavior(new DisableSnackbarSwipeBehavior());
        layout.get().setLayoutParams(lp);
    }
}
