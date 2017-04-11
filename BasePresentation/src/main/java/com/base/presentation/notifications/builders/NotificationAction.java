package com.base.presentation.notifications.builders;

import android.app.PendingIntent;
import android.support.annotation.DrawableRes;

/**
 * a class that indicates an action on a notification that triggers a pending intent
 * <p>
 * Created by Ahmed Adel on 2/19/2017.
 */
public class NotificationAction {

    @DrawableRes
    public final int icon;
    public final String label;
    final PendingIntent pendingIntent;

    private NotificationAction(Builder builder) {
        icon = builder.icon;
        label = builder.label;
        pendingIntent = builder.pendingIntent;
    }


    public static final class Builder {
        private int icon;
        private String label;
        private PendingIntent pendingIntent;

        public Builder() {
        }

        public Builder icon(int icon) {
            this.icon = icon;
            return this;
        }

        public Builder label(String label) {
            this.label = label;
            return this;
        }

        public Builder pendingIntent(PendingIntent pendingIntent) {
            this.pendingIntent = pendingIntent;
            return this;
        }

        public NotificationAction build() {
            return new NotificationAction(this);
        }
    }
}
