package com.appzoneltd.lastmile.customer.features.main.models;

import android.support.annotation.NonNull;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.subfeatures.notificationcommands.NotificationsItemDeletion;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.abstraction.events.Event;
import com.base.abstraction.events.Message;
import com.base.abstraction.system.App;
import com.base.presentation.models.Model;
import com.base.presentation.repos.base.Repository;
import com.base.usecases.events.RequestMessage;
import com.base.usecases.events.ResponseMessage;
import com.entities.Notification;
import com.entities.cached.RatingRequestParams;

/**
 * Created by Wafaa on 12/29/2016.
 */


public class MainModel extends Model {

    public RatingRequestParams rating;
    public Notification notification;

    public MainModel() {

    }

    @Override
    protected Repository createRepository() {
        return new MainRepository();
    }

    @NonNull
    @Override
    protected CommandExecutor<Long, Message> createOnViewsUpdatedCommands() {
        CommandExecutor<Long, Message> commandExecutor = new CommandExecutor<>();
        Command<Message, Void> command = createRatingRequestCommand();
        commandExecutor.put((long) R.id.requestRating, command);
        return commandExecutor;
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

    @NonNull
    @Override
    protected CommandExecutor<Long, ResponseMessage> createOnRepositoryUpdatedCommands() {
        CommandExecutor<Long, ResponseMessage> commandExecutor = new CommandExecutor<>();
        Command<ResponseMessage, Void> command = createOnRatingResponseCommand();
        commandExecutor.put((long) R.id.requestRating, command);
        return commandExecutor;
    }

    private Command<ResponseMessage, Void> createOnRatingResponseCommand() {
        return new Command<ResponseMessage, Void>() {
            @Override
            public Void execute(ResponseMessage message) {
                if (message.isSuccessful()) {
                    new NotificationsItemDeletion<>(notification).execute(Notification.class);
                    notifyToShowToast(R.string.rating_sucess_response_msg);
                } else {
                    notifyToShowToast(R.string.rating_failed_response_msg);
                }
                return null;
            }

            private void notifyToShowToast(int msgResource) {
                Event event = new Event.Builder(R.id.showToast).message(new Message.Builder()
                        .content(msgResource).build()).receiverActorAddresses(R.id.addressActivity).build();
                App.getInstance().getActorSystem()
                        .get((long) R.id.addressActivity)
                        .execute(event);
            }
        };
    }

    @Override
    public void onClear() {

    }
}
