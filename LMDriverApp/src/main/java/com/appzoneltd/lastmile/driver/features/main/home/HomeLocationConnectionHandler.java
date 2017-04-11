package com.appzoneltd.lastmile.driver.features.main.home;

import android.location.Location;
import android.support.annotation.NonNull;

import com.appzoneltd.lastmile.driver.R;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.RxCommand;
import com.base.abstraction.logs.Logger;
import com.base.abstraction.references.CheckedReference;
import com.base.abstraction.system.AppResources;
import com.base.presentation.locations.LocationConnection;
import com.base.presentation.locations.LocationTracking;
import com.base.presentation.locations.LocationTrackingExceptionHandler;

/**
 * a class to handle the {@link LocationConnection} for the {@link HomePresenter}
 * <p>
 * Created by Ahmed Adel on 12/28/2016.
 */
class HomeLocationConnectionHandler implements Command<HomePresenter, Void> {


    @Override
    public Void execute(HomePresenter presenter) {
        showProgressBarAndStartNewLocationConnection(presenter);
        return null;
    }

    @SuppressWarnings("MissingPermission")
    private void showProgressBarAndStartNewLocationConnection(HomePresenter p) {
        try {
            LocationConnection.Callbacks callbacks = createCallbacks(p);
            new LocationTracking().execute(p.getHostActivity()).execute(callbacks);
        } catch (Throwable e) {
            new LocationTrackingExceptionHandler(p).execute(e);
        }
    }

    @NonNull
    private LocationConnection.Callbacks createCallbacks(HomePresenter p) {
        CheckedReference<HomePresenter> ref = new CheckedReference<>(p);
        return new LocationConnection.Callbacks(onLocationChanged(ref), onError(ref));
    }

    private static RxCommand<Location> onLocationChanged(final CheckedReference<HomePresenter> ref) {
        return new RxCommand<Location>() {
            @Override
            public Void execute(Location location) {
                HomePresenter p = ref.get();
                p.getModel().currentLocation.set(location);
                p.onUpdateViewModel();
                p.getViewModel().invalidateViews();

                return null;
            }
        };
    }

    private static RxCommand<Throwable> onError(final CheckedReference<HomePresenter> ref) {
        return new RxCommand<Throwable>() {
            @Override
            public Void execute(Throwable throwable) {
                Logger.getInstance().exception(throwable);
                HomePresenter p = ref.get();
                int snackbarTextResource = R.string.screen_home_location_error_msg;
                p.getViewModel().snackBarText.set(AppResources.string(snackbarTextResource));
                p.getViewModel().currentLocation.set(null);
                p.getViewModel().invalidateViews();
                p.onUpdateModel();
                return null;
            }
        };
    }


}
