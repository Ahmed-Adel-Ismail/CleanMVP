package com.appzoneltd.lastmile.customer.subfeatures.Toolbar;

import android.support.v7.widget.Toolbar;

import com.base.presentation.base.presentation.ViewModel;
import com.base.presentation.commands.DelayedCommand;

/**
 * An invalidator {@link DelayedCommand} for the toolbar in {@link ViewModel}
 * <p/>
 * Created by Ahmed Adel on 10/12/2016.
 */
public class ToolbarInvalidator extends DelayedCommand<ViewModel, Toolbar> {

    public ToolbarInvalidator(ViewModel host) {
        super(host);
    }

    @Override
    protected void delayedExecute(Toolbar toolbar) {
        ViewModel host = getHost();
        if (host != null) {
            toolbar.setTitle(host.getTitle());
        }

    }
}
