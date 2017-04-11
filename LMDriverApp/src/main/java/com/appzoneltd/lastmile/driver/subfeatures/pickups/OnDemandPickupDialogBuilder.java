package com.appzoneltd.lastmile.driver.subfeatures.pickups;

import com.appzoneltd.lastmile.driver.R;
import com.base.abstraction.commands.Command;
import com.base.abstraction.events.Message;
import com.base.abstraction.system.App;
import com.base.abstraction.system.AppResources;
import com.base.cached.RemoteMessageData;
import com.base.presentation.notifications.PayloadReader;
import com.base.presentation.views.dialogs.EventDialogBuilder;
import com.entities.cached.pickup.OnDemandPickupRequestAssignedPayload;

/**
 * a class that parses the On-demand pickup-assigned notification into a {@link EventDialogBuilder}
 * <p>
 * Created by Ahmed Adel on 2/19/2017.
 */
class OnDemandPickupDialogBuilder implements Command<Message, EventDialogBuilder> {

    @Override
    public EventDialogBuilder execute(Message message) {
        OnDemandPickupRequestAssignedPayload payload = readPayload(message);
        EventDialogBuilder builder = new EventDialogBuilder(R.id.onDemandPickupRequestAssignedDialog);
        builder.setTitle(R.string.on_demand_pickup_request_assigned_dialog_title);
        builder.setMessage(formatPayloadDialogMessage(payload));
        builder.setPositiveText(R.string.on_demand_pickup_request_assigned_dialog_positive_button);
        builder.setNegativeText(R.string.on_demand_pickup_request_assigned_dialog_negative_button);
        builder.setTag(payload);
        return builder;
    }

    private OnDemandPickupRequestAssignedPayload readPayload(Message message) {
        RemoteMessageData data = message.getContent();
        Class<OnDemandPickupRequestAssignedPayload> klass;
        klass = OnDemandPickupRequestAssignedPayload.class;
        return new PayloadReader<>(klass).execute(data);
    }

    private String formatPayloadDialogMessage(OnDemandPickupRequestAssignedPayload p) {
        String message = AppResources.string(R.string.on_demand_pickup_request_assigned_dialog_message);
        message = String.format(App.getInstance().getLocale(), message, p.getWeight(), p.getAddress());
        return message;
    }
}
