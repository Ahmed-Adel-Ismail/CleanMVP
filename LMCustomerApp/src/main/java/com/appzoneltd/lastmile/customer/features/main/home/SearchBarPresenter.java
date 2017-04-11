package com.appzoneltd.lastmile.customer.features.main.home;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.deprecated.ConnectionErrorActivity;
import com.appzoneltd.lastmile.customer.deprecated.utilities.UIManager;
import com.appzoneltd.lastmile.customer.deprecatred.SharedManager;
import com.appzoneltd.lastmile.customer.features.main.models.PickupLocationModel;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.abstraction.events.Event;
import com.base.abstraction.events.Message;
import com.base.abstraction.system.AppResources;
import com.base.presentation.base.abstracts.features.ViewBinder;
import com.base.presentation.base.presentation.Presenter;
import com.base.presentation.receivers.NetworkStateReceiver;
import com.base.presentation.views.validators.StringValidator;
import com.base.presentation.views.validators.ValidStringGenerator;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;

/**
 * A {@link Presenter} that is responsible for SearchBar
 * Created by Wafaa on 9/7/2016.
 */
class SearchBarPresenter extends Presenter {

    private TextView addressTitle;
    private ProgressBar progressBar;
    private LinearLayout addressSearchLayout;
    private boolean locationPermissionEnabled;
    private boolean networkEnabled;
    private boolean gpsEnabled;
    private PickupLocationModel locationModel;
    private AsyncTask addressGenerator;
    private LatLng latLng;

    SearchBarPresenter(ViewBinder viewBinder, LinearLayout addressSearchLayout,
                       TextView address, ProgressBar progressBar) {
        super(viewBinder);
        this.addressTitle = address;
        this.progressBar = progressBar;
        this.addressSearchLayout = addressSearchLayout;
        this.locationPermissionEnabled = getManifestPermissions().isLocationPermissionAllowed();
        this.networkEnabled = getSystemServices().isNetworkConnected();
        this.gpsEnabled = getSystemServices().isGPSProviderEnabled();
        locationModel = new PickupLocationModel();
    }

    @NonNull
    protected CommandExecutor<Long, View> createOnClickCommandExecutor() {
        CommandExecutor<Long, View> commandExecutor = new CommandExecutor<>();
        Command<View, Void> command = createSearchBarOnClickCommand();
        commandExecutor.put((long) R.id.address_title, command);
        commandExecutor.put((long) R.id.search_location_image, command);
        return commandExecutor;
    }

    private Command<View, Void> createSearchBarOnClickCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                getHostActivity().lockInteractions();
                openAutocompleteActivity();
                return null;
            }
        };
    }


    private void notifyValidAddressEvent(PickupLocationModel locationModel) {
        Message message = new Message.Builder().content(locationModel).build();
        Event.Builder event = new Event.Builder((long) R.id.SearchBar_onAddressValid);
        event.message(message);
        notifyObservers(event.build());
    }

    private void openAutocompleteActivity() {
        if (!UIManager.isConnectedToInternet(getHostActivity())) {
            showErrorDialog();
            return;
        }
        int reqCode = AppResources.integer(R.integer.requestCodeSearchAutoComplete);
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(getHostActivity());
            getHostActivity().startActivityForResult(intent, reqCode);
        } catch (GooglePlayServicesRepairableException e) {
            GoogleApiAvailability.getInstance().getErrorDialog(getHostActivity(),
                    e.getConnectionStatusCode(), reqCode).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);
            Toast.makeText(getHostActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }

    private void showErrorDialog() {
        Intent intent = new Intent(getHostActivity(), ConnectionErrorActivity.class);
        intent.putExtra(SharedManager.CONNECTION_ERROR_TITLE, SharedManager.HOME_TITLE);
        getHostActivity().startActivityForResult(intent, SharedManager.CONNECTION_REQUEST_CODE);
    }

    @Override
    protected CommandExecutor<Long, Message> createCommandExecutor() {
        CommandExecutor<Long, Message> commandExecutor = new CommandExecutor<>();
        Command<Message, Void> command = createOnMyLocationCommand();
        commandExecutor.put((long) R.id.Map_onLocationSelected, command);
        command = createOnLocationPermissionChanged();
        commandExecutor.put((long) R.id.HomeBinder_onLocationPermissionChanged, command);
        command = createOnGPSStateChanged();
        commandExecutor.put((long) R.id.onGPSStateChanged, command);
        command = createOnNetworkStateChangedCommand();
        commandExecutor.put((long) R.id.onNetworkStateChanged, command);
        command = createOnActivityResultCommand();
        commandExecutor.put((long) R.id.onActivityResult, command);
        command = createOnPauseCommand();
        commandExecutor.put((long) R.id.onPause, command);
        command = createOnResumeCommand();
        commandExecutor.put((long) R.id.onResume, command);
        return commandExecutor;
    }

    private Command<Message, Void> createOnLocationPermissionChanged() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message p) {
                locationPermissionEnabled = p.getContent();
                if (locationPermissionEnabled) {
                    addressSearchLayout.setVisibility(View.VISIBLE);
                } else {
                    addressSearchLayout.setVisibility(View.GONE);
                }
                return null;
            }
        };
    }

    private Command<Message, Void> createOnGPSStateChanged() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                gpsEnabled = message.getContent();
                if (networkEnabled) {
                    if (gpsEnabled && getManifestPermissions().isLocationPermissionAllowed()) {
                        addressSearchLayout.setVisibility(View.VISIBLE);
                    } else {
                        addressSearchLayout.setVisibility(View.GONE);
                    }
                } else {
                    addressSearchLayout.setVisibility(View.GONE);
                }
                return null;
            }
        };
    }

    private Command<Message, Void> createOnMyLocationCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                latLng = message.getContent();
                if (latLng != null) {
                    generateAndUpdateAddressTextView(latLng);
                } else {
                    showFailureToast();
                }
                return null;
            }


            private void showFailureToast() {
                Toast.makeText(getHostActivity(),
                        AppResources.string(R.string.detect_current_location_failed)
                        , Toast.LENGTH_SHORT).show();
            }

        };
    }

    private Command<Message, Void> createOnPauseCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message p) {
                if (addressGenerator != null) {
                    addressGenerator.cancel(true);
                }
                return null;
            }
        };
    }

    private void generateAndUpdateAddressTextView(LatLng latLng) {
        locationModel.setGoogleAddress(latLng);
        if (addressGenerator != null) {
            addressGenerator.cancel(true);
        }
        addressGenerator = new AddressGenerator(getHostActivity(), latLng, progressBar) {
            @Override
            public String onAddressGenerated(Addresses address) {
                updateAddressTextViewAndNotify(address);
                return null;
            }
        }.execute();

    }


    private Command<Message, Void> createOnResumeCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                if (latLng != null
                        && getSystemServices().isNetworkConnected()) {
                    generateAndUpdateAddressTextView(latLng);
                }
                return null;
            }
        };
    }

    private void updateAddressTextViewAndNotify(Addresses address) {
        addressTitle.setText(address.getDisplayedAddress());
        addressTitle.setHint(address.getDisplayedAddress());
        String addressValue = new ValidStringGenerator().execute(address.getDisplayedAddress());
        boolean emptyText = new StringValidator().execute(addressValue);
        if (!emptyText) {
            locationModel.setDisplayedAddress(addressValue);
            locationModel.setValidAddress(true);
            locationModel.setFormattedAddress(address.getFormattedAddress());
            notifyValidAddressEvent(locationModel);
        } else {
            locationModel.setValidAddress(false);
            notifyValidAddressEvent(locationModel);
            Toast.makeText(getHostActivity(), R.string.choose_valid_location_msg, Toast.LENGTH_SHORT).show();
        }
    }

    private Command<Message, Void> createOnNetworkStateChangedCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message p) {
                NetworkStateReceiver.ConnectedNetworks connectedNetworks = p.getContent();
                networkEnabled = connectedNetworks.any;
                if (networkEnabled) {
                    if (getSystemServices().isGPSProviderEnabled()
                            && getManifestPermissions().isLocationPermissionAllowed()) {
                        addressSearchLayout.setVisibility(View.VISIBLE);
                    } else {
                        addressSearchLayout.setVisibility(View.GONE);
                    }
                } else {
                    addressSearchLayout.setVisibility(View.GONE);
                }
                return null;
            }
        };
    }

    private Command<Message, Void> createOnActivityResultCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message p) {
                getHostActivity().unlockInteractions();
                return null;
            }
        };
    }


    @Override
    public void onDestroy() {
        addressTitle = null;
        if (addressGenerator != null) {
            addressGenerator.cancel(true);
            addressGenerator = null;
        }
    }
}
