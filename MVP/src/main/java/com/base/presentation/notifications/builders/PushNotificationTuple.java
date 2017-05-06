package com.base.presentation.notifications.builders;

import android.app.PendingIntent;
import android.support.annotation.DrawableRes;

/**
 * Created by Wafaa on 11/22/2016.
 */
public class PushNotificationTuple {

    public final String title;
    public final String body;
    @DrawableRes
    final int smallIcon;
    @DrawableRes
    final int largeIcon;
    final PendingIntent contentPendingIntent;
    final NotificationAction positiveAction;
    final NotificationAction negativeAction;

    private PushNotificationTuple(Builder builder) {
        body = builder.body;
        title = builder.title;
        smallIcon = builder.smallIcon;
        largeIcon = builder.largeIcon;
        contentPendingIntent = builder.contentPendingIntent;
        positiveAction = builder.positiveAction;
        negativeAction = builder.negativeAction;
    }

    boolean hasPositiveAction() {
        return positiveAction != null;
    }

    boolean hasNegativeAction() {
        return negativeAction != null;
    }

    boolean hasSmallIcon() {
        return smallIcon != 0;
    }

    boolean hasLargeIcon() {
        return largeIcon != 0;
    }


    public static final class Builder {
        private String body;
        private String title;
        private int smallIcon;
        private int largeIcon;
        private PendingIntent contentPendingIntent;
        private NotificationAction positiveAction;
        private NotificationAction negativeAction;

        public Builder() {
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder smallIcon(int smallIcon) {
            this.smallIcon = smallIcon;
            return this;
        }

        public Builder largeIcon(int largeIcon) {
            this.largeIcon = largeIcon;
            return this;
        }

        public Builder contentPendingIntent(PendingIntent contentPendingIntent) {
            this.contentPendingIntent = contentPendingIntent;
            return this;
        }

        public Builder positiveAction(NotificationAction positiveAction) {
            this.positiveAction = positiveAction;
            return this;
        }

        public Builder negativeAction(NotificationAction negativeAction) {
            this.negativeAction = negativeAction;
            return this;
        }

        public PushNotificationTuple build() {
            return new PushNotificationTuple(this);
        }
    }
}
