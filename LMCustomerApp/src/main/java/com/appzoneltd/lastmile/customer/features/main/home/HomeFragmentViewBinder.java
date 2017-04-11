package com.appzoneltd.lastmile.customer.features.main.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.abstracts.LastMileViewBinder;
import com.appzoneltd.lastmile.customer.features.main.menu.HomeFloatingMenuPresenter;
import com.appzoneltd.lastmile.customer.subfeatures.menus.FloatingMenuViewModel;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.abstraction.events.Event;
import com.base.abstraction.events.Message;
import com.base.abstraction.system.AppResources;
import com.base.presentation.annotations.interfaces.BindLayout;
import com.base.presentation.base.abstracts.features.Feature;
import com.base.presentation.base.abstracts.features.ViewBinder;
import com.base.presentation.models.NullModel;
import com.base.presentation.receivers.GPSStateReceiver;
import com.base.presentation.receivers.NetworkStateReceiver;
import com.base.presentation.requests.ActivityResult;
import com.base.presentation.requests.PermissionResult;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.MapView;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;

/**
 * A {@link ViewBinder} for Home screen
 * <p/>
 * Created by Ahmed Adel on 9/5/2016.
 *
 * @deprecated old implementation
 */
@BindLayout(R.layout.activity_home)
public class HomeFragmentViewBinder extends LastMileViewBinder<NullModel> {

    @BindDrawable(R.drawable.header_logo)
    Drawable logoDrawable;
    @BindView(R.id.pickup_selector)
    View pickupSelector;
    @BindView(R.id.trace_selector)
    View traceSelector;
    @BindView(R.id.pickup_location_layout)
    FrameLayout pickupLocationLayout;
    @BindDrawable(R.drawable.detect_gps)
    Drawable detectGBS;
    @BindView(R.id.address_title)
    TextView addressTitle;
    @BindView(R.id.address_search_layout)
    LinearLayout addressSearchLayout;
    @BindString(R.string.wrong_Address)
    String wrongAddress;
    @BindView(R.id.home_floating_menu)
    LinearLayout homeFloatingMenu;
    @BindView(R.id.pb_address_text)
    ProgressBar addressProgressBar;
    @BindView(R.id.map_outer_layout)
    ViewGroup mapOuterLayout;
    @BindView(R.id.map_inner_layout)
    ViewGroup mapInnerLayout;
    @BindView(R.id.loading_map)
    ViewGroup loadingMap;
    @BindView(R.id.layout_outer_internet_error)
    ViewGroup outerInternetError;
    @BindView(R.id.home_map_view)
    MapView homeMapView;

    private final CommandExecutor<Long, View> onClickCommandsExecutor;
    private DialogBuilder dialogBuilder;


    public HomeFragmentViewBinder(Feature<NullModel> feature) {
        super(feature);
        onClickCommandsExecutor = createOnClickCommandExecutor();
        addSubClassCommands();
        addEventsSubscriber(new GPSStateReceiver(getFeature(), true));
        addEventsSubscriber(new NetworkStateReceiver(getFeature()));
    }

    private CommandExecutor<Long, View> createOnClickCommandExecutor() {
        CommandExecutor<Long, View> commandExecutor = new CommandExecutor<>();
        Command<View, Void> command = createRequestPickupClickCommand();
        commandExecutor.put((long) R.id.request_pickup, command);
        command = createTracePackageClickCommand();
        commandExecutor.put((long) R.id.trace_package, command);
        command = createOnRetryClickCommand();
        commandExecutor.put((long) R.id.network_error_retry, command);
        return commandExecutor;
    }

    private Command<View, Void> createOnRetryClickCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                getHostActivity().recreate();
                return null;
            }
        };
    }


    private void addSubClassCommands() {
        Command<Message, Void> command = createOnClickCommand();
        addCommand((long) R.id.onClick, command);
        command = createOnActivityResultCommand();
        addCommand((long) R.id.onActivityResult, command);
        command = createOnRequestPermissionCommand();
        addCommand((long) R.id.onRequestPermissionsResult, command);
        command = createOnOpenLocationSettingsCommand();
        addCommand((long) R.id.openLocationSettings, command);
        command = createOnResumeCommand();
        addCommand((long) R.id.onResume, command);
    }

    private Command<Message, Void> createOnOpenLocationSettingsCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message p) {
                if (getFeature().getManifestPermissions().isLocationPermissionAllowed()) {
                    showGPSDialog();
                } else {
                    ActivityCompat.requestPermissions(getHostActivity(),
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.ACCESS_FINE_LOCATION},
                            AppResources.integer(R.integer.requestCodeLocationPermission));
                }
                return null;
            }
        };
    }


    @NonNull
    private Command<Message, Void> createOnRequestPermissionCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message p) {
                PermissionResult result = p.getContent();
                int locationPermission = AppResources.integer(R.integer.requestCodeLocationPermission);
                if (result.requestCode == locationPermission) {
                    if (result.grantResults.length > 0
                            && result.grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        if (!getFeature().getSystemServices().isGPSProviderEnabled()) {
                            showGPSDialog();
                            return null;
                        }
                        notifyOnLocationPermissionEvent(true);
                    } else {
                        notifyOnLocationPermissionEvent(false);
                    }
                }
                return null;
            }
        };
    }


    private Command<Message, Void> createOnActivityResultCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                ActivityResult result = message.getContent();
                if (result.hasRequestCode(R.integer.requestCodeSearchAutoComplete)
                        && result.isResultCodeOk()) {
                    notifyOnMyLocationEvent(result.data);
                }
                return null;
            }
        };
    }

    private void notifyOnMyLocationEvent(Intent data) {
        Place place = PlaceAutocomplete.getPlace(getHostActivity(), data);
        Message message = new Message.Builder().content(place.getLatLng()).build();
        Event.Builder event = new Event.Builder((long) R.id.HomeBinder_onSearchPlaceSelected);
        event.message(message);
        notifyObservers(event.build());
    }

    private void notifyOnLocationPermissionEvent(boolean permissionStatus) {
        Message message = new Message.Builder().content(permissionStatus).build();
        Event.Builder event = new Event.Builder((long) R.id.HomeBinder_onLocationPermissionChanged);
        event.message(message);
        notifyObservers(event.build());
    }

    @NonNull
    private Command<Message, Void> createOnClickCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message p) {
                long id = p.getId();
                View v = p.getContent();
                onClickCommandsExecutor.execute(id, v);
                return null;
            }
        };
    }

    @NonNull
    private Command<View, Void> createRequestPickupClickCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View v) {
                traceSelector.setVisibility(View.GONE);
                pickupSelector.setVisibility(View.VISIBLE);
                return null;
            }

        };
    }

    @NonNull
    private Command<View, Void> createTracePackageClickCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View v) {
                pickupSelector.setVisibility(View.GONE);
                traceSelector.setVisibility(View.VISIBLE);
                return null;
            }
        };
    }

    @NonNull
    private Command<Message, Void> createOnResumeCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message p) {
                getHostActivity().invalidateOptionsMenu();
                return null;
            }
        };
    }

    @Override
    public void initializeAfterBindingViews(Bundle savedInstanceState) {
        setupMembersVariables();
        setupPresenters();
    }

    private void setupMembersVariables() {
        dialogBuilder = new DialogBuilder();
    }


    private void setupPresenters() {

        addEventsSubscriber(new MapPresenter(this, homeMapView, outerInternetError,
                mapInnerLayout, mapOuterLayout, loadingMap));

        addEventsSubscriber(new SearchBarPresenter(this, addressSearchLayout, addressTitle, addressProgressBar));

        FloatingMenuViewModel floatingMenuViewModel;
        floatingMenuViewModel = new FloatingMenuViewModel(this, homeFloatingMenu);
        addEventsSubscriber(new HomeFloatingMenuPresenter(this, floatingMenuViewModel));
    }


    private void showGPSDialog() {
        dialogBuilder.showGPSDialog(getHostActivity());
    }


    @Override
    public void onDestroy() {
        onClickCommandsExecutor.clear();
        super.onDestroy();
    }


}
