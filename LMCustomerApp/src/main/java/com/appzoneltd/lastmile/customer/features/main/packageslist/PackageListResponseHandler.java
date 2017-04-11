package com.appzoneltd.lastmile.customer.features.main.packageslist;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.features.main.packageslist.models.PackageListModel;
import com.appzoneltd.lastmile.customer.subfeatures.notificationcommands.NotificationsItemDeletion;
import com.base.abstraction.annotations.interfaces.Executable;
import com.base.abstraction.events.Event;
import com.base.abstraction.events.Message;
import com.base.abstraction.system.App;
import com.entities.Notification;
import com.base.presentation.base.presentation.PresenterResponsesHandler;
import com.base.usecases.events.ResponseMessage;
import com.entities.cached.PickupStatusGroup;

/**
 * Created by Wafaa on 12/15/2016.
 */

public class PackageListResponseHandler extends PresenterResponsesHandler
        <PackageListPresenter, PackageListViewModel, PackageListModel> {

    @Executable(R.id.requestPackagesList)
    void requestAllPackages(ResponseMessage message) {
        getViewModel().setProgressVisibility(false);
        if (message.isSuccessful()) {
            PickupStatusGroup group = message.getContent();
            group = addAndReturnPickupsGroup(group);
            if (group.size() > 0) {
                getModel().setPickupStatuses(group);
            } else {
                getViewModel().setErrorMsgVisibility(true);
            }
        } else if ((getModel().getPickupStatuses() == null
                || getModel().getPickupStatuses().size() == 0)) {
            getViewModel().setErrorMsgVisibility(true);
        }
        updateViewModel();
        getViewModel().invalidateViews();
    }

    private PickupStatusGroup addAndReturnPickupsGroup(PickupStatusGroup pickupStatuses) {
        PickupStatusGroup group = getModel().getPickupStatuses();
        if (group == null) {
            group = new PickupStatusGroup();
            group.addAll(pickupStatuses);
        }
        return group;
    }

    @Executable(R.id.requestRating)
    void submitRatingResponse(ResponseMessage message) {
        if (message.isSuccessful()) {
            new NotificationsItemDeletion<>(getModel().notification).execute(Notification.class);
            notifyToShowToast(R.string.rating_sucess_response_msg);
        } else {
            notifyToShowToast(R.string.rating_failed_response_msg);
        }
        getHostActivity().finish();
    }

    private void notifyToShowToast(int msgResource) {
        Event event = new Event.Builder(R.id.showToast).message(new Message.Builder()
                .content(msgResource).build()).build();
        App.getInstance().getActorSystem()
                .get((long) R.id.addressActivity)
                .execute(event);
    }
}
