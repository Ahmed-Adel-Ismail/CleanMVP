package com.base.presentation.listeners;

import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.base.presentation.references.BooleanProperty;
import com.base.presentation.references.Property;

/**
 * an Object that holds the values passed from
 * {@link OnCheckedChangeListener#onCheckedChanged(CompoundButton, boolean)} method
 * <p>
 * Created by Ahmed Adel on 12/31/2016.
 */
@SuppressWarnings("WeakerAccess")
public class OnCheckedChangedParams {
    public final Property<CompoundButton> compoundButton = new Property<>();
    public final BooleanProperty checked = new BooleanProperty();
    public final Property<Object> tag = new Property<>();
}
