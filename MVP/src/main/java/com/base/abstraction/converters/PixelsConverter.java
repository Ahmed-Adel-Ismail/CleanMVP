package com.base.abstraction.converters;

import android.util.DisplayMetrics;

import com.base.abstraction.system.AppResources;

/**
 * A Class that is responsible for converting pixel related values
 * <p/>
 * Created by Ahmed Adel on 9/14/2016.
 */
public class PixelsConverter {

    private int value;

    public PixelsConverter(int value) {
        this.value = value;
    }

    public int pixels() {
        DisplayMetrics displayMetrics = AppResources.displayMetrics();
        return Math.round(value * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public int dependantPixels() {
        DisplayMetrics displayMetrics = AppResources.displayMetrics();
        return Math.round(value / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
