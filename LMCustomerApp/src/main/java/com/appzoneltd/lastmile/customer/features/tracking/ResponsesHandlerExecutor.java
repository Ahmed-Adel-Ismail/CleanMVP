package com.appzoneltd.lastmile.customer.features.tracking;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.firebase.NotificationCounterChanger;
import com.appzoneltd.lastmile.customer.subfeatures.notificationcommands.NotificationsItemDeletion;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.abstraction.events.Event;
import com.base.abstraction.events.EventBuilder;
import com.base.abstraction.events.Message;
import com.base.abstraction.system.App;
import com.base.presentation.requests.ActionType;
import com.base.presentation.requests.ActivityActionRequest;
import com.base.presentation.views.dialogs.EventDialog;
import com.base.presentation.views.dialogs.EventDialogBuilder;
import com.base.usecases.events.ResponseMessage;
import com.entities.Notification;

/**
 * a {@link CommandExecutor} responsible for handel on Response of
 * {@link com.appzoneltd.lastmile.customer.features.tracking.TrackingFragment}
 * <p>
 * Created by Wafaa on 12/23/2016.
 */

class ResponsesHandlerExecutor extends CommandExecutor<Long, ResponseMessage> {

    private TrackingFragmentPresenter presenter;
    private TrackingFragmentViewModel viewModel;

    ResponsesHandlerExecutor(TrackingFragmentPresenter presenter
            , TrackingFragmentViewModel viewModel) {
        this.presenter = presenter;
        this.viewModel = viewModel;
        Command<ResponseMessage, Void> command = createOnCancelPickupRequestResponseCommand();
        put((long) R.id.requestCancelPickupRequest, command);
        command = createOnWebSocketMessageResponse();
        put((long) R.id.requestOpenWebSocket, command);
        command = createOnRatingResponseCommand();
        put((long) R.id.requestRating, command);
    }

    private Command<ResponseMessage, Void> createOnCancelPickupRequestResponseCommand() {
        return new Command<ResponseMessage, Void>() {
            @Override
            public Void execute(ResponseMessage msg) {
                if (msg.isSuccessful()) {
                    notifyToFinishActivity();
                    deleteNotificationItem();
                    decreaseNotificationCount();
                } else {
                    createAndShowFailureDialog();
                }
                return null;
            }

            private void notifyToFinishActivity() {
                ActivityActionRequest request = new ActivityActionRequest(ActionType.FINISH);
                EventBuilder eventBuilder = new EventBuilder(R.id.startActivityAction, request);
                presenter.getFeature().startActivityActionRequest(eventBuilder.execute(presenter));
            }

            private void deleteNotificationItem() {
                new NotificationsItemDeletion<Notification>(presenter.loadNotificationFromPref())
                        .execute(Notification.class);
            }

            private void decreaseNotificationCount() {
                new NotificationCounterChanger().execute(null);
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

    private Command<ResponseMessage, Void> createOnWebSocketMessageResponse() {
        return new Command<ResponseMessage, Void>() {
            @Override
            public Void execute(ResponseMessage message) {
                hideMapProgress();
                if(presenter.getModel().destinationLatLng != null){
                    viewModel.drawRoute(presenter.getModel().destinationLatLng);
                }
                return null;
            }

        };
    }

    private void hideMapProgress() {
        presenter.invalidateMapProgress(false);
        presenter.getViewModel().invalidateView(R.id.fragment_tracking_loading);
    }

    private Command<ResponseMessage, Void> createOnRatingResponseCommand() {
        return new Command<ResponseMessage, Void>() {
            @Override
            public Void execute(ResponseMessage message) {
                if (message.isSuccessful()) {
                    new NotificationsItemDeletion<>(presenter.getModel().notification.get()).execute(Notification.class);
                    notifyToShowToast(R.string.rating_sucess_response_msg);
                } else {
                    notifyToShowToast(R.string.rating_failed_response_msg);
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

    private void notifyToShowToast(int msgResource) {
        Event event = new Event.Builder(R.id.showToast).message(new Message.Builder()
                .content(msgResource).build())
                .receiverActorAddresses(R.id.addressActivity)
                .build();
        App.getInstance().getActorSystem()
                .get((long) R.id.addressActivity)
                .execute(event);
    }

}
