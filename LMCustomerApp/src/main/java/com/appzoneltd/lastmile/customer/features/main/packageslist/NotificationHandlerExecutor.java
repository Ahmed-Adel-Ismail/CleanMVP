package com.appzoneltd.lastmile.customer.features.main.packageslist;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.subfeatures.notificationcommands.RatingDialogHandler;
import com.appzoneltd.lastmile.customer.subfeatures.notificationcommands.RatingParamsGenerator;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.abstraction.concurrency.Future;
import com.base.abstraction.events.EventBuilder;
import com.base.abstraction.events.Message;
import com.base.abstraction.serializers.StringJsonParser;
import com.entities.Notification;
import com.entities.cached.Rating;
import com.entities.cached.RatingRequestParams;

/**
 * Created by Wafaa on 1/4/2017.
 */

public class NotificationHandlerExecutor extends CommandExecutor<Long, Message> {

    private PackageListPresenter presenter;

    public NotificationHandlerExecutor(PackageListPresenter presenter) {
        this.presenter = presenter;
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
                if (future != null) {
                    future.onComplete(createOnCompleteCommand(rating));
                }
                return null;
            }

            private Command<RatingRequestParams, Void> createOnCompleteCommand(final Rating rating) {
                return new Command<RatingRequestParams, Void>() {
                    @Override
                    public Void execute(RatingRequestParams message) {
                        if (message != null) {
                            presenter.getModel().rating = new RatingParamsGenerator(rating)
                                    .execute(message);
                            presenter.getModel().execute(new EventBuilder(R.id.requestRating)
                                    .execute(presenter));
                        }
                        return null;
                    }
                };
            }

            private Rating extractPayload(Notification notification){
                return new StringJsonParser<>(Rating.class).execute(notification.getPayload());
            }
        };
    }

}
