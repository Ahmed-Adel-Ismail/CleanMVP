package com.base.presentation.base.abstracts.features;

import android.view.KeyEvent;

/**
 * the parameters of the {@code onKeyDown()} event which occures in the Activity
 * <p>
 * Created by Ahmed Adel Ismail on 4/20/2017.
 */
public class OnKeyDownParams {

    public final int keyCode;
    public final KeyEvent keyEvent;

    public OnKeyDownParams(int keyCode, KeyEvent keyEvent) {
        this.keyCode = keyCode;
        this.keyEvent = keyEvent;
    }
}
