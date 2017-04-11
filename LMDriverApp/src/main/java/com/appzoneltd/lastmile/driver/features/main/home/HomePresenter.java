package com.appzoneltd.lastmile.driver.features.main.home;

import android.content.Intent;
import android.location.Location;

import com.appzoneltd.lastmile.driver.R;
import com.appzoneltd.lastmile.driver.features.main.model.MainModel;
import com.appzoneltd.lastmile.driver.services.tracking.LocationTrackingService;
import com.base.abstraction.annotations.interfaces.Executable;
import com.base.abstraction.annotations.interfaces.Load;
import com.base.abstraction.events.Message;
import com.base.abstraction.system.AppResources;
import com.base.presentation.annotations.interfaces.OnClickHandler;
import com.base.presentation.base.presentation.Presenter;
import com.base.usecases.annotations.ResponsesHandler;


/**
 * the {@link Presenter} for the home screen
 * <p>
 * Created by Ahmed Adel on 12/20/2016.
 */
@Load
@OnClickHandler(HomeClickHandler.class)
@ResponsesHandler(HomePresenterResponseHandler.class)
class HomePresenter extends Presenter<HomePresenter, HomeViewModel, MainModel> {


    @Executable(R.id.onCreate)
    void onCreate(Message message) {
        startLocationTrackingService();
    }

    @Executable(R.id.onGPSStateChanged)
    void onGpsStateChangedAndOnResume(Message message) {
        boolean gpsOn = message.getContent();
        if (!gpsOn || !getManifestPermissions().isLocationPermissionAllowed()) {
            new HomeLocationConnectionHandler().execute(this);
        } else {
            updateMapAndStartTrackingService();
        }
    }

    private void updateMapAndStartTrackingService() {
        if (!getModel().navigating.isTrue()) {
            getModel().jobOrdersRoute.requestIfRequired();
        }
        startLocationTrackingService();
    }

    private void startLocationTrackingService() {
        getHostActivity().startService(new Intent(getHostActivity(), LocationTrackingService.class));
    }


    @Executable(R.id.onFineLocationDialogCancelled)
    void onFineLocationDialogCancelled(Message message) {
        getViewModel().progressBarVisible.set(false);
        getViewModel().snackBarText.set(AppResources.string(R.string.fine_location_dialog_title));
        getViewModel().invalidateViews();
    }

    @Executable(R.id.onLocationTrackingServiceUpdateLocation)
    void onUpdateLocation(Message message) {
        getModel().currentLocation.set((Location) message.getContent());
        onUpdateViewModel();
    }


    @Executable(R.id.onBackPressed)
    void onBackPressed(Message message) {
        if (getModel().navigating.isTrue()) {
            String text = AppResources.string(R.string.screen_home_navigation_active);
            getViewModel().snackBarText.set(text);
            getViewModel().invalidateViews();
        }
    }


}
