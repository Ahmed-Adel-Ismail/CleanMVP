package com.base.presentation.listeners;

import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

/**
 * Created by Wafaa on 9/22/2016.
 */
public class OnSeekBarChangedParams {

    public final SeekBar seekBar;
    public final int progress;
    public final boolean fromUSer;

    public OnSeekBarChangedParams(SeekBar seekBar, int progress, boolean fromUSer) {
        this.seekBar = seekBar;
        this.progress = progress;
        this.fromUSer = fromUSer;
    }
}
