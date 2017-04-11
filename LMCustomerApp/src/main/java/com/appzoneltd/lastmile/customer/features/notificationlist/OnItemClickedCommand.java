package com.appzoneltd.lastmile.customer.features.notificationlist;

import android.os.Bundle;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.cutomerappsystem.Features;
import com.appzoneltd.lastmile.customer.features.tracking.host.TrackingActivity;
import com.appzoneltd.lastmile.customer.firebase.NotificationCounterChanger;
import com.appzoneltd.lastmile.customer.subfeatures.notificationcommands.RatingDialogHandler;
import com.appzoneltd.lastmile.customer.subfeatures.notificationcommands.RatingParamsGenerator;
import com.base.abstraction.commands.Command;
import com.base.abstraction.concurrency.Future;
import com.base.abstraction.events.Event;
import com.base.abstraction.events.EventBuilder;
import com.base.abstraction.events.Message;
import com.base.abstraction.serializers.StringJsonParser;
import com.base.abstraction.system.AppResources;
import com.base.presentation.listeners.OnListItemEventListenerParams;
import com.base.presentation.requests.ActionType;
import com.base.presentation.requests.ActivityActionRequest;
import com.entities.Notification;
import com.entities.cached.PayloadActiveVehicleDetails;
import com.entities.cached.Rating;
import com.entities.cached.RatingRequestParams;
import com.entities.cached.Receipt;

/**
 * Created by Wafaa on 1/3/2017.
 */

public class OnItemClickedCommand implements Command<OnListItemEventListenerParams, Void> {

    NotificationFragmentPresenter presenter;

    OnItemClickedCommand(NotificationFragmentPresenter presenter) {
        this.presenter = presenter;
    }


    @Override
    public Void execute(OnListItemEventListenerParams params) {
        Notification notification = (Notification) params.getEntity();
        startActivityBasedOnType(notification);
        return null;
    }

    private void startActivityBasedOnType(Notification notificationItem) {
        presenter.getModel().notification = notificationItem;
        if (notificationItem.getType().equals(AppResources.string(R.string.notification_driver_on_his_way))) {
            PayloadActiveVehicleDetails details = new StringJsonParser<>(PayloadActiveVehicleDetails.class)
                    .execute(notificationItem.getPayload());
            requestIsTrackedRequest(details);
        } else if (notificationItem.getType().equals(
                AppResources.string(R.string.notification_invoice))) {
            Receipt receipt = new StringJsonParser<>(Receipt.class)
                    .execute(notificationItem.getPayload());
            startReceiptActivity(receipt);
        } else if (notificationItem.getType().equals(
                AppResources.string(R.string.notification_rating))) {
            Rating rating = extractPayload(notificationItem);
            showRatingDialog(rating);
        }
    }

    private void requestIsTrackedRequest(PayloadActiveVehicleDetails vehicleDetails) {
        Message message = new Message.Builder().content(vehicleDetails.getRequestId()).build();
        Event event = new Event.Builder(R.id.requestTrackedRequest).message(message).build();
        presenter.getModel().execute(event);
    }


    private void startReceiptActivity(Receipt receipt) {
        ActivityActionRequest request = new ActivityActionRequest(ActionType.START_ACTIVITY);
        request.action(Features.ReceiptActivity.class);
        Bundle extras = new Bundle();
        extras.putSerializable(AppResources.string(R.string.INTENT_KEY_RECEIPT), receipt);
        request.extras(extras);
        EventBuilder eventBuilder = new EventBuilder(R.id.startActivityAction, request);
        presenter.getFeature().startActivityActionRequest(eventBuilder.execute(presenter));
        finishCurrentActivity();
    }

    private void finishCurrentActivity() {
        ActivityActionRequest request = new ActivityActionRequest(ActionType.FINISH);
        EventBuilder eventBuilder = new EventBuilder(R.id.startActivityAction, request);
        presenter.getFeature().startActivityActionRequest(eventBuilder.execute(presenter));
    }

    private void showRatingDialog(Rating rating) {
        Future<RatingRequestParams> future = new RatingDialogHandler()
                .execute(presenter.getHostActivity());
        future.onComplete(createOnCompleteCommand(rating));
        new NotificationCounterChanger().execute(null);
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
}
