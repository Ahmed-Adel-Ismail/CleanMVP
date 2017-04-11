package com.appzoneltd.lastmile.driver.features.main.host;

import android.widget.Toast;

import com.appzoneltd.lastmile.driver.R;
import com.appzoneltd.lastmile.driver.features.main.model.MainModel;
import com.appzoneltd.lastmile.driver.subfeatures.pickups.OnDemandPickupDialog;
import com.base.abstraction.annotations.interfaces.Executable;
import com.base.abstraction.annotations.interfaces.ExecutableCommand;
import com.base.abstraction.annotations.interfaces.Load;
import com.base.abstraction.events.Message;
import com.base.presentation.annotations.interfaces.OnClickHandler;
import com.base.presentation.base.presentation.Presenter;
import com.base.presentation.exceptions.OnBackPressException;
import com.base.presentation.references.Entity;
import com.base.usecases.annotations.ResponsesHandler;
import com.entities.cached.pickup.OnDemandPickupRequestAssignedPayload;

@Load
@OnClickHandler(MainPresenterClicksHandler.class)
@ResponsesHandler(MainPresenterResponsesHandler.class)
class MainPresenter extends Presenter<MainPresenter, MainViewModel, MainModel> {

    @ExecutableCommand(R.id.onNotifiedOnDemandPickup)
    OnDemandPickupDialog onNotificationReceived() {
        return new OnDemandPickupDialog(getHostActivity());
    }


    @Executable(R.id.onDemandPickupRequestAssignedDialog)
    void onDemandPickupRequestAssignedDialog(Message message) {
        if (getHostActivity().getSystemServices().isNetworkConnected()) {
            sendActionToServer(message);
        } else {
            showConnectionErrorToast();
        }
    }

    private void sendActionToServer(Message message) {

        getViewModel().showRequestsProgressBar.set(true);


        OnDemandPickupRequestAssignedPayload payload = message.getContent();
        Entity<?> entity = (message.getId() == R.id.onDialogPositiveClick)
                ? getModel().driverPickupAccept
                : getModel().driverPickupRejection;

        entity.request(payload.getPackageId());
    }

    private void showConnectionErrorToast() {
        Toast.makeText(getHostActivity(), R.string.screen_home_connection_down, Toast.LENGTH_LONG)
                .show();
    }

    @Executable(R.id.onBackPressed)
    void onBackPressed(Message message) {
        if (getModel().navigating.isTrue()) {
            throw new OnBackPressException();
        }
    }


}
