package com.base.presentation.base.abstracts.features;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;

import com.base.abstraction.annotations.readers.ClassAnnotationReader;
import com.base.abstraction.commands.Command;
import com.base.abstraction.exceptions.annotations.AnnotationNotDeclaredException;
import com.base.abstraction.logs.Logger;
import com.base.presentation.R;
import com.base.presentation.annotations.interfaces.Drawer;
import com.base.presentation.exceptions.ViewNotFoundInXmlException;

import static com.base.presentation.annotations.interfaces.Drawer.NULL;

/**
 * a class responsible for inflating {@link DrawerLayout} and constructing it's
 * UI from xml
 * <p>
 * Created by Ahmed Adel on 12/18/2016.
 */
class DrawerInflater implements Command<AbstractActivity, DrawerLayout> {

    private Toolbar toolbar;
    private long openDescRes = R.string.navigation_drawer_open;
    private long closedDescRes = R.string.navigation_drawer_close;
    private long menuIcon = R.drawable.menu_white_lines;

    DrawerInflater(Toolbar toolbar) {
        this.toolbar = toolbar;
    }

    @Override
    public DrawerLayout execute(AbstractActivity activity) throws ViewNotFoundInXmlException {
        DrawerLayout drawerLayout = null;
        try {
            Drawer annotation = new ClassAnnotationReader<>(Drawer.class).execute(activity);

            long value = annotation.openDescRes();
            openDescRes = (value != NULL) ? value : openDescRes;

            value = annotation.closedDescRes();
            closedDescRes = (value != NULL) ? value : closedDescRes;

            value = annotation.menuIcon();
            menuIcon = (value != NULL) ? value : menuIcon;

            int drawerLayoutId = (int) annotation.value();
            drawerLayout = (DrawerLayout) activity.findViewById(drawerLayoutId);
            if (drawerLayout == null) {
                throw new ViewNotFoundInXmlException(drawerLayoutId);
            }

            drawToggle(activity, drawerLayout);

        } catch (AnnotationNotDeclaredException e) {
            Logger.getInstance().info(activity.getClass(), "no custom drawer found");
        }
        return drawerLayout;
    }

    private void drawToggle(AbstractActivity activity, DrawerLayout drawerLayout) {
        ActionBarDrawerToggle toggle = createToggle(activity, drawerLayout);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        toggle.setHomeAsUpIndicator((int) menuIcon);
    }

    private ActionBarDrawerToggle createToggle(AbstractActivity activity, DrawerLayout drawerLayout) {
        return new ActionBarDrawerToggle(activity, drawerLayout,
                toolbar, (int) openDescRes, (int) closedDescRes);
    }


}
