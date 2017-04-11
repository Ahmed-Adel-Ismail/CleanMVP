package com.appzoneltd.lastmile.customer.features.notificationlist.host;

import android.support.annotation.NonNull;
import android.view.Menu;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.annotations.MenuGroup;
import com.appzoneltd.lastmile.customer.features.notificationlist.NotificationListModel;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.abstraction.events.Event;
import com.base.abstraction.events.Message;
import com.base.presentation.base.presentation.Presenter;

/**
 * Created by Wafaa on 11/23/2016.
 */

class NotificationActivityPresenter extends Presenter
        <NotificationActivityPresenter, NotificationActivityViewModel, NotificationListModel> {

    public NotificationActivityPresenter(NotificationActivityViewModel viewModel) {
        super(viewModel);
    }

    @Override
    protected CommandExecutor<Long, Message> createCommandExecutor() {
        CommandExecutor<Long, Message> commandExecutor = new CommandExecutor<>();
        Command<Message, Void> command = createOnCreateMenuCommand();
        commandExecutor.put((long) R.id.onCreateOptionsMenu, command);
        command = createOnNotificationReceived();
        commandExecutor.put((long) R.id.onNotificationReceived, command);
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
                setMenuGroupVisibility(menu);
                return null;
            }

            private void setMenuGroupVisibility(Menu menu) {
                Class<?> klass = getHostActivity().getClass();
                boolean visible = klass.isAnnotationPresent(MenuGroup.class);
                menu.setGroupVisible(R.id.pickup_menu_group, visible);
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

    private Command<Message, Void> onDialogCancelRequestFailure() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                long id = message.getId();
                if (id == R.id.onDialogPositiveClick) {
                    requestCancelPickupRequest();
                    getModel().updateNotificationsInModel(true);
                    onUpdateViewModel();
                    notifyWithTryAgainClick();
                }
                return null;
            }

            private void requestCancelPickupRequest() {
                Message message = new Message.Builder().content(getModel().notification).build();
                Event event = new Event.Builder(R.id.requestCancelPickupRequest)
                        .message(message).build();
                getModel().execute(event);
            }

            private void notifyWithTryAgainClick() {
                notifyObservers(
                        new Event.Builder(R.id.onDialogCancelRequestFailureTryAgainClicked)
                                .build());
            }

        };
    }


}
