package com.base.presentation.base.abstracts.features;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.base.abstraction.annotations.readers.ClassAnnotationReader;
import com.base.abstraction.commands.Command;
import com.base.abstraction.exceptions.annotations.AnnotationNotDeclaredException;
import com.base.abstraction.logs.Logger;
import com.base.presentation.exceptions.ViewNotFoundInXmlException;

import static com.base.presentation.annotations.interfaces.Toolbar.NULL;

/**
 * a class that draws the {@link ActionBar} and returns a {@link Toolbar} to be used
 * <p>
 * Created by Ahmed Adel on 12/18/2016.
 */
class ToolbarInflater implements Command<AbstractActivity, Toolbar> {

    private long logoResourceId;
    private boolean showTitle;


    @Override
    public Toolbar execute(AbstractActivity activity) throws ViewNotFoundInXmlException {
        Toolbar toolbar = null;
        try {
            int toolbarId = readToolbarAnnotationAndGetId(activity);
            toolbar = (Toolbar) activity.findViewById(toolbarId);
            if (toolbar == null) {
                throw new ViewNotFoundInXmlException(toolbarId);
            }
            drawActionBar(activity, toolbar);
        } catch (AnnotationNotDeclaredException e) {
            Logger.getInstance().info(activity.getClass(), "no custom toolbar found");
        }
        return toolbar;
    }

    private int readToolbarAnnotationAndGetId(AbstractActivity activity) {
        com.base.presentation.annotations.interfaces.Toolbar annotation = new ClassAnnotationReader<>(com.base.presentation.annotations.interfaces.Toolbar.class).execute(activity);
        logoResourceId = annotation.logo();
        showTitle = annotation.showTitle();
        return (int) annotation.value();
    }

    private void drawActionBar(AbstractActivity activity, Toolbar toolbar) {
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = activity.getSupportActionBar();

        if (actionBar == null) {
            throwNullActionBarException();
        }

        if (logoResourceId != NULL) {
            drawActionBarIcon(actionBar);
        }

    }


    private void drawActionBarIcon(ActionBar actionBar) {
        actionBar.setDisplayShowTitleEnabled(showTitle);
        actionBar.setIcon((int) logoResourceId);
    }

    private ActionBar throwNullActionBarException() {
        throw new UnsupportedOperationException("null @ " + getClass().getSimpleName() +
                ".drawActionBar() : no ActionBar found");
    }

}
