package com.appzoneltd.lastmile.customer.failures;

import android.content.Intent;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.features.login.LoginActivity;
import com.base.abstraction.events.Event;
import com.base.abstraction.events.Message;
import com.base.abstraction.exceptions.failures.SessionExpiredFailure;
import com.base.abstraction.system.App;
import com.base.abstraction.system.Preferences;
import com.base.presentation.repos.base.RepositoriesGroup;
import com.base.presentation.requests.ActionType;
import com.base.presentation.requests.ActivityActionRequest;

/**
 * a class that handles {@link SessionExpiredFailure} for this
 * project
 * <p>
 * Created by Ahmed Adel on 10/30/2016.
 */
public class SessionExpiredHandler extends com.base.abstraction.failures.SessionExpiredHandler {

    @Override
    public Void execute(Event event) {
        Preferences.getInstance().remove(R.string.PREFS_KEY_TOKEN);

        ActivityActionRequest request = new ActivityActionRequest(ActionType.START_ACTIVITY)
                .action(LoginActivity.class)
                .flags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        event = new Event.Builder(R.id.startActivityAction)
                .message(new Message.Builder().content(request).build())
                .receiverActorAddresses(R.id.addressActivity)
                .build();

        App.getInstance().getActorSystem().send(event);

        RepositoriesGroup.setHealingRepository(null);
        return null;
    }
}
