package com.base.presentation.base.abstracts.features;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.base.abstraction.commands.Command;

import java.lang.ref.WeakReference;

/**
 * A Class to watch for key-board and hide it when touched outside {@link android.widget.EditText}
 * views
 * <p/>
 * Created by Ahmed Adel on 10/12/2016.
 */
class KeyboardWatcher implements Command<Feature<?>, Void> {

    private boolean keyboardOpened;
    private WeakReference<Feature<?>> featureRef;

    @Override
    public Void execute(Feature<?> feature) {
        featureRef = new WeakReference<Feature<?>>(feature);
        observeOnKeyboard(feature);
        attachOnTouchListener(feature);
        return null;
    }

    private void observeOnKeyboard(Feature<?> feature) {
        View view = feature.getHostActivity().getView();
        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(createKeyboardListener());
        setViewFocus(view);
    }

    @NonNull
    private ViewTreeObserver.OnGlobalLayoutListener createKeyboardListener() {
        return new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Feature<?> feature = featureRef.get();
                if (feature != null) {
                    updateKeyboardOpenedFlag(feature);
                }
            }

            private void updateKeyboardOpenedFlag(Feature<?> feature) {
                View view = feature.getView();
                if (view != null) {
                    updateKeyboardState(feature);
                } else {
                    invalidateKeyboardState();
                }

            }

            private void updateKeyboardState(Feature<?> feature) {
                int heightDiff = getHeightDiff(feature);
                keyboardOpened = (heightDiff > 100);
            }

            private int getHeightDiff(Feature<?> feature) {
                int overAllHeight = feature.getView().getRootView().getHeight();
                int originalHeight = feature.getView().getHeight();
                return overAllHeight - originalHeight;
            }

            private void invalidateKeyboardState() {
                keyboardOpened = false;
            }
        };
    }

    private void attachOnTouchListener(Feature<?> feature) {
        View.OnTouchListener onTouchListener = createKeyboardHiderOnTouchListener();
        if (feature.getView() instanceof SwipeRefreshLayout) {
            ((SwipeRefreshLayout) feature.getView())
                    .getChildAt(1)
                    .setOnTouchListener(onTouchListener);
        } else {
            feature.getView().setOnTouchListener(onTouchListener);
        }
    }

    @NonNull
    private View.OnTouchListener createKeyboardHiderOnTouchListener() {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (keyboardOpened) {
                    Feature<?> feature = featureRef.get();
                    if (feature != null) {
                        feature.getHostActivity().getSystemServices().hideKeyboard();
                    }
                    keyboardOpened = false;
                }
                return false;
            }
        };
    }

    private void setViewFocus(View view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        if (view instanceof SwipeRefreshLayout) {
            ((SwipeRefreshLayout) view).getChildAt(1).setFocusable(true);
            ((SwipeRefreshLayout) view).getChildAt(1).setFocusableInTouchMode(true);
        }
    }

}
