package com.base.presentation.base.abstracts.features;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

import com.base.abstraction.commands.Command;
import com.base.presentation.exceptions.ViewNotFoundInXmlException;

/**
 * a class to get the {@link NavigationView} if found
 * <p>
 * Created by Ahmed Adel on 12/18/2016.
 */
class NavigationViewInflater implements Command<DrawerLayout, NavigationView> {

    @Override
    public NavigationView execute(@NonNull DrawerLayout drawerLayout)
            throws ViewNotFoundInXmlException {

        NavigationView navigationView = null;
        View v;
        for (int i = 0; i < drawerLayout.getChildCount(); i++) {
            v = drawerLayout.getChildAt(i);
            if (v instanceof NavigationView) {
                navigationView = (NavigationView) v;
                break;
            }
        }

        if (navigationView == null) {
            throw new ViewNotFoundInXmlException("NavigationView");
        }

        return navigationView;
    }
}
