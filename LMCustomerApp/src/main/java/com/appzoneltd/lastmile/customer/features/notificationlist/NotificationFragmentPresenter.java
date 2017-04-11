package com.appzoneltd.lastmile.customer.features.notificationlist;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.firebase.NotificationCounterChanger;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.abstraction.events.Event;
import com.base.abstraction.events.Message;
import com.base.abstraction.system.App;
import com.base.abstraction.system.Preferences;
import com.base.presentation.base.presentation.Presenter;
import com.base.presentation.listeners.OnListItemEventListenerParams;
import com.base.usecases.events.ResponseMessage;
import com.entities.Notification;

/**
 * Created by Wafaa on 11/14/2016.
 */

class NotificationFragmentPresenter extends Presenter<NotificationFragmentPresenter, NotificationFragmentViewModel, NotificationListModel> {

    private final CommandExecutor<Long, OnListItemEventListenerParams> onItemListSelectedExecutor;

    NotificationFragmentPresenter(NotificationFragmentViewModel viewModel) {
        super(viewModel);
        onUpdateViewModel();
        onItemListSelectedExecutor = createOnItemListSelectedCommandExecutor();
        getViewModel().invalidateView(R.id.notification_list);
        getViewModel().invalidateView(R.id.fragment_notification_empty_msg);
    }

    private CommandExecutor<Long, OnListItemEventListenerParams> createOnItemListSelectedCommandExecutor() {
        CommandExecutor<Long, OnListItemEventListenerParams> commandExecutor = new CommandExecutor<>();
        Command<OnListItemEventListenerParams, Void> command = createOnCancelSelectedCommand();
        commandExecutor.put((long) R.id.notification_cancel, command);
        commandExecutor.put((long) R.id.notification_layout, new OnItemClickedCommand(this));
        return commandExecutor;
    }

    private Command<OnListItemEventListenerParams, Void> createOnCancelSelectedCommand() {
        return new Command<OnListItemEventListenerParams, Void>() {
            @Override
            public Void execute(OnListItemEventListenerParams params) {
                Notification notification = (Notification) params.getEntity();
                getViewModel().notification = (Notification) params.getEntity();
                getViewModel().itemPosition = params.getPosition();
                requestCancelPickupRequest(notification);
                return null;
            }

            private void requestCancelPickupRequest(Notification notification) {
                Message message = new Message.Builder().content(notification).build();
                Event event = new Event.Builder(R.id.requestCancelPickupRequest)
                        .message(message).build();
                getModel().execute(event);
            }
        };
    }

    @Override
    protected CommandExecutor<Long, ResponseMessage> createResponseCommands() {
        return new NotificationResponseHandler(this, getViewModel());
    }


    @Override
    protected CommandExecutor<Long, Message> createCommandExecutor() {
        CommandExecutor<Long, Message> commandExecutor = new CommandExecutor<>();
        Command<Message, Void> command = createOnItemEventListener();
        commandExecutor.put((long) R.id.onListItemEventListener, command);
        command = createOnResumeCommand();
        commandExecutor.put((long) R.id.onResume, command);
        command = createOnNotificationReceived();
        commandExecutor.put((long) R.id.onHandleNotification, command);
        command = onDialogTryAgainClicked();
        commandExecutor.put((long) R.id.onDialogCancelRequestFailureTryAgainClicked, command);
        return commandExecutor;
    }

    private Command<Message, Void> createOnItemEventListener() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                OnListItemEventListenerParams params = message.getContent();
                long id = params.getView().getId();
                onItemListSelectedExecutor.execute(id, params);
                return null;
            }
        };
    }

    private Command<Message, Void> createOnResumeCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message msg) {
                zeroNotificationCount();
                return null;
            }

            private void zeroNotificationCount() {
                Preferences.getInstance().save(R.string.PREFS_KEY_NOTIFICATION_COUNTER, 0);
            }
        };
    }

    private Command<Message, Void> createOnNotificationReceived() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message p) {
                getModel().notifications = getModel().retrieveListOfNotifications();
                onUpdateViewModel();
                getViewModel().invalidateView(R.id.notification_list);
                new NotificationCounterChanger().execute(null);
                return null;
            }

        };
    }


    private void notifyToShowToast(int msgResource) {
        Event event = new Event.Builder(R.id.showToast).message(new Message.Builder()
                .content(msgResource).build()).build();
        App.getInstance().getActorSystem()
                .get((long) R.id.addressActivity)
                .execute(event);
    }

    private Command<Message, Void> onDialogTryAgainClicked() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message p) {
                getViewModel().invalidateView(R.id.notification_list);
                return null;
            }
        };
    }


}
