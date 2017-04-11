package com.base.presentation.base.abstracts.features;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;

import com.base.abstraction.commands.Command;
import com.base.abstraction.logs.Logger;
import com.base.presentation.exceptions.ViewNotFoundInXmlException;
import com.base.presentation.listeners.OnEventListener;

/**
 * a class that is responsible for drawing the UI of an Activity
 * <p>
 * Created by Ahmed Adel on 12/15/2016.
 */
class ActivityInflater implements Command<ViewBinder, Void> {


    @Override
    public Void execute(ViewBinder viewBinder) {
        AbstractActivity activity = (AbstractActivity) viewBinder.getFeature();
        activity.setContentView(viewBinder.getContentView());
        try {
            Toolbar toolbar = new ToolbarInflater().execute(activity);
            DrawerLayout drawerLayout = new DrawerInflater(toolbar).execute(activity);
            if (drawerLayout != null) {
                setupNavigationView(activity, drawerLayout);
            }
        } catch (ViewNotFoundInXmlException e) {
            Logger.getInstance().error(activity.getClass(), e);
        }
        return null;
    }

    private void setupNavigationView(AbstractActivity activity, DrawerLayout drawerLayout) {
        NavigationView navigationView = new NavigationViewInflater().execute(drawerLayout);
        navigationView.setNavigationItemSelectedListener(new OnEventListener(activity));
        activity.setDrawerLayout(drawerLayout);
    }


}
