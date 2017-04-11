package com.appzoneltd.lastmile.customer.features.main.menu;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.features.main.models.PickupLocationModel;
import com.appzoneltd.lastmile.customer.features.pickup.host.PickupActivity;
import com.appzoneltd.lastmile.customer.features.tracking.host.TrackingActivity;
import com.appzoneltd.lastmile.customer.subfeatures.menus.FloatingMenuParams;
import com.appzoneltd.lastmile.customer.subfeatures.menus.FloatingMenuParamsGroup;
import com.appzoneltd.lastmile.customer.subfeatures.menus.FloatingMenuPresenter;
import com.appzoneltd.lastmile.customer.subfeatures.menus.FloatingMenuViewModel;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.abstraction.events.Event;
import com.base.abstraction.events.EventBuilder;
import com.base.abstraction.events.Message;
import com.base.presentation.base.abstracts.features.AbstractActivity;
import com.base.presentation.base.abstracts.features.ViewBinder;
import com.base.presentation.receivers.NetworkStateReceiver;
import com.base.presentation.requests.ActionType;
import com.base.presentation.requests.ActivityActionRequest;
import com.base.presentation.requests.ActivityResult;

import java.io.Serializable;

/**
 * A {@link HomeFloatingMenuPresenter} that handles the Home screen floating menu
 * <p/>
 * Created by Ahmed Adel on 9/14/2016.
 */
public class HomeFloatingMenuPresenter extends FloatingMenuPresenter {

    private Serializable pickupData;
    private PickupLocationModel locationModel;
    private boolean networkEnabled;
    private static final int NO_TEXT_VALUE = -1;


    public HomeFloatingMenuPresenter(ViewBinder viewBinder,
                                     @NonNull FloatingMenuViewModel floatingMenuViewModel) {
        super(viewBinder, floatingMenuViewModel);
        this.networkEnabled = getSystemServices().isNetworkConnected();
    }

    @NonNull
    protected CommandExecutor<Long, View> createOnClickCommandExecutor() {
        CommandExecutor<Long, View> commandExecutor = new CommandExecutor<>();
        Command<View, Void> command = createPickupFloatingButtonOnClickCommand();
        commandExecutor.put((long) R.id.floating_btn_pickup_menu, command);
        command = createOutsideFloatingMenuOnClickCommand();
        commandExecutor.put((long) R.id.floating_menu_parent, command);
        command = createEnableLocationOnClickCommand();
        commandExecutor.put((long) R.id.floating_btn_detect_my_location, command);
        return commandExecutor;
    }

    private Command<View, Void> createEnableLocationOnClickCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View p) {
                Event event = new Event.Builder(R.id.openLocationSettings).build();
                notifyObservers(event);
                return null;
            }

        };
    }

    @NonNull
    private Command<View, Void> createPickupFloatingButtonOnClickCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View v) {
                AbstractActivity activity = getHostActivity();
                if (activity != null && locationModel != null) {
                    if (locationModel.isValidAddress()) {
                        startPickupMenu();
                    }
                } else {
                    showConfirmationToast();
                }
//                startNotificationActivity();
                return null;
            }

            private void startPickupMenu() {
                getFloatingMenuDrawer().hide();
                ActivityActionRequest request = new ActivityActionRequest(ActionType.START_ACTIVITY);
                request.action(PickupFloatingMenuActivity.class);
                request.extra(R.string.INTENT_KEY_PICKUP_LOCATION_MODEL, locationModel);
                request.code(R.integer.requestCodePickupRequestMenu);
                EventBuilder eventBuilder = new EventBuilder(R.id.startActivityAction, request);
                getFeature().startActivityActionRequest(eventBuilder.execute(getHostActivity()));
            }
        };
    }

    private void showConfirmationToast() {
        Toast.makeText(getHostActivity(), R.string.choose_location_msg,
                Toast.LENGTH_SHORT).show();
    }

    private Command<View, Void> createOutsideFloatingMenuOnClickCommand() {
        return new Command<View, Void>() {

            @Override
            public Void execute(View p) {
                Activity activity = getHostActivity();
                if (activity != null) {
                    finishHostActivity(activity);
                }
                return null;
            }

            private void finishHostActivity(Activity activity) {
                activity.finish();
            }
        };
    }

    @Override
    protected CommandExecutor<Long, Message> createCommandExecutor() {
        CommandExecutor<Long, Message> commandExecutor = new CommandExecutor<>();
        Command<Message, Void> command = createOnResumeCommand();
        commandExecutor.put((long) R.id.onResume, command);
        commandExecutor.put((long) R.id.Map_onGPSEnabled, command);
        command = createOnPickupRequestClicked();
        commandExecutor.put((long) R.id.SearchBar_onAddressValid, command);
        command = createOnGpsStatChanged();
        commandExecutor.put((long) R.id.onGPSStateChanged, command);
        command = createOnLocationPermissionChanged();
        commandExecutor.put((long) R.id.HomeBinder_onLocationPermissionChanged, command);
        command = createOnNetworkStateChangedCommand();
        commandExecutor.put((long) R.id.onNetworkStateChanged, command);
        command = createOnActivityResultCommand();
        commandExecutor.put((long) R.id.onActivityResult, command);
        return commandExecutor;
    }


    private Command<Message, Void> createOnResumeCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message p) {
                drawAndShowNormalFloatingMenu(createInitialFloatingMenuParamsGroup());
                return null;
            }
        };
    }

    private Command<Message, Void> createOnPickupRequestClicked() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message p) {
                locationModel = p.getContent();
                return null;
            }
        };
    }

    private Command<Message, Void> createOnGpsStatChanged() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                drawAndShowNormalFloatingMenu(createInitialFloatingMenuParamsGroup());
                return null;
            }
        };
    }

    private Command<Message, Void> createOnNetworkStateChangedCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message p) {
                NetworkStateReceiver.ConnectedNetworks connectedNetworks = p.getContent();
                networkEnabled = connectedNetworks.any;
                if (networkEnabled) {
                    drawAndShowNormalFloatingMenu(createInitialFloatingMenuParamsGroup());
                } else {
                    getFloatingMenuDrawer().hide();
                }
                return null;
            }


        };
    }

    private void drawAndShowNormalFloatingMenu(FloatingMenuParamsGroup floatingMenuParamsGroup) {
        if (floatingMenuParamsGroup != null) {
            getFloatingMenuDrawer().draw(floatingMenuParamsGroup);
            getFloatingMenuDrawer().show();
        }
    }

    @Override
    protected FloatingMenuParamsGroup createInitialFloatingMenuParamsGroup() {
        return createFloatingMenuForLocationState();
    }

    private FloatingMenuParamsGroup createFloatingMenuForLocationState() {
        if (networkEnabled) {
            if (getSystemServices().isGPSProviderEnabled()
                    && getManifestPermissions().isLocationPermissionAllowed()) {
                return createNormalFloatingMenu();
            } else {
                return createLocationSettingsOrPermissionFloatingMenu();
            }
        } else {
            getFloatingMenuDrawer().hide();
        }
        return null;
    }

    private Command<Message, Void> createOnLocationPermissionChanged() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message p) {
                boolean permissionStatus = p.getContent();
                if (permissionStatus) {
                    drawAndShowNormalFloatingMenu(createNormalFloatingMenu());
                } else {
                    drawAndShowNormalFloatingMenu(createLocationSettingsOrPermissionFloatingMenu());
                }
                return null;
            }
        };
    }


    @NonNull
    private FloatingMenuParamsGroup createNormalFloatingMenu() {
        FloatingMenuParamsGroup floatingMenuParamsGroup = new FloatingMenuParamsGroup();

        FloatingMenuParams.Builder b = new FloatingMenuParams.Builder(R.id.floating_btn_locate_me);
        b.imageResourceId(R.drawable.detect);
        b.textResourceId(NO_TEXT_VALUE);
        floatingMenuParamsGroup.add(b.build());

        b = new FloatingMenuParams.Builder(R.id.floating_btn_pickup_menu);
        b.imageResourceId(R.drawable.truck);
        b.textResourceId(NO_TEXT_VALUE);
        floatingMenuParamsGroup.add(b.build());

        return floatingMenuParamsGroup;
    }

    @NonNull
    private FloatingMenuParamsGroup createLocationSettingsOrPermissionFloatingMenu() {
        FloatingMenuParamsGroup floatingMenuParamsGroup = new FloatingMenuParamsGroup();

        FloatingMenuParams.Builder b;
        b = new FloatingMenuParams.Builder(R.id.floating_btn_detect_my_location);
        b.imageResourceId(R.drawable.no_gps);
        b.textResourceId(NO_TEXT_VALUE);
        floatingMenuParamsGroup.add(b.build());
        return floatingMenuParamsGroup;
    }


    private Command<Message, Void> createOnActivityResultCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                ActivityResult result = message.getContent();
                if (result.hasRequestCode(R.integer.requestCodePickupRequestMenu)) {
                    if (result.isResultCodeOk()) {
                        startPickupRequestScreen(result);
                    }
                } else if (result.hasRequestCode(R.integer.requestCodePickupRequestScreen)) {
                    if (result.isResultCodeCancel()) {
                        pickupData = result.getExtra(R.string.INTENT_KEY_PICKUP_DATA);
                    }
                }
                return null;
            }

            private void startPickupRequestScreen(ActivityResult result) {
                ActivityActionRequest request = new ActivityActionRequest(ActionType.START_ACTIVITY);
                request.action(PickupActivity.class);
                request.extras(result.data.getExtras());
                if (pickupData != null) {
                    request.extra(R.string.INTENT_KEY_PICKUP_DATA, pickupData);
                    pickupData = null;
                }
                request.code(R.integer.requestCodePickupRequestScreen);
                EventBuilder eventBuilder = new EventBuilder(R.id.startActivityAction, request);
                getFeature().startActivityActionRequest(eventBuilder.execute(getHostActivity()));
            }
        };
    }

    @Override
    public void onDestroy() {
        locationModel = null;
    }

}
