package com.appzoneltd.lastmile.driver.subfeatures.pickups;

import com.base.abstraction.commands.Command;
import com.base.abstraction.events.Message;
import com.base.abstraction.exceptions.CheckedReferenceClearedException;
import com.base.abstraction.logs.Logger;
import com.base.abstraction.references.CheckedReference;
import com.base.presentation.base.abstracts.features.AbstractActivity;
import com.base.presentation.views.dialogs.EventDialog;
import com.base.presentation.views.dialogs.EventDialogBuilder;

/**
 * a class to handle On-demand-pickup-request dialog showing
 * <p>
 * Created by Ahmed Adel on 12/28/2016.
 */
public class OnDemandPickupDialog implements Command<Message, Void> {

    private CheckedReference<AbstractActivity> activity;

    public OnDemandPickupDialog(AbstractActivity activity) {
        this.activity = new CheckedReference<>(activity);
    }

    @Override
    public Void execute(Message message) {
        try {
            EventDialogBuilder dialogBuilder = new OnDemandPickupDialogBuilder().execute(message);
            new EventDialog(dialogBuilder, activity.get()).show();
        } catch (CheckedReferenceClearedException e) {
            Logger.getInstance().exception(e);
        }
        return null;
    }


}
