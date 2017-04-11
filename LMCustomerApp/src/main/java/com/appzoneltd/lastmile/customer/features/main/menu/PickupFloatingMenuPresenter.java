package com.appzoneltd.lastmile.customer.features.main.menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.features.main.models.PickupLocationModel;
import com.appzoneltd.lastmile.customer.subfeatures.menus.FloatingMenuParams;
import com.appzoneltd.lastmile.customer.subfeatures.menus.FloatingMenuParamsGroup;
import com.appzoneltd.lastmile.customer.subfeatures.menus.FloatingMenuPresenter;
import com.appzoneltd.lastmile.customer.subfeatures.menus.FloatingMenuViewModel;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.abstraction.events.EventBuilder;
import com.base.abstraction.events.Message;
import com.base.abstraction.system.AppResources;
import com.base.presentation.base.abstracts.features.ViewBinder;
import com.base.presentation.requests.ActionType;
import com.base.presentation.requests.ActivityActionRequest;

/**
 * A {@link FloatingMenuPresenter} that handles the Pickup floating menu
 * <p/>
 * Created by Ahmed Adel on 9/14/2016.
 */
class PickupFloatingMenuPresenter extends FloatingMenuPresenter {

    private PickupLocationModel pickupLocationModel;

    PickupFloatingMenuPresenter(ViewBinder viewBinder,
                                @NonNull FloatingMenuViewModel floatingMenuViewModel) {
        super(viewBinder, floatingMenuViewModel);
        pickupLocationModel = getExtra();
    }

    private PickupLocationModel getExtra() {
        PickupLocationModel model = null;
        Intent intent = getHostActivity().getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            model = (PickupLocationModel)
                    bundle.get(AppResources.string(R.string.INTENT_KEY_PICKUP_LOCATION_MODEL));
        }
        return model;
    }

    @Override
    protected FloatingMenuParamsGroup createInitialFloatingMenuParamsGroup() {
        FloatingMenuParamsGroup floatingMenuParamsGroup = new FloatingMenuParamsGroup();

        FloatingMenuParams.Builder b;
        b = new FloatingMenuParams.Builder(R.id.floating_btn_pickup_scheduled);
        b.imageResourceId(R.drawable.schedule);
        b.textResourceId(R.string.schedule_pickup_request);
        floatingMenuParamsGroup.add(b.build());

        b = new FloatingMenuParams.Builder(R.id.floating_btn_pickup_now);
        b.imageResourceId(R.drawable.send);
        b.textResourceId(R.string.request_pickup_now);
        floatingMenuParamsGroup.add(b.build());

        return floatingMenuParamsGroup;
    }


    @NonNull
    protected CommandExecutor<Long, View> createOnClickCommandExecutor() {
        CommandExecutor<Long, View> commandExecutor = new CommandExecutor<>();
        Command<View, Void> command = createPickupNowOnClickCommand();
        commandExecutor.put((long) R.id.floating_btn_pickup_now, command);
        command = createSchedulePickupOnClickCommand();
        commandExecutor.put((long) R.id.floating_btn_pickup_scheduled, command);
        return commandExecutor;
    }

    private Command<View, Void> createPickupNowOnClickCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                startPickupActivity(true);
                return null;
            }
        };
    }

    private Command<View, Void> createSchedulePickupOnClickCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View p) {
                startPickupActivity(false);
                return null;
            }

        };

    }

    private void startPickupActivity(boolean now) {
        ActivityActionRequest request =
                new ActivityActionRequest(ActionType.FINISH).codeOk();
        request.extra(R.string.INTENT_KEY_PICKUP_NOW_ACTIVITY, now);
        request.extra(R.string.INTENT_KEY_PICKUP_LOCATION_MODEL, pickupLocationModel);
        EventBuilder eventBuilder = new EventBuilder(R.id.startActivityAction, request);
        getFeature().startActivityActionRequest(eventBuilder.execute(getHostActivity()));
    }


    @Override
    protected CommandExecutor<Long, Message> createCommandExecutor() {
        return new CommandExecutor<>();
    }

    @Override
    public void onDestroy() {
        pickupLocationModel = null;
    }
}
