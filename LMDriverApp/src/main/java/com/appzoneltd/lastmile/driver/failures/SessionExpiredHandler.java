package com.appzoneltd.lastmile.driver.failures;

import android.content.Intent;

import com.appzoneltd.lastmile.driver.Flow;
import com.appzoneltd.lastmile.driver.R;
import com.base.abstraction.events.Event;
import com.base.abstraction.events.Message;
import com.base.abstraction.system.App;
import com.base.abstraction.system.Preferences;
import com.base.presentation.repos.base.RepositoriesGroup;
import com.base.presentation.requests.ActionType;
import com.base.presentation.requests.ActivityActionRequest;

/**
 * the {@link com.base.abstraction.failures.FailureHandler} for
 * {@link com.base.abstraction.exceptions.failures.SessionExpiredFailure} in this project
 * <p>
 * Created by Ahmed Adel on 11/21/2016.
 */
public class SessionExpiredHandler extends com.base.abstraction.failures.SessionExpiredHandler {

    @Override
    public Void execute(Event event) {

        Preferences.getInstance().remove(R.string.PREFS_KEY_TOKEN);

        event = new Event.Builder(R.id.onSessionExpired)
                .receiverActorAddresses(R.id.addressLocationTrackingService)
                .build();

        App.getInstance().getActorSystem().send(event);

        ActivityActionRequest request = new ActivityActionRequest(ActionType.START_ACTIVITY)
                .action(Flow.LoginActivity.class)
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

