package com.appzoneltd.lastmile.customer.subfeatures.drawers;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.cutomerappsystem.Features;
import com.appzoneltd.lastmile.customer.deprecated.SettingActivity;
import com.appzoneltd.lastmile.customer.features.tracking.host.TrackingActivity;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.abstraction.events.Message;
import com.base.presentation.base.abstracts.features.AbstractActivity;
import com.base.presentation.base.abstracts.features.ViewBinder;
import com.base.presentation.base.presentation.Presenter;
import com.base.presentation.exceptions.OnBackPressException;
import com.base.presentation.listeners.OnEventListener;

/**
 * a {@link Presenter} that handles the Main {@link DrawerLayout} of the application with it's
 * {@link android.support.design.widget.NavigationView.OnNavigationItemSelectedListener} actions
 * <p/>
 * Created by Ahmed Adel on 9/6/2016.
 */
public class MainDrawerPresenter extends Presenter {

    private final CommandExecutor<Long, MenuItem> menuItemsCommandExecutor;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;


    public MainDrawerPresenter(MainDrawerPresenter.Params p, Toolbar toolbar) {
        super(p.viewBinder);
        menuItemsCommandExecutor = createMenuItemsCommandExecutor();
        drawerLayout = getUpdatedDrawerLayout(p, toolbar);
        navigationView = getUpdatedNavigationView(p);
    }

    private CommandExecutor<Long, MenuItem> createMenuItemsCommandExecutor() {
        CommandExecutor<Long, MenuItem> commandExecutor = new CommandExecutor<>();
        Command<MenuItem, Void> command = createOnNavigationSettingsClickCommand();
        commandExecutor.put((long) R.id.packages, command);
        command = createOnNavigationTrackingClickCommand();
        commandExecutor.put((long) R.id.tracking, command);
        return commandExecutor;
    }

    private Command<MenuItem, Void> createOnNavigationSettingsClickCommand() {
        return new Command<MenuItem, Void>() {
            @Override
            public Void execute(MenuItem p) {
                Intent intent = new Intent(getHostActivity(), Features.PackageListActivity.class);
                getHostActivity().startActivity(intent);
                onMenuItemClickFinished();
                return null;
            }
        };
    }

    private Command<MenuItem, Void> createOnNavigationTrackingClickCommand() {
        return new Command<MenuItem, Void>() {
            @Override
            public Void execute(MenuItem p) {
                Intent intent = new Intent(getHostActivity(), TrackingActivity.class);
                getHostActivity().startActivity(intent);
                onMenuItemClickFinished();
                return null;
            }

        };
    }


    private DrawerLayout getUpdatedDrawerLayout(MainDrawerPresenter.Params p,
                                                Toolbar toolbar) {
        AbstractActivity activity = getHostActivity();
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            setupActionBar(p, actionBar);
        }
        ActionBarDrawerToggle toggle = createToggle(p, toolbar);
        p.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        toggle.setHomeAsUpIndicator(p.menuIconResource);
        return p.drawerLayout;
    }

    @NonNull
    private ActionBarDrawerToggle createToggle(MainDrawerPresenter.Params p,
                                               Toolbar toolbar) {
        return new ActionBarDrawerToggle(getHostActivity(), p.drawerLayout,
                toolbar, p.openDrawerStringResource, p.closeDrawerStringResource);
    }

    private void setupActionBar(MainDrawerPresenter.Params p, ActionBar actionBar) {
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setIcon(p.logo);
    }

    private NavigationView getUpdatedNavigationView(MainDrawerPresenter.Params p) {
        p.navigationView.setNavigationItemSelectedListener(new OnEventListener(getHostActivity()));
        return p.navigationView;
    }


    @Override
    protected CommandExecutor<Long, Message> createCommandExecutor() {
        CommandExecutor<Long, Message> commandExecutor = new CommandExecutor<>();
        Command<Message, Void> command = createOnNavigationItemSelectedCommand();
        commandExecutor.put((long) R.id.onNavigationItemSelected, command);
        command = createOnBackPressedCommand();
        commandExecutor.put((long) R.id.onBackPressed, command);
        return commandExecutor;
    }

    private Command<Message, Void> createOnNavigationItemSelectedCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                long menuItemId = message.getId();
                MenuItem menuItem = message.getContent();
                menuItemsCommandExecutor.execute(menuItemId, menuItem);
                return null;
            }
        };
    }


    private Command<Message, Void> createOnBackPressedCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message p) {
                if (isCloseNavigationPerformed()) {
                    throw new OnBackPressException();
                }
                return null;
            }
        };
    }

    private boolean isCloseNavigationPerformed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            onMenuItemClickFinished();
            return true;
        }
        return false;
    }

    private void onMenuItemClickFinished() {
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onDestroy() {
        drawerLayout = null;
        navigationView = null;
        menuItemsCommandExecutor.clear();

    }


    public static class Params {
        public ViewBinder viewBinder;
        public DrawerLayout drawerLayout;
        public NavigationView navigationView;
        public int logo = R.drawable.logo;
        public int menuIconResource = R.drawable.menu;
        public int openDrawerStringResource = R.string.navigation_drawer_open;
        public int closeDrawerStringResource = R.string.navigation_drawer_close;
    }


}
