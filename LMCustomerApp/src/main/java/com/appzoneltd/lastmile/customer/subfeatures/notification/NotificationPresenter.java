package com.appzoneltd.lastmile.customer.subfeatures.notification;

import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.annotations.MenuGroup;
import com.appzoneltd.lastmile.customer.cutomerappsystem.Features;
import com.appzoneltd.lastmile.customer.features.notificationlist.host.NotificationActivity;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.abstraction.events.EventBuilder;
import com.base.abstraction.events.Message;
import com.base.presentation.base.presentation.Presenter;
import com.base.presentation.models.Model;
import com.base.presentation.requests.ActionType;
import com.base.presentation.requests.ActivityActionRequest;


/**
 * Created by Wafaa on 11/23/2016.
 */


public class NotificationPresenter<M extends Model>
        extends Presenter<NotificationPresenter<M>, NotificationViewModel, M> {

    private final CommandExecutor<Long, MenuItem> onMenuItemSelectedExecutor;

    public NotificationPresenter(NotificationViewModel viewModel) {
        super(viewModel);
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
                getFeature().startActivityActionRequest(eventBuilder.execute(NotificationPresenter.this));
            }
        };
    }

    @Override
    protected CommandExecutor<Long, Message> createCommandExecutor() {
        CommandExecutor<Long, Message> commandExecutor = new CommandExecutor<>();
        Command<Message, Void> command;
        command = createOnCreateMenuCommand();
        commandExecutor.put((long) R.id.onCreateOptionsMenu, command);
        command = createOnMenuItemSelectedCommand();
        commandExecutor.put((long) R.id.onOptionItemSelected, command);
        command = createOnResumeCommand();
        commandExecutor.put((long) R.id.onResume, command);
        command = createOnNotificationReceivedCommand();
        commandExecutor.put((long) R.id.onNotificationReceived, command);
        return commandExecutor;
    }


    @NonNull
    private Command<Message, Void> createOnCreateMenuCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                Menu menu = message.getContent();
                getViewModel().notificationItem = menu.findItem(R.id.pickup_menu_notification);
                setMenuGroupVisibility(menu);
                getViewModel().invalidateViews();
                return null;
            }

            private void setMenuGroupVisibility(Menu menu) {
                Class<?> klass = getHostActivity().getClass();
                boolean visible = klass.isAnnotationPresent(MenuGroup.class);
                menu.setGroupVisible(R.id.pickup_menu_group, visible);
            }

        };
    }


    private Command<Message, Void> createOnMenuItemSelectedCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                long id = message.getId();
                MenuItem item = message.getContent();
                onMenuItemSelectedExecutor.execute(id, item);
                return null;
            }
        };
    }

    private Command<Message, Void> createOnNotificationReceivedCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                getViewModel().invalidateViews();
                getHostActivity().invalidateOptionsMenu();
                return null;
            }
        };
    }

    private Command<Message, Void> createOnResumeCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message msg) {
                getHostActivity().invalidateOptionsMenu();
                return null;
            }
        };
    }



}
