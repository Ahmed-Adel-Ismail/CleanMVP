package com.base.presentation.listeners;

import android.view.MotionEvent;
import android.view.View;

import com.base.abstraction.interfaces.Immutable;

public class OnTouchParams implements Immutable {
    private final View view;
    private final MotionEvent motionEvent;

    public OnTouchParams(View view, MotionEvent motionEvent) {
        this.view = view;
        this.motionEvent = motionEvent;
    }

    public View getView() {
        return view;
    }

    public MotionEvent getMotionEvent() {
        return motionEvent;
    }
}
