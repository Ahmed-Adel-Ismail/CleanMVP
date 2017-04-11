package com.appzoneltd.lastmile.customer.features.main.packageslist.host;

import android.view.Menu;
import android.view.MenuItem;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.features.main.packageslist.models.PackageListModel;
import com.appzoneltd.lastmile.customer.features.notificationlist.host.NotificationActivity;
import com.base.abstraction.annotations.interfaces.Executable;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.abstraction.events.Event;
import com.base.abstraction.events.EventBuilder;
import com.base.abstraction.events.Message;
import com.base.presentation.base.presentation.Presenter;
import com.base.presentation.requests.ActionType;
import com.base.presentation.requests.ActivityActionRequest;

/**
 * the {@link Presenter} for the packages list activity
 * <p>
 * Created by Wafaa on 12/15/2016.
 */
class PackageListActivityPresenter extends Presenter
        <PackageListActivityPresenter, PackageListActivityViewModel, PackageListModel> {

    private CommandExecutor<Long, MenuItem> onMenuItemSelectedExecutor;

    @Override
    public void initialize(PackageListActivityViewModel viewModel) {
        super.initialize(viewModel);
        getViewModel().invalidateViews();
        onMenuItemSelectedExecutor = createOnMenuItemSelectedCommandExecutor();
    }


    private CommandExecutor<Long, MenuItem> createOnMenuItemSelectedCommandExecutor() {
        CommandExecutor<Long, MenuItem> commandExecutor = new CommandExecutor<>();
        Command<MenuItem, Void> command = createOnToolbarBackClicked();
        commandExecutor.put((long) android.R.id.home, command);
        command = createOnNotificationClick();
        commandExecutor.put((long) R.id.pickup_menu_notification, command);
        return commandExecutor;
    }

    private Command<MenuItem, Void> createOnToolbarBackClicked() {
        return new Command<MenuItem, Void>() {
            @Override
            public Void execute(MenuItem p) {
                getHostActivity().onBackPressed();
                return null;
            }
        };
    }

    private Command<MenuItem, Void> createOnNotificationClick() {
        return new Command<MenuItem, Void>() {
            @Override
            public Void execute(MenuItem item) {
                notifyToOpenNotificationListActivity();
                return null;
            }

            private void notifyToOpenNotificationListActivity() {
                ActivityActionRequest request = new ActivityActionRequest(ActionType.START_ACTIVITY);
                request.action(NotificationActivity.class);
                EventBuilder eventBuilder = new EventBuilder(R.id.startActivityAction, request);
                getFeature().startActivityActionRequest(
                        eventBuilder.execute(PackageListActivityPresenter.this));
            }
        };
    }

    @Executable(R.id.onCreateOptionsMenu)
    void onCreateOptionMenu(Message message) {
        Menu menu = message.getContent();
        getViewModel().menuItem = menu.findItem(R.id.pickup_menu_notification);
        getViewModel().invalidateViews();
    }


    @Executable(R.id.onOptionItemSelected)
    void onOptionItemSelected(Message message) {
        long id = message.getId();
        MenuItem item = message.getContent();
        onMenuItemSelectedExecutor.execute(id, item);
    }

    @Executable(R.id.onNotificationReceived)
    void onNotificationReceived(Message message) {
        getViewModel().invalidateViews();
        getHostActivity().invalidateOptionsMenu();
        Event event = new Event.Builder(R.id.onHandleNotification).message(message).build();
        notifyObservers(event);
    }
}
