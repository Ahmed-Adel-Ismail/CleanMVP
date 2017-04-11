package com.appzoneltd.lastmile.customer.features.tracking;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.features.tracking.model.TrackingModel;
import com.appzoneltd.lastmile.customer.firebase.NotificationCounterChanger;
import com.appzoneltd.lastmile.customer.subfeatures.MapView.MapViewLifeCycle;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.abstraction.events.Event;
import com.base.abstraction.events.Message;
import com.base.abstraction.serializers.JsonSetLoader;
import com.base.abstraction.system.AppResources;
import com.base.presentation.base.presentation.Presenter;
import com.base.usecases.events.ResponseMessage;
import com.directions.route.Route;
import com.entities.Notification;
import com.entities.cached.PayloadActiveVehicleDetails;
import com.entities.cached.PickupStatus;
import com.google.android.gms.maps.model.LatLng;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;

/**
 * Created by Wafaa on 11/13/2016.
 */

class TrackingFragmentPresenter extends Presenter<
        TrackingFragmentPresenter, TrackingFragmentViewModel, TrackingModel> {

    private final CommandExecutor<Long, Message> onNotificationReceivedExecutor;
    private int imageIndex;
    private Timer timer;
    private MyTimerTask myTimerTask;

    TrackingFragmentPresenter(TrackingFragmentViewModel viewModel) {
        super(viewModel);
        onNotificationReceivedExecutor = new NotificationHandlerExecutor(this, getViewModel());
        Intent intent = getHostActivity().getIntent();
        retrieveLocationExtras(intent);
        retrievePickupStatusExtra(intent);
        retrieveNotificationExtras(intent);
        onUpdateViewModel();
        getViewModel().invalidateViews();
    }

    private void retrieveLocationExtras(Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String latitude = bundle.getString(AppResources.getResources()
                    .getString(R.string.INTENT_KEY_PICKUP_LOCATION_LATITUDE));
            String longitude = bundle.getString(AppResources.getResources()
                    .getString(R.string.INTENT_KEY_PICKUP_LOCATION_LONGITUDE));
            if (latitude != null && longitude != null) {
                getModel().pickupLocation = new LatLng(Double.parseDouble(latitude)
                        , Double.parseDouble(longitude));
            }
        }
    }

    private void retrievePickupStatusExtra(Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            PickupStatus status = (PickupStatus)
                    bundle.getSerializable(AppResources.string(R.string.INTENT_KEY_PICKUP_STATUS));
            if (status != null) {
                getModel().pickupLocation = new LatLng(Double.parseDouble(status.getLatitude())
                        , Double.parseDouble(status.getLongitude()));
            }
        }
    }

    private void retrieveNotificationExtras(Intent intent) {
        if (intent == null) {
            return;
        }
        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            return;
        }
        PayloadActiveVehicleDetails payloadActiveVehicleDetails =
                (PayloadActiveVehicleDetails) bundle.getSerializable(
                        AppResources.string(R.string.INTENT_KEY_ACTIVE_VEHICLE_DETAILS));
        if (payloadActiveVehicleDetails != null) {
            getModel().payloadActiveVehicleDetails = payloadActiveVehicleDetails;
            getModel().pickupLocation = new LatLng(payloadActiveVehicleDetails.getPickupLatitude()
                    , payloadActiveVehicleDetails.getPickupLongitude());
        }
    }

    @Override
    protected CommandExecutor<Long, Message> createCommandExecutor() {
        CommandExecutor<Long, Message> commandExecutor = new CommandExecutor<>();
        Command<Message, Void> command = createOnPauseCommand();
        commandExecutor.put((long) R.id.onPause, command);
        command = createOnResumeCommand();
        commandExecutor.put((long) R.id.onResume, command);
        command = createOnStartCommand();
        commandExecutor.put((long) R.id.onStart, command);
        command = createOnStopCommand();
        commandExecutor.put((long) R.id.onStop, command);
        command = createOnNotificationReceivedCommand();
        commandExecutor.put((long) R.id.onHandleNotification, command);
        command = createOnRoutingCommand();
        commandExecutor.put((long) R.id.onRouting, command);
        return commandExecutor;
    }

    private Command<Message, Void> createOnResumeCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message p) {
                if (getModel().payloadActiveVehicleDetails == null) {
                    startTimerTask();
                } else {
                    invalidateMapProgress(true);
                    Event event = new Event.Builder(R.id.requestSearchTopic).build();
                    getModel().execute(event);
                }
                getViewModel().setMapCycleStatus(MapViewLifeCycle.ONRESUME.status);
                getViewModel().invalidateViews();
                return null;

            }
        };
    }

    private Command<Message, Void> createOnPauseCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message p) {
                stopTimer();
                getViewModel().setMapCycleStatus(MapViewLifeCycle.ONPAUSE.status);
                getViewModel().invalidateView(R.id.driver_map_view);
                getModel().requestCloseWebSocket();
                return null;
            }

        };
    }

    private Command<Message, Void> createOnStartCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message p) {
                getViewModel().setMapCycleStatus(MapViewLifeCycle.ONSTART.status);
                getViewModel().invalidateView(R.id.driver_map_view);
                return null;
            }
        };
    }

    private Command<Message, Void> createOnStopCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message p) {
                getViewModel().setMapCycleStatus(MapViewLifeCycle.ONSTOP.status);
                getViewModel().invalidateView(R.id.driver_map_view);
                return null;
            }
        };
    }

    private void startTimerTask() {
        imageIndex = -1;
        timer = new Timer();
        myTimerTask = new MyTimerTask();
        timer.schedule(myTimerTask, 300, 300);
    }

    private class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            getHostActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (imageIndex >= getViewModel().getRadarImagesLength() - 1) {
                        imageIndex = 0;
                    } else {
                        ++imageIndex;
                    }
                    getViewModel().drawRadar(imageIndex);
                }
            });
        }
    }

    private Command<Message, Void> createOnNotificationReceivedCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                stopTimer();
                getViewModel().clearMap();
                new NotificationCounterChanger().execute(null);
                onNotificationReceivedExecutor.execute(message.getId(), message);
                return null;
            }
        };
    }

    private Command<Message, Void> createOnRoutingCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                hideMapProgress();
                List<Route> routes = message.getContent();
                getViewModel().routes = routes;
                getViewModel().drawRoute(getModel().destinationLatLng);
                return null;
            }

        };
    }

    Notification loadNotificationFromPref() {
        TreeSet<Notification> notifications;
        JsonSetLoader<Notification> loader =
                new JsonSetLoader<>(R.string.PREFS_KEY_NOTIFICATION_LIST);
        notifications = loader.execute(Notification.class);
        LinkedList<Notification> linkedList = new LinkedList<>(notifications);
        if (linkedList.size() > 0) {
            return linkedList.get(linkedList.size() - 1);
        }
        return null;
    }

    private void hideMapProgress() {
        invalidateMapProgress(false);
        getViewModel().invalidateView(R.id.fragment_tracking_loading);
    }

    @NonNull
    @Override
    protected CommandExecutor<Long, ResponseMessage> createResponseCommands() {
        return new ResponsesHandlerExecutor(this, getViewModel());
    }

    private void stopTimer() {
        if (timer != null)
            timer.cancel();
        if (myTimerTask != null)
            myTimerTask.cancel();
    }

    void invalidateMapProgress(boolean show) {
        getViewModel().showProgress = show;
        getViewModel().invalidateView(R.id.fragment_tracking_loading);
    }

    @Override
    public void onDestroy() {
        stopTimer();
        timer = null;
        myTimerTask = null;
        getViewModel().setMapCycleStatus(MapViewLifeCycle.ONDESTROY.status);
        getViewModel().invalidateView(R.id.driver_map_view);
        super.onDestroy();
    }
}
