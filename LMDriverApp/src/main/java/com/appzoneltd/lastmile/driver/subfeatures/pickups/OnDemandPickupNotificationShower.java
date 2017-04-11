package com.appzoneltd.lastmile.driver.subfeatures.pickups;

import android.support.annotation.NonNull;

import com.appzoneltd.lastmile.driver.Flow;
import com.appzoneltd.lastmile.driver.R;
import com.base.abstraction.commands.Command;
import com.base.abstraction.events.Message;
import com.base.abstraction.logs.Logger;
import com.base.abstraction.system.App;
import com.base.abstraction.system.AppResources;
import com.base.abstraction.system.Preferences;
import com.base.presentation.notifications.builders.NotificationShower;

/**
 * a class that handles showing On-Demand pickup notification
 * <p>
 * Created by Ahmed Adel on 2/21/2017.
 */
public class OnDemandPickupNotificationShower implements Command<Message, Void> {

    @Override
    public Void execute(Message message) {
        if (Preferences.getInstance().hasKey(R.string.PREFS_KEY_TOKEN)) {
            showNotificationIfPossible(message);
        } else {
            logUnsupportedOperationException();
        }
        return null;
    }

    private void showNotificationIfPossible(Message message) {
        if (!App.getInstance().isInForeground(Flow.MainActivity.class)) {
            showNotification(message);
        } else {
            logIgnoredMessage();
        }
    }


    private void showNotification(Message message) {
        int notificationId = AppResources.integer(R.integer.onDemandPickupNotificationId);
        String notificationTag = createRandomTag();
        OnDemandPickupNotificationBuilder builder;
        builder = new OnDemandPickupNotificationBuilder(notificationTag);
        new NotificationShower(notificationTag, notificationId).execute(builder.execute(message));
    }

    @NonNull
    private String createRandomTag() {
        String randomValue = String.valueOf((long) (Math.random() * 50000));
        Class<?> flowClass = Flow.class;
        return flowClass.getName().replace(flowClass.getSimpleName(), "") + randomValue;
    }

    private void logIgnoredMessage() {
        Logger.getInstance().error(getClass(), Flow.MainActivity.class.getSimpleName()
                + " is in foreground already");
    }

    private void logUnsupportedOperationException() {
        Logger.getInstance().exception(new UnsupportedOperationException("must be logged in " +
                " to show on-demand pickup notifications"));
    }


}
