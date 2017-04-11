package com.appzoneltd.lastmile.customer.features.main.packageslist.models;

import com.appzoneltd.lastmile.customer.R;
import com.base.abstraction.annotations.interfaces.Executable;
import com.base.abstraction.events.Message;
import com.base.presentation.models.ModelUpdatesHandler;
import com.base.usecases.events.RequestMessage;

/**
 * the {@link ModelUpdatesHandler} for the {@link PackageListModel}
 * <p>
 * Created by Wafaa on 12/15/2016.
 */

public class PackageListModelUpdatesHandler extends ModelUpdatesHandler<PackageListModel> {

    @Executable(R.id.requestPackagesList)
    void requestPackagesList(Message message) {
        RequestMessage requestMessage = new RequestMessage.Builder().build();
        requestFromRepository(R.id.requestPackagesList, requestMessage);
    }

    @Executable(R.id.requestRating)
    void requestSubmitRating(Message message){
        RequestMessage requestMessage = new RequestMessage.Builder()
                .content(getModel().rating).build();
        requestFromRepository(R.id.requestRating, requestMessage);
    }

}
