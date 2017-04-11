package com.appzoneltd.lastmile.customer.features.main.host;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.firebase.NotificationCounterChanger;
import com.appzoneltd.lastmile.customer.subfeatures.notificationcommands.RatingDialogHandler;
import com.appzoneltd.lastmile.customer.subfeatures.notificationcommands.RatingParamsGenerator;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.abstraction.concurrency.Future;
import com.base.abstraction.events.Event;
import com.base.abstraction.events.Message;
import com.base.abstraction.serializers.StringJsonParser;
import com.entities.Notification;
import com.entities.cached.Rating;
import com.entities.cached.RatingRequestParams;

/**
 * a {@link CommandExecutor} that handle received notification types
 * <p>
 * Created by Wafaa on 12/23/2016.
 */

public class MainNotificationHandlerExecutor extends CommandExecutor<Long, Message> {

    private MainActivityPresenter presenter;
    private MainActivityViewModel viewModel;

    MainNotificationHandlerExecutor(MainActivityPresenter presenter,
                                    MainActivityViewModel viewModel) {

        this.presenter = presenter;
        this.viewModel = viewModel;
        put((long) R.id.onNotifiedDriverRating, createOnRatingNotificationCommand());

    }

    private Command<Message, Void> createOnRatingNotificationCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                Notification notification = message.getContent();
                presenter.getModel().notification = notification;
                Rating rating = extractPayload(notification);
                Future<RatingRequestParams> future = new RatingDialogHandler()
                        .execute(presenter.getHostActivity());
                future.onComplete(createOnCompleteCommand(rating));
                new NotificationCounterChanger().execute(null);
                return null;
            }

            private Command<RatingRequestParams, Void> createOnCompleteCommand(final Rating rating) {
                return new Command<RatingRequestParams, Void>() {
                    @Override
                    public Void execute(RatingRequestParams message) {
                        if (message != null) {
                            presenter.getModel().rating = new RatingParamsGenerator(rating)
                                    .execute(message);
                            Event event = new Event.Builder(R.id.requestRating).build();
                            presenter.getModel().execute(event);
                        }
                        return null;
                    }
                };
            }

            private Rating extractPayload(Notification notification) {
                return new StringJsonParser<>(Rating.class).execute(notification.getPayload());
            }
        };
    }

}
