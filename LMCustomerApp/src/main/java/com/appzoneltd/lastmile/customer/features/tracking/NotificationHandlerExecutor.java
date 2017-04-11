package com.appzoneltd.lastmile.customer.features.tracking;

import android.support.v7.app.AlertDialog;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.firebase.NotificationCounterChanger;
import com.appzoneltd.lastmile.customer.subfeatures.notificationcommands.RatingDialogHandler;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.abstraction.concurrency.Future;
import com.base.abstraction.events.Event;
import com.base.abstraction.events.Message;
import com.base.abstraction.serializers.StringJsonParser;
import com.base.presentation.references.CheckedProperty;
import com.base.presentation.views.dialogs.CustomEventDialog;
import com.base.presentation.views.dialogs.CustomEventDialogBuilder;
import com.base.presentation.views.dialogs.EventDialog;
import com.base.presentation.views.dialogs.EventDialogBuilder;
import com.entities.Notification;
import com.entities.cached.PayloadActiveVehicleDetails;
import com.entities.cached.PayloadBusy;
import com.entities.cached.Rating;
import com.entities.cached.RatingRequestParams;

/**
 * a {@link CommandExecutor} that handle received notification types
 * <p>
 * Created by Wafaa on 12/23/2016.
 */

class NotificationHandlerExecutor extends CommandExecutor<Long, Message> {

    private TrackingFragmentPresenter presenter;
    private TrackingFragmentViewModel viewModel;
    private final CheckedProperty<AlertDialog> alertDialogRef = new CheckedProperty<>();

    NotificationHandlerExecutor(TrackingFragmentPresenter presenter,
                                TrackingFragmentViewModel viewModel) {

        this.presenter = presenter;
        this.viewModel = viewModel;
        Command<Message, Void> command = createOnBusyNotificationCommand();
        put((long) R.id.onNotifiedDriverBusy, command);
        command = createOnAssignedNotificationCommand();
        put((long) R.id.onNotifiedDriverAssigned, command);
        command = createDriverOnHisWayNotificationCommand();
        put((long) R.id.onNotifiedDriverOnHisWay, command);
        command = createDriverArrivedNotificationCommand();
        put((long) R.id.onNotifiedDriverArrived, command);
        put((long) R.id.onNotifiedDriverRating, createOnRatingNotificationCommand());
        put((long) R.id.onNotifiedRequestCancelled, createOnCancelledNotificationCommand());

    }

    private Command<Message, Void> createOnBusyNotificationCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                Notification notification = message.getContent();
                presenter.getModel().notification.set(notification);
                if (notification != null) {
                    handleBusyNotification(notification);
                }
                return null;
            }

            private void handleBusyNotification(Notification notification) {
                PayloadBusy payloadBusy = extractPayload(notification);
                if (payloadBusy != null) {
                    presenter.getModel().packageId.set(payloadBusy.getPackageId());
                    createAlertDialog(notification);
                }
            }

            private PayloadBusy extractPayload(Notification notification) {
                return new StringJsonParser<>(PayloadBusy.class).execute(notification.getPayload());
            }

            private void createAlertDialog(Notification notification) {
                if (!alertDialogRef.isEmpty()) {
                    return;
                }
                CustomEventDialogBuilder dialogBuilder;
                dialogBuilder = new CustomEventDialogBuilder(R.id.onDialogNotificationDriverBusy);
                dialogBuilder.setTitleText(notification.getNotificationItemTitle());
                dialogBuilder.setMessageText(notification.getNotificationItemBody());
                dialogBuilder.setPositiveText(R.string.reschedule);
                dialogBuilder.setNeutralText(R.string.try_later);
                dialogBuilder.setNegativeText(R.string.cancel);
                CustomEventDialog eventDialog = new CustomEventDialog(
                        dialogBuilder, presenter.getHostActivity());
                AlertDialog alertDialog = eventDialog.show();
                alertDialogRef.set(alertDialog);
            }
        };
    }

    private Command<Message, Void> createOnAssignedNotificationCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message p) {
                new NotificationCounterChanger().execute(null);
                createConfirmationDialog(R.id.onDialogNotificationDriverAssigned
                        , R.string.driver_assigned
                        , R.string.driver_assigned_content);
                return null;
            }

        };
    }

    private Command<Message, Void> createDriverOnHisWayNotificationCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                Notification notification = message.getContent();
                if (notification != null) {
                    invalidateDriverLayout(notification);
                    invalidateMapProgress();
                    Event event = new Event.Builder(R.id.requestSearchTopic).build();
                    presenter.getModel().execute(event);
                }
                return null;
            }

            private void invalidateDriverLayout(Notification notification) {
                PayloadActiveVehicleDetails payloadActiveVehicleDetails;
                payloadActiveVehicleDetails =
                        new StringJsonParser<>(PayloadActiveVehicleDetails.class)
                                .execute(notification.getPayload());
                if (payloadActiveVehicleDetails != null) {
                    presenter.getModel().payloadActiveVehicleDetails = payloadActiveVehicleDetails;
                    presenter.onUpdateViewModel();
                    presenter.getViewModel().invalidateView(R.id.driver_layout);
                }
            }

            private void invalidateMapProgress() {
                presenter.getViewModel().showProgress = true;
                presenter.getViewModel().invalidateView(R.id.fragment_tracking_loading);
            }
        };
    }

    private Command<Message, Void> createDriverArrivedNotificationCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                presenter.getModel().subject.get().onComplete();
                presenter.getModel().webSocketOpened.set(false);
                createConfirmationDialog(R.id.onDialogNotificationDriverArrived
                        , R.string.driver_arrived
                        , R.string.driver_arrives_msg);
                return null;
            }
        };
    }

    private void createConfirmationDialog(int id
            , int titleResourceId
            , int msgResourceId) {
        if (!alertDialogRef.isEmpty()) {
            return;
        }
        EventDialogBuilder dialogBuilder = new EventDialogBuilder(id);
        dialogBuilder.setTitle(titleResourceId);
        dialogBuilder.setMessage(msgResourceId);
        dialogBuilder.setPositiveText(R.string.ok);
        EventDialog eventDialog = new EventDialog(dialogBuilder
                , presenter.getHostActivity());
        AlertDialog alertDialog = eventDialog.show();
        alertDialogRef.set(alertDialog);
    }

    private Command<Message, Void> createOnRatingNotificationCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                if (!alertDialogRef.isEmpty()) {
                    return null;
                }
                Notification notification = message.getContent();
                presenter.getModel().notification.set(notification);
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
                            presenter.getModel().rating.set(message);
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

    private Command<Message, Void> createOnCancelledNotificationCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                Notification notification = message.getContent();
                if (notification != null) {
                    createAlertDialog(notification);
                }
                return null;
            }

            private void createAlertDialog(Notification notification) {
                if (!alertDialogRef.isEmpty()) {
                    return;
                }
                CustomEventDialogBuilder dialogBuilder;
                dialogBuilder = new CustomEventDialogBuilder(R.id.onDialogNotificationRquestCancelled);
                dialogBuilder.setTitleText(notification.getNotificationItemTitle());
                dialogBuilder.setMessageText(notification.getNotificationItemBody());
                dialogBuilder.setPositiveText(R.string.ok);
                EventDialog eventDialog = new CustomEventDialog(
                        dialogBuilder, presenter.getHostActivity());
                AlertDialog alertDialog = eventDialog.show();
                alertDialogRef.set(alertDialog);
            }

        };
    }

}
