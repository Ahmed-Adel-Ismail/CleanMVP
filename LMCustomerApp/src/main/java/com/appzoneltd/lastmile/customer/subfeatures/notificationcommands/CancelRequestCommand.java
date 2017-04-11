package com.appzoneltd.lastmile.customer.subfeatures.notificationcommands;

import com.appzoneltd.lastmile.customer.R;
import com.base.abstraction.commands.Command;
import com.base.abstraction.events.Message;
import com.base.abstraction.serializers.StringJsonParser;
import com.base.presentation.models.Model;
import com.base.usecases.events.RequestMessage;
import com.entities.Notification;
import com.entities.cached.PayloadBusy;
import com.entities.requesters.CancelRequestParams;

/**
 * Created by Wafaa on 11/24/2016.
 */
public class CancelRequestCommand implements Command<Message, Void> {

    private Model model;

    public CancelRequestCommand(Model model) {
        this.model = model;
    }

    @Override
    public Void execute(Message message) {
        if (message != null) {
            long packageId = message.getContent();
            CancelRequestParams params = new CancelRequestParams();
            RequestMessage requestMessage = new RequestMessage.Builder()
                    .content(params).build();
            params.setPackageId(packageId);
            model.requestFromRepository(R.id.requestCancelPickupRequest, requestMessage);
        }
        return null;
    }

}
