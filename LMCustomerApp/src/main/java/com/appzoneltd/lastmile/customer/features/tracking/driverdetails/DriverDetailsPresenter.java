package com.appzoneltd.lastmile.customer.features.tracking.driverdetails;

import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.view.View;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.features.tracking.model.TrackingModel;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.abstraction.events.Event;
import com.base.abstraction.events.EventBuilder;
import com.base.abstraction.events.Message;
import com.base.presentation.base.presentation.Presenter;
import com.base.presentation.requests.ActionType;
import com.base.presentation.requests.ActivityActionRequest;
import com.base.usecases.events.ResponseMessage;
import com.directions.route.Route;
import com.entities.cached.ServerImage;

import java.util.List;

/**
 * Created by Wafaa on 11/27/2016.
 */

public class DriverDetailsPresenter extends Presenter<
        DriverDetailsPresenter, DriverDetailsViewModel, TrackingModel> {

    private CountDownTimer countDownTimer;

    public DriverDetailsPresenter(DriverDetailsViewModel viewModel) {
        super(viewModel);
        if (getModel().payloadActiveVehicleDetails != null) {
            onUpdateViewModel();
            requestFindFile();
            getViewModel().invalidateViews();
        }
    }

    private void requestFindFile() {
        getModel().execute(new Event.Builder(R.id.requestFindImage).build());
    }

    @Override
    protected CommandExecutor<Long, Message> createCommandExecutor() {
        CommandExecutor<Long, Message> commandExecutor = new CommandExecutor<>();
        Command<Message, Void> command;
        command = createOnCountTimeCommand();
        commandExecutor.put((long) R.id.onResume, command);
        command = createOnPauseCommand();
        commandExecutor.put((long) R.id.onPause, command);
        command = createOnNotificationReceivedCommand();
        commandExecutor.put((long) R.id.onHandleNotification, command);
        command = createOnRoutingCommand();
        commandExecutor.put((long) R.id.onRouting, command);
        return commandExecutor;
    }

    private Command<Message, Void> createOnCountTimeCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message msg) {
                if (getModel().payloadActiveVehicleDetails != null) {
                    getViewModel().invalidateViews();
                }
                return null;
            }

        };
    }

    private Command<Message, Void> createOnPauseCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                if (countDownTimer != null)
                    countDownTimer.cancel();
                return null;
            }
        };
    }

    private Command<Message, Void> createOnNotificationReceivedCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                if (getModel().payloadActiveVehicleDetails != null) {
                    if (message.getId() == R.id.onNotifiedDriverOnHisWay) {
                        onUpdateViewModel();
                        getViewModel().invalidateViews();
                        Event event = new Event.Builder(R.id.requestFindImage).build();
                        getModel().execute(event);
                    }
                }
                return null;
            }
        };
    }

    private Command<Message, Void> createOnRoutingCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                List<Route> routes = message.getContent();
                if (getViewModel().max == 1) {
                    getViewModel().max = routes.get(0).getDurationValue();
                }
                getViewModel().progress = (routes.get(0).getDurationValue()) / 60;
                getViewModel().estimationTimeText = routes.get(0).getDurationText();
                getViewModel().invalidateView(R.id.time_wheel);
                return null;
            }


        };
    }

    @NonNull
    @Override
    protected CommandExecutor<Long, View> createOnClickCommandExecutor() {
        CommandExecutor<Long, View> commandExecutor = new CommandExecutor<>();
        Command<View, Void> command = createOnCallClickedCommand();
        commandExecutor.put((long) R.id.call_driver, command);
        return commandExecutor;
    }

    private Command<View, Void> createOnCallClickedCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                if (!getManifestPermissions().isCallPermissionAllowed()) {
                    getManifestPermissions().openCallPermissionDialog(
                            R.integer.requestCodeCallPermission);
                } else {
                    ActivityActionRequest request = new ActivityActionRequest(ActionType.START_ACTIVITY);
                    request.action(Intent.ACTION_CALL);
                    request.addData(Uri.parse("tel:" + getModel().payloadActiveVehicleDetails.getDriverPhoneNumber()));
                    EventBuilder eventBuilder = new EventBuilder(R.id.startActivityAction, request);
                    getFeature().startActivityActionRequest(eventBuilder.execute(DriverDetailsPresenter.this));
                }
                return null;
            }
        };
    }

    @Override
    protected CommandExecutor<Long, ResponseMessage> createResponseCommands() {
        CommandExecutor<Long, ResponseMessage> commandExecutor = new CommandExecutor<>();
        Command<ResponseMessage, Void> command = createOnFindImageResponse();
        commandExecutor.put((long) R.id.requestFindImage, command);
        return commandExecutor;
    }

    private Command<ResponseMessage, Void> createOnFindImageResponse() {
        return new Command<ResponseMessage, Void>() {
            @Override
            public Void execute(ResponseMessage message) {
                ServerImage serverImage = message.getContent();
                if (serverImage != null) {
                    getViewModel().uri = serverImage.getUri();
                    getViewModel().invalidateView(R.id.driver_details_image);
                }
                return null;
            }
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }
}
