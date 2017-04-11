package com.base.presentation.base.presentation;

import android.view.View;
import android.widget.Checkable;
import android.widget.TextView;

import com.base.abstraction.commands.Command;

/**
 * Created by Wafaa on 11/29/2016.
 */

public class AddFocusToViews implements Command<View, Void> {

    @Override
    public Void execute(View view) {
        if(!(view instanceof TextView)
                && !(view instanceof Checkable)){
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
        }
        return null;
    }

}
