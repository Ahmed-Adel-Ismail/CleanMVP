package com.base.presentation.listeners;

import android.text.Editable;

/**
 * a {@link android.text.TextWatcher} for but requires a View ID to track the view in it's
 * {@link #afterTextChanged(Editable)}
 * <p/>
 * Created by Ahmed Adel on 10/3/2016.
 */
public abstract class TextWatcher implements android.text.TextWatcher {

    protected final int viewId;

    public TextWatcher(int viewId) {
        this.viewId = viewId;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

}
