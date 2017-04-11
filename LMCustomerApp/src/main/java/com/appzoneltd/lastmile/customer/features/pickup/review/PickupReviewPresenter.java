package com.appzoneltd.lastmile.customer.features.pickup.review;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.cutomerappsystem.Features;
import com.appzoneltd.lastmile.customer.features.pickup.models.PickupModel;
import com.appzoneltd.lastmile.customer.features.tracking.host.TrackingActivity;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.abstraction.events.EventBuilder;
import com.base.abstraction.events.Message;
import com.base.abstraction.system.App;
import com.base.abstraction.system.AppResources;
import com.base.presentation.base.presentation.Presenter;
import com.base.presentation.requests.ActionType;
import com.base.presentation.requests.ActivityActionRequest;
import com.base.presentation.views.dialogs.EventDialog;
import com.base.presentation.views.dialogs.EventDialogBuilder;
import com.base.usecases.events.ResponseMessage;
import com.entities.cached.NotificationTypes;
import com.entities.cached.PickupStatus;

/**
 * A Presenter to handle the Pickup Review Screen
 * <p/>
 * Created by Ahmed Adel on 9/27/2016.
 */
class PickupReviewPresenter extends
        Presenter<PickupReviewPresenter, PickupReviewViewModel, PickupModel> {


    PickupReviewPresenter(PickupReviewViewModel viewModel) {
        super(viewModel);
        setUpdater(new PickupReviewUpdater());
    }

    @Override
    protected CommandExecutor<Long, Message> createCommandExecutor() {
        CommandExecutor<Long, Message> commandExecutor = new CommandExecutor<>();
        Command<Message, Void> command = createOnPositivDialogClickedCommand();
        commandExecutor.put((long) R.id.onDialogPositiveClick, command);
        return commandExecutor;
    }

    private Command<Message, Void> createOnPositivDialogClickedCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                getViewModel().setRequesting(true);
                getViewModel().invalidateView(R.id.pickup_review_progress);
                getViewModel().invalidateView(R.id.pickup_review_submit_request_btn);
                return null;
            }
        };
    }

    @NonNull
    protected CommandExecutor<Long, View> createOnClickCommandExecutor() {
        CommandExecutor<Long, View> commandExecutor = new CommandExecutor<>();
        Command<View, Void> command = createOnSubmitBtnClickedCommand();
        commandExecutor.put((long) R.id.pickup_review_submit_request_btn, command);
        return commandExecutor;
    }

    private Command<View, Void> createOnSubmitBtnClickedCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                createAlertDialog();
                return null;
            }

            private void createAlertDialog() {
                EventDialogBuilder dialogBuilder = new EventDialogBuilder(R.id.onDialogSubmitPickupRequest);
                dialogBuilder.setTitle(R.string.submit_conf_msg);
                dialogBuilder.setMessage(R.string.submit_conf_msg2);
                dialogBuilder.setPositiveText(R.string.confirm);
                dialogBuilder.setNegativeText(R.string.cancel);
                EventDialog eventDialog = new EventDialog(dialogBuilder, getHostActivity());
                eventDialog.show();
            }
        };
    }

    @NonNull
    @Override
    protected CommandExecutor<Long, ResponseMessage> createResponseCommands() {
        CommandExecutor<Long, ResponseMessage> commandExecutor = new CommandExecutor<>();
        Command<ResponseMessage, Void> command = createOnSubmitRequestResponse();
        commandExecutor.put((long) R.id.requestSubmitPickupRequest, command);
        command = createOnCreatePickupRequestResponse();
        commandExecutor.put((long) R.id.requestCreatePickupRequest, command);
        commandExecutor.put((long) R.id.requestCreatePackage, command);
        return commandExecutor;
    }

    private Command<ResponseMessage, Void> createOnSubmitRequestResponse() {
        return new Command<ResponseMessage, Void>() {
            @Override
            public Void execute(ResponseMessage message) {
                getViewModel().setRequesting(false);
                getViewModel().invalidateView(R.id.pickup_review_progress);
                getViewModel().invalidateView(R.id.pickup_review_submit_request_btn);
                if (!message.isSuccessful()) {
                    showToast(R.string.pickup_sent_failed);
                }
                return null;
            }
        };
    }

    private Command<ResponseMessage, Void> createOnCreatePickupRequestResponse() {
        return new Command<ResponseMessage, Void>() {
            @Override
            public Void execute(ResponseMessage message) {
                getViewModel().setRequesting(false);
                getViewModel().invalidateView(R.id.pickup_review_progress);
                getViewModel().invalidateView(R.id.pickup_review_submit_request_btn);
                if (message.isSuccessful()) {
                    showSuccessToastAndStartTrackDriver();
                } else {
                    showToast(R.string.pickup_sent_failed);
                }
                return null;
            }

            private void showSuccessToastAndStartTrackDriver() {
                showToast(R.string.pickup_sent_successfully);
                finishCurrentActivity();
                if (getModel().getSchedule().isScheduled()) {
                    startPackagesActivity();
                } else {
                    startTrackRadarActivity();
                }
            }

            private void finishCurrentActivity() {
                ActivityActionRequest request = new ActivityActionRequest(ActionType.FINISH);
                EventBuilder eventBuilder = new EventBuilder(R.id.startActivityAction, request);
                getFeature().startActivityActionRequest(eventBuilder.execute(PickupReviewPresenter.this));
            }

            private void startTrackRadarActivity() {
                ActivityActionRequest request = new ActivityActionRequest(ActionType.START_ACTIVITY);
                request.action(TrackingActivity.class);
                Bundle extras = new Bundle();
                extras.putString(AppResources.string(R.string.INTENT_KEY_PICKUP_LOCATION_LATITUDE)
                        , getModel().getPickupLatitude());
                extras.putString(AppResources.getResources().getString(R.string.INTENT_KEY_PICKUP_LOCATION_LONGITUDE)
                        , getModel().getPickupLongitude());
                request.extras(extras);
                EventBuilder eventBuilder = new EventBuilder(R.id.startActivityAction, request);
                getFeature().startActivityActionRequest(eventBuilder.execute(PickupReviewPresenter.this));
            }

            private void startPackagesActivity() {
                ActivityActionRequest request = new ActivityActionRequest(ActionType.START_ACTIVITY);
                request.action(Features.PackageListActivity.class);
                Bundle extra = new Bundle();
                extra.putSerializable(AppResources.string(R.string.INTENT_KEY_PICKUP_STATUS)
                        , generatePickupStatus());
                request.extras(extra);
                EventBuilder eventBuilder = new EventBuilder(R.id.startActivityAction, request);
                getFeature().startActivityActionRequest(eventBuilder.execute(PickupReviewPresenter.this));
            }

            private PickupStatus generatePickupStatus() {
                PickupStatus pickupStatus = new PickupStatus();
                pickupStatus.setNickname(getModel().getPackage().getNickname());
                pickupStatus.setRecipientAddress(getModel().getRecipient().getFullAddress());
                pickupStatus.setStatus(NotificationTypes.NEW);
                pickupStatus.setRecipientName(getModel().getRecipient().getName());
                return pickupStatus;
            }
        };
    }

    private void showToast(int messageResourceId) {
        Toast.makeText(App.getInstance(), AppResources.string(messageResourceId)
                , Toast.LENGTH_SHORT).show();
    }

}
