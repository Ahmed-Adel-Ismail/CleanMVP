package com.appzoneltd.lastmile.driver.services.pickups;

import android.widget.Toast;

import com.appzoneltd.lastmile.driver.R;
import com.appzoneltd.lastmile.driver.subfeatures.ErrorMessage;
import com.base.abstraction.commands.Command;
import com.base.abstraction.system.App;
import com.base.abstraction.system.AppResources;
import com.base.cached.ServerMessage;
import com.base.presentation.annotations.interfaces.JsonRequest;
import com.base.presentation.annotations.interfaces.Repository;
import com.base.presentation.models.Model;
import com.base.presentation.references.Entity;
import com.base.usecases.events.ResponseMessage;


@Repository(OnDemandPickupRepository.class)
class OnDemandPickupModel extends Model {

    @JsonRequest(R.id.requestSubmitDriverAcceptedOnDemandPickup)
    public final Entity<ServerMessage> driverPickupAccept = new Entity<>();

    @JsonRequest(R.id.requestSubmitDriverRejectedOnDemandPickup)
    public final Entity<ServerMessage> driverPickupRejection = new Entity<>();

    public OnDemandPickupModel() {
        driverPickupAccept.onComplete(showToast());
        driverPickupRejection.onComplete(showToast());
    }

    private Command<ResponseMessage, ?> showToast() {
        return new Command<ResponseMessage, Void>() {
            @Override
            public Void execute(ResponseMessage responseMessage) {

                String message = (!responseMessage.isSuccessful())
                        ? new ErrorMessage().execute(responseMessage)
                        : AppResources.string(R.string.server_success_message);

                Toast.makeText(App.getInstance(), message, Toast.LENGTH_LONG).show();

                return null;
            }
        };
    }
}
