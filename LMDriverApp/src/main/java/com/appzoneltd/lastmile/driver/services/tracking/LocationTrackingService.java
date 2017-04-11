package com.appzoneltd.lastmile.driver.services.tracking;

import android.location.Location;
import android.support.annotation.NonNull;

import com.appzoneltd.lastmile.driver.R;
import com.base.abstraction.annotations.interfaces.Address;
import com.base.abstraction.annotations.interfaces.Executable;
import com.base.abstraction.commands.RxCommand;
import com.base.abstraction.events.Event;
import com.base.abstraction.events.Message;
import com.base.abstraction.exceptions.CheckedReferenceClearedException;
import com.base.abstraction.exceptions.TestException;
import com.base.abstraction.logs.Logger;
import com.base.abstraction.system.App;
import com.base.presentation.annotations.interfaces.Foreground;
import com.base.presentation.base.services.AbstractService;
import com.base.presentation.exceptions.locations.FineLocationDisabledException;
import com.base.presentation.exceptions.locations.LocationPermissionRequiredException;
import com.base.presentation.locations.LocationConnection;
import com.base.presentation.locations.LocationConnection.Callbacks;
import com.base.presentation.locations.LocationTracking;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * a service that is responsible for tracking the driver location and updating the server
 * <p>
 * Created by Ahmed Adel on 1/17/2017.
 */
@Foreground
@Address(R.id.addressLocationTrackingService)
public class LocationTrackingService extends AbstractService<LocationTrackingModel> {

    private static final long UPDATES_INTERVAL_MILLIS = 100;
    private static final long WAIT_FOR_GPS_ENABLED_MILLIS = 5 * 1000;

    private LocationConnection locationConnection;
    private Disposable socketConnection;

    @Executable(R.id.onSessionExpired)
    void onSessionExpired(Message message) {
        clear();
    }

    @Executable(R.id.onServiceStartedAgain)
    void onStartAgain(Message message) {
        Logger.getInstance().error(getClass(), "ignored starting again @ " + message.getId());
    }

    @Executable(R.id.onServiceStarted)
    void onStart(Message message) {
        try {
            locationConnection = createLocationConnection();
            socketConnection = createSocketConnection();
        } catch (LocationPermissionRequiredException e) {
            clear();
        } catch (FineLocationDisabledException e) {
            handleFineLocationDisabled(message);
        } catch (CheckedReferenceClearedException e) {
            handleCheckedReferenceException(e);
        } catch (Throwable e) {
            handleThrowable(e);
        }
    }

    // location connection
    private LocationConnection createLocationConnection() {
        return new LocationTracking().execute(this)
                .execute(new Callbacks(onLocationChanged(), onLocationException()));
    }

    private RxCommand<Location> onLocationChanged() {
        return new RxCommand<Location>() {
            @Override
            public Void execute(Location location) {

                getModel().currentLocation.set(location);

                Message message = new Message.Builder().content(location).build();

                Event event = new Event.Builder(R.id.onLocationTrackingServiceUpdateLocation)
                        .message(message)
                        .senderActorAddress(getActorAddress())
                        .receiverActorAddresses(R.id.addressHomeFragment)
                        .build();


                App.getInstance().getActorSystem().send(event);

                return null;
            }
        };
    }

    private RxCommand<Throwable> onLocationException() {
        return new RxCommand<Throwable>() {
            @Override
            public Void execute(Throwable e) {
                getModel().currentLocation.set(null);
                Logger.getInstance().exception(e);
                return null;
            }
        };
    }

    // location sender
    @NonNull
    private Disposable createSocketConnection() {
        return Observable.interval(0, UPDATES_INTERVAL_MILLIS, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.trampoline())
                .observeOn(Schedulers.trampoline())
                .subscribe(sendLocation());
    }

    @NonNull
    private RxCommand<Long> sendLocation() {
        return new RxCommand<Long>() {
            @Override
            public Void execute(Long time) {
                if (!getModel().socketConnection.requestIfRequired()
                        && getSystemServices().isGPSProviderEnabled()) {
                    getModel().trackingDetailsUpdate.request();
                }
                return null;
            }
        };
    }

    // fine location required
    private void handleFineLocationDisabled(Message message) {
        String messagePrefix = "GPS disabled ... will retry in ";
        Logger.getInstance().error(getClass(), messagePrefix + WAIT_FOR_GPS_ENABLED_MILLIS / 1000 + " seconds");
        try {
            Thread.sleep(WAIT_FOR_GPS_ENABLED_MILLIS);
            onStart(message);
        } catch (InterruptedException e) {
            Logger.getInstance().error(getClass(), "handleFineLocationDisabled() : " + e);
        }
    }

    // checker reference cleared
    private void handleCheckedReferenceException(CheckedReferenceClearedException e) {
        Logger.getInstance().error(getClass(), "not supposed to reach this point");
        new TestException().execute(e);
    }

    // onStart Throwable
    private void handleThrowable(Throwable e) {
        Logger.getInstance().exception(e);
        clear();
    }


    @Override
    public void onDestroy() {
        clearLocationSender();
        clearLocationConnection();
        super.onDestroy();
    }

    private void clearLocationSender() {
        if (socketConnection != null) {
            if (!socketConnection.isDisposed()) {
                socketConnection.dispose();
            }
            socketConnection = null;
        }
    }

    private void clearLocationConnection() {
        if (locationConnection != null) {
            locationConnection.clear();
            locationConnection = null;
        }
    }
}
