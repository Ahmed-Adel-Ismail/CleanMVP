package com.appzoneltd.lastmile.customer.features.main.packageslist.models;

import com.appzoneltd.lastmile.customer.R;
import com.base.abstraction.annotations.interfaces.Executable;
import com.base.abstraction.events.Event;
import com.base.abstraction.events.Message;
import com.base.abstraction.system.App;
import com.base.presentation.models.ModelResponsesHandler;
import com.base.usecases.events.ResponseMessage;

/**
 * Created by Wafaa on 12/15/2016.
 */

public class PackagesListModelResponseHandler extends ModelResponsesHandler<PackageListModel> {

    @Executable(R.id.requestPackagesList)
    void requestPackagesList(ResponseMessage message) {
        if (!message.isSuccessful()) {
            notifyToShowToast(R.string.request_app_packages_failed_msg);
        }
        notifyOnRepositoryResponse(message);
    }

    private void notifyToShowToast(int msgResource) {
        Event event = new Event.Builder(R.id.showToast).message(new Message.Builder()
                .content(msgResource).build()).build();
        App.getInstance().getActorSystem()
                .get((long) R.id.addressActivity)
                .execute(event);
    }

    @Executable(R.id.requestRating)
    void requestSubmitRating(ResponseMessage message) {
        notifyOnRepositoryResponse(message);
    }

}
