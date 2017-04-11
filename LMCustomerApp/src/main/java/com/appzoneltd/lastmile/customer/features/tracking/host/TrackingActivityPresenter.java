package com.appzoneltd.lastmile.customer.features.tracking.host;

import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.features.notificationlist.host.NotificationActivity;
import com.appzoneltd.lastmile.customer.features.tracking.model.TrackingModel;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.abstraction.events.Event;
import com.base.abstraction.events.EventBuilder;
import com.base.abstraction.events.Message;
import com.base.presentation.base.presentation.Presenter;
import com.base.presentation.requests.ActionType;
import com.base.presentation.requests.ActivityActionRequest;

/**
 * Created by Wafaa on 11/22/2016.
 */

public class TrackingActivityPresenter extends
        Presenter<TrackingActivityPresenter, TrackingActivityViewModel, TrackingModel> {

    private final CommandExecutor<Long, MenuItem> onMenuItemSelectedExecutor;

    public TrackingActivityPresenter(TrackingActivityViewModel viewModel) {
        super(viewModel);
        onMenuItemSelectedExecutor = createOnMenuItemSelectedCommandExecutor();
        getViewModel().invalidateViews();
    }

    private CommandExecutor<Long, MenuItem> createOnMenuItemSelectedCommandExecutor() {
        CommandExecutor<Long, MenuItem> commandExecutor = new CommandExecutor<>();
        Command<MenuItem, Void> command = createOnNotificationIconSelectedCommand();
        commandExecutor.put((long) R.id.pickup_menu_notification, command);
        command = createOnToolbarBackClicked();
        commandExecutor.put((long) android.R.id.home, command);
        return commandExecutor;
    }

    private Command<MenuItem, Void> createOnNotificationIconSelectedCommand() {
        return new Command<MenuItem, Void>() {
            @Override
            public Void execute(MenuItem p) {
                notifyToOpenNotificationListActivity();
                return null;
            }

            private void notifyToOpenNotificationListActivity() {
                ActivityActionRequest request = new ActivityActionRequest(ActionType.START_ACTIVITY);
                request.action(NotificationActivity.class);
                EventBuilder eventBuilder = new EventBuilder(R.id.startActivityAction, request);
                getFeature().startActivityActionRequest(eventBuilder.execute(TrackingActivityPresenter.this));
            }
        };
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


    @Override
    protected CommandExecutor<Long, Message> createCommandExecutor() {
        CommandExecutor<Long, Message> commandExecutor = new CommandExecutor<>();
        Command<Message, Void> command = createOnCreateMenuCommand();
        commandExecutor.put((long) R.id.onCreateOptionsMenu, command);
        command = createOnMenuItemSelectedCommand();
        commandExecutor.put((long) R.id.onOptionItemSelected, command);
        command = createOnNotificationReceived();
        commandExecutor.put((long) R.id.onNotificationReceived, command);
        command = createOnNotificationDialogCommand();
        commandExecutor.put((long) R.id.onDialogNotificationDriverBusy, command);
        command = createOnConformationDialogCommand();
        commandExecutor.put((long) R.id.onDialogNotificationDriverArrived, command);
        commandExecutor.put((long) R.id.onDialogNotificationDriverAssigned, command);
        commandExecutor.put((long) R.id.onDialogNotificationRquestCancelled, command);
        command = onDialogCancelRequestFailure();
        commandExecutor.put((long) R.id.dialogCancelRequestFailure, command);
        return commandExecutor;
    }

    @NonNull
    private Command<Message, Void> createOnCreateMenuCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                Menu menu = message.getContent();
                getHostActivity().getMenuInflater().inflate(R.menu.pickup_menu, menu);
                return null;
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

    private Command<Message, Void> createOnNotificationReceived() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message p) {
                Event event = new Event.Builder(R.id.onHandleNotification).message(p).build();
                notifyObservers(event);
                return null;
            }
        };
    }

    private Command<Message, Void> createOnNotificationDialogCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                long id = message.getId();
                if (id == R.id.onDialogNegativeClick) {
                    requestCancelPickupRequest();
                }
                return null;
            }
        };
    }

    private void requestCancelPickupRequest() {
        Event event = new Event.Builder(R.id.requestCancelPickupRequest).build();
        getModel().execute(event);
    }

    private Command<Message, Void> createOnConformationDialogCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message p) {
                finishActivity();
                return null;
            }

            private void finishActivity() {
                ActivityActionRequest request = new ActivityActionRequest(ActionType.FINISH);
                EventBuilder eventBuilder = new EventBuilder(R.id.startActivityAction, request);
                getFeature().startActivityActionRequest(eventBuilder.execute(TrackingActivityPresenter.this));
            }
        };
    }

    private Command<Message, Void> onDialogCancelRequestFailure() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                long id = message.getId();
                if (id == R.id.onDialogPositiveClick) {
                    requestCancelPickupRequest();
                } else {
                    Message msg = new Message.Builder()
                            .id(R.id.onNotifiedDriverBusy)
                            .content(getModel().notification.get())
                            .build();
                    notifyObservers(new Event.Builder(R.id.onHandleNotification).message(msg).build());
                }
                return null;
            }
        };
    }
}
