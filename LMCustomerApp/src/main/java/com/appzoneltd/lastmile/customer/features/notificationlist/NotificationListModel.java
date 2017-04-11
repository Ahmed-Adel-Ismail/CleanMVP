package com.appzoneltd.lastmile.customer.features.notificationlist;

import android.support.annotation.NonNull;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.subfeatures.notificationcommands.CancelRequestCommand;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.abstraction.events.Event;
import com.base.abstraction.events.Message;
import com.base.abstraction.exceptions.NotSavedInPreferencesException;
import com.base.abstraction.logs.Logger;
import com.base.abstraction.serializers.JsonSetLoader;
import com.base.abstraction.serializers.StringJsonParser;
import com.base.abstraction.system.App;
import com.base.cached.ServerMessage;
import com.base.presentation.annotations.interfaces.Sync;
import com.base.presentation.models.Model;
import com.base.presentation.repos.base.Repository;
import com.base.usecases.events.RequestMessage;
import com.base.usecases.events.ResponseMessage;
import com.entities.Notification;
import com.entities.cached.PayloadBusy;
import com.entities.cached.Rating;
import com.entities.cached.TrackedRequestParams;

import java.util.TreeSet;

/**
 * Created by Wafaa on 11/24/2016.
 */

public class NotificationListModel extends Model {

    public Notification notification;
    @Sync("notification_list")
    public TreeSet<Notification> notifications;
    Rating rating;

    @Override
    public void preInitialize() {
        notifications = retrieveListOfNotifications();
    }

    TreeSet<Notification> retrieveListOfNotifications()
            throws NotSavedInPreferencesException {
        TreeSet<Notification> notifications = new TreeSet<>();
        JsonSetLoader<Notification> loader =
                new JsonSetLoader<>(R.string.PREFS_KEY_NOTIFICATION_LIST);
        try {
            notifications = loader.execute(Notification.class);
        } catch (NotSavedInPreferencesException ex) {
            Logger.getInstance().error(NotificationListModel.class, ex.getMessage()
                    + " " + R.string.PREFS_KEY_NOTIFICATION_LIST);
        }
        return notifications;
    }

    public void updateNotificationsInModel(boolean requesting) {
        notifications.remove(notification);
        notification.setRequesting(requesting);
        notifications.add(notification);
    }

    @NonNull
    @Override
    protected Repository createRepository() {
        return new NotificationListRepository();
    }

    @NonNull
    @Override
    protected CommandExecutor<Long, Message> createOnViewsUpdatedCommands() {
        CommandExecutor<Long, Message> commandExecutor = new CommandExecutor<>();
        Command<Message, Void> command = createCancelRequestCommand();
        commandExecutor.put((long) R.id.requestCancelPickupRequest, command);
        command = createRatingRequestCommand();
        commandExecutor.put((long) R.id.requestRating, command);
        command = requestTrackedRequest();
        commandExecutor.put((long) R.id.requestTrackedRequest, command);
        return commandExecutor;
    }

    private Command<Message, Void> createCancelRequestCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message p) {
                Notification notification = p.getContent();
                PayloadBusy payloadBusy = extractPayload(notification);
                if (payloadBusy != null) {
                    long packageId = payloadBusy.getPackageId();
                    Message message = new Message.Builder().content(packageId).build();
                    new CancelRequestCommand(NotificationListModel.this).execute(message);
                }
                return null;
            }

            private PayloadBusy extractPayload(Notification notification) {
                return new StringJsonParser<>(PayloadBusy.class).execute(notification.getPayload());
            }

        };
    }

    private Command<Message, Void> createRatingRequestCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                RequestMessage requestMessage = new RequestMessage.Builder()
                        .content(rating).build();
                requestFromRepository(R.id.requestRating, requestMessage);
                return null;
            }

        };
    }

    private Command<Message, Void> requestTrackedRequest() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                long requestId = message.getContent();
                TrackedRequestParams trackedRequestParams = new TrackedRequestParams();
                trackedRequestParams.setId(requestId);
                RequestMessage requestMessage = new RequestMessage.Builder()
                        .content(trackedRequestParams).build();
                requestFromRepository(R.id.requestTrackedRequest, requestMessage);
                return null;
            }
        };
    }

    @NonNull
    @Override
    protected CommandExecutor<Long, ResponseMessage> createOnRepositoryUpdatedCommands() {
        CommandExecutor<Long, ResponseMessage> commandExecutor = new CommandExecutor<>();
        Command<ResponseMessage, Void> command = createOnCancelPickupRequestResponseCommand();
        commandExecutor.put((long) R.id.requestCancelPickupRequest, command);
        command = createOnRatingResponse();
        commandExecutor.put((long) R.id.requestRating, command);
        command = onTrackerRequestResponse();
        commandExecutor.put((long) R.id.requestTrackedRequest, command);
        return commandExecutor;
    }

    private Command<ResponseMessage, Void> createOnCancelPickupRequestResponseCommand() {
        return new Command<ResponseMessage, Void>() {
            @Override
            public Void execute(ResponseMessage message) {
                notifyOnRepositoryResponse(message);
                return null;
            }
        };
    }

    private Command<ResponseMessage, Void> createOnRatingResponse() {
        return new Command<ResponseMessage, Void>() {
            @Override
            public Void execute(ResponseMessage message) {
                if (message.isSuccessful()) {
                    notifyToShowToast(R.string.rating_sucess_response_msg);
                } else {
                    notifyToShowToast(R.string.rating_failed_response_msg);
                }
                notifyOnRepositoryResponse(message);
                return null;
            }
        };
    }

    private Command<ResponseMessage, Void> onTrackerRequestResponse() {
        return new Command<ResponseMessage, Void>() {
            @Override
            public Void execute(ResponseMessage message) {
                if(message.isSuccessful()) {
                    ServerMessage serverMessage = message.getContent();
                    if (serverMessage.getMessage().equals("false")) {
                        updateNotificationsInModel(false);
                        notifyToShowToast(R.string.trackedResponseErrorMsg);
                    }
                } else {
                    notifyToShowToast(R.string.s_failed_msg);
                }
                notifyOnRepositoryResponse(message);
                return null;
            }
        };
    }

    private void notifyToShowToast(int msgResource) {
        Event event = new Event.Builder(R.id.showToast).message(new Message.Builder()
                .content(msgResource).build())
                .receiverActorAddresses(R.id.addressActivity).build();
        App.getInstance().getActorSystem()
                .get((long) R.id.addressActivity)
                .execute(event);
    }

    @Override
    public void onClear() {

    }
}
