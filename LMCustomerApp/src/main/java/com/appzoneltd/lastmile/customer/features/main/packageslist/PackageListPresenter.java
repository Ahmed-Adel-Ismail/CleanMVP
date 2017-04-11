package com.appzoneltd.lastmile.customer.features.main.packageslist;

import android.content.Intent;
import android.os.Bundle;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.features.main.packageslist.models.PackageListModel;
import com.appzoneltd.lastmile.customer.features.tracking.host.TrackingActivity;
import com.appzoneltd.lastmile.customer.firebase.NotificationCounterChanger;
import com.base.abstraction.annotations.interfaces.Executable;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.abstraction.events.EventBuilder;
import com.base.abstraction.events.Message;
import com.base.abstraction.system.AppResources;

import com.base.presentation.base.presentation.Presenter;
import com.base.presentation.listeners.OnListItemEventListenerParams;
import com.base.presentation.requests.ActionType;
import com.base.presentation.requests.ActivityActionRequest;
import com.base.usecases.annotations.ResponsesHandler;
import com.entities.cached.NotificationTypes;
import com.entities.cached.PickupStatus;
import com.entities.cached.PickupStatusGroup;

/**
 * the {@link Presenter} for the packages list
 * <p>
 * Created by Wafaa on 12/14/2016.
 */
@ResponsesHandler(PackageListResponseHandler.class)
class PackageListPresenter extends Presenter
        <PackageListPresenter, PackageListViewModel, PackageListModel> {

    private CommandExecutor<Long, OnListItemEventListenerParams> onItemListSelectedExecutor;
    private CommandExecutor<Long, Message> onNotificationReceivedExecutor;

    @Override
    public void initialize(PackageListViewModel viewModel) {
        super.initialize(viewModel);
        extractPickupStatus(getHostActivity().getIntent());
        getViewModel().invalidateViews();
        getModel().execute(new EventBuilder(R.id.requestPackagesList).execute(this));
        onItemListSelectedExecutor = createOnItemListSelectedCommandExecutor();
        onNotificationReceivedExecutor = new NotificationHandlerExecutor(this);
    }

    private void extractPickupStatus(Intent intent) {
        if (intent != null) {
            Bundle extra = intent.getExtras();
            if (extra != null) {
                PickupStatus pickupStatus = (PickupStatus) extra.getSerializable(
                        AppResources.string(R.string.INTENT_KEY_PICKUP_STATUS));
                if (pickupStatus != null) {
                    PickupStatusGroup group = new PickupStatusGroup();
                    group.add(pickupStatus);
                    getModel().setPickupStatuses(group);
                }
            }
        }
    }


    private CommandExecutor<Long, OnListItemEventListenerParams> createOnItemListSelectedCommandExecutor() {
        CommandExecutor<Long, OnListItemEventListenerParams> commandExecutor = new CommandExecutor<>();
        Command<OnListItemEventListenerParams, Void> command = createOnItemClickListener();
        commandExecutor.put((long) R.id.layout_package_list_item_layout, command);
        return commandExecutor;
    }

    private Command<OnListItemEventListenerParams, Void> createOnItemClickListener() {
        return new Command<OnListItemEventListenerParams, Void>() {
            @Override
            public Void execute(OnListItemEventListenerParams params) {
                if (params.getEntity() != null) {
                    PickupStatus pickupStatus = (PickupStatus) params.getEntity();
                    if (pickupStatus != null) {
                        startTrackingActivity(pickupStatus);
                    }
                }
                return null;
            }

            private void startTrackingActivity(PickupStatus pickupStatus) {
                if (pickupStatus.getStatus().equals(NotificationTypes.NEW)
                        || pickupStatus.getStatus().equals(NotificationTypes.WAITING_FOR_CUSTOMER_ALTERNATIVE)
                        || pickupStatus.getStatus().equals(NotificationTypes.ACTION_NEEDED)) {
                    invokeTrackingActivity(pickupStatus);
                }
            }

            private void invokeTrackingActivity(PickupStatus pickupStatus) {
                ActivityActionRequest request = new ActivityActionRequest(ActionType.START_ACTIVITY);
                request.action(TrackingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(AppResources.string(R.string.INTENT_KEY_PICKUP_STATUS)
                        , pickupStatus);
                request.extras(bundle);
                EventBuilder eventBuilder = new EventBuilder(R.id.startActivityAction, request);
                getFeature().startActivityActionRequest(eventBuilder.execute(PackageListPresenter.this));
            }
        };
    }

    @Executable(R.id.onListItemEventListener)
    void onItemClickedExecutor(Message message) {
        OnListItemEventListenerParams params = message.getContent();
        long id = params.getView().getId();
        onItemListSelectedExecutor.execute(id, params);
    }

    @Executable(R.id.onHandleNotification)
    void createOnNotificationReceived(Message message) {
        new NotificationCounterChanger().execute(null);
        onNotificationReceivedExecutor.execute(message.getId(), message);
    }

}
