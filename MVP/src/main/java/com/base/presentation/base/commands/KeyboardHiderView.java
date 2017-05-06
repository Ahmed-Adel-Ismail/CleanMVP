package com.base.presentation.base.commands;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.base.abstraction.commands.Command;

/**
 * a class that adds a to the passed {@link View} an {@link OnTouchListener} that hides the
 * keyboard when {@link MotionEvent#ACTION_DOWN} is detected
 * <p>
 * if the {@link View} is {@link EditText}, nothing will happen
 * <p>
 * Created by Ahmed Adel on 1/30/2017.
 */
public class KeyboardHiderView implements Command<View, View> {

    private OnTouchListener keyboardHider;

    public KeyboardHiderView() {
        this.keyboardHider = createKeyboardHider();
    }

    @NonNull
    private OnTouchListener createKeyboardHider() {
        return new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    hideKeyboard(v);
                }
                return false;
            }
        };
    }

    private void hideKeyboard(View v) {
        InputMethodManager imm;
        imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    @Override
    public View execute(View view) {
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(keyboardHider);
        }
        return view;
    }
}
