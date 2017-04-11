package com.appzoneltd.lastmile.customer.features.notificationlist;

import android.os.Bundle;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.features.tracking.host.TrackingActivity;
import com.appzoneltd.lastmile.customer.subfeatures.notificationcommands.NotificationsItemDeletion;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.abstraction.events.EventBuilder;
import com.base.abstraction.serializers.StringJsonParser;
import com.base.abstraction.system.AppResources;
import com.base.cached.ServerMessage;
import com.base.presentation.requests.ActionType;
import com.base.presentation.requests.ActivityActionRequest;
import com.base.presentation.views.dialogs.EventDialog;
import com.base.presentation.views.dialogs.EventDialogBuilder;
import com.base.usecases.events.ResponseMessage;
import com.entities.Notification;
import com.entities.cached.PayloadActiveVehicleDetails;

/**
 * Created by Wafaa on 2/14/2017.
 */

public class NotificationResponseHandler extends CommandExecutor<Long, ResponseMessage> {

    private NotificationFragmentPresenter presenter;
    private NotificationFragmentViewModel viewModel;

    NotificationResponseHandler(NotificationFragmentPresenter presenter
            , NotificationFragmentViewModel viewModel) {
        this.presenter = presenter;
        this.viewModel = viewModel;
        Command<ResponseMessage, Void> command = createOnCancelRequestResponse();
        put((long) R.id.requestCancelPickupRequest, command);
        command = createOnRatingResponseCommand();
        put((long) R.id.requestRating, command);
        command = createOnTrackedRequestResponse();
        put((long) R.id.requestTrackedRequest, command);
    }

    private Command<ResponseMessage, Void> createOnCancelRequestResponse() {
        return new Command<ResponseMessage, Void>() {
            @Override
            public Void execute(ResponseMessage message) {
                presenter.getModel().updateNotificationsInModel(false);
                presenter.onUpdateViewModel();
                if (message.isSuccessful()) {
                    deleteNotificationItem();
                    viewModel.invalidateView(R.id.frame_notification_layout);
                } else {
                    createAndShowFailureDialog();
                    viewModel.invalidateView(R.id.notification_list);
                }
                return null;
            }

            private void deleteNotificationItem() {
                new NotificationsItemDeletion<>(viewModel.notification).execute(Notification.class);
            }

            private void createAndShowFailureDialog() {
                EventDialogBuilder dialogBuilder;
                dialogBuilder = new EventDialogBuilder(R.id.dialogCancelRequestFailure);
                dialogBuilder.setTitle(R.string.dialog_title_cancel_pickup_response_failure);
                dialogBuilder.setMessage(R.string.dialog_title_cancel_pickup_response_failure_msg);
                dialogBuilder.setPositiveText(R.string.dialog_title_cancel_pickup_response_positive);
                dialogBuilder.setNegativeText(R.string.cancel);
                EventDialog eventDialog = new EventDialog(dialogBuilder, presenter.getHostActivity());
                eventDialog.show();
            }
        };
    }

    private Command<ResponseMessage, Void> createOnRatingResponseCommand() {
        return new Command<ResponseMessage, Void>() {
            @Override
            public Void execute(ResponseMessage message) {
                if (message.isSuccessful()) {
                    new NotificationsItemDeletion<>(presenter.getModel().notification).execute(Notification.class);
                }
                finishActivity();
                return null;
            }

            private void finishActivity() {
                ActivityActionRequest request = new ActivityActionRequest(ActionType.FINISH);
                EventBuilder eventBuilder = new EventBuilder(R.id.startActivityAction, request);
                presenter.getHostActivity().startActivityActionRequest(eventBuilder.execute(presenter));
            }
        };
    }

    private Command<ResponseMessage, Void> createOnTrackedRequestResponse() {
        return new Command<ResponseMessage, Void>() {
            @Override
            public Void execute(ResponseMessage message) {
                presenter.getViewModel().invalidateView(R.id.notification_list);
                if(message.isSuccessful()) {
                    ServerMessage serverMessage = message.getContent();
                    if (serverMessage.getMessage().equals("true")) {
                        PayloadActiveVehicleDetails details = new StringJsonParser<>(PayloadActiveVehicleDetails.class)
                                .execute(presenter.getModel().notification.getPayload());
                        startTrackActivity(details);
                    }
                }
                return null;
            }

            private void startTrackActivity(PayloadActiveVehicleDetails payloadActiveVehicleDetails) {
                ActivityActionRequest request = new ActivityActionRequest(ActionType.START_ACTIVITY);
                request.action(TrackingActivity.class);
                Bundle extras = new Bundle();
                extras.putSerializable(
                        AppResources.string(R.string.INTENT_KEY_ACTIVE_VEHICLE_DETAILS)
                        , payloadActiveVehicleDetails);
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
        };
    }

}
