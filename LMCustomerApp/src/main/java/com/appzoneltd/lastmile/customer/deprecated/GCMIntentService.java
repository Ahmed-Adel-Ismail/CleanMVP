/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.appzoneltd.lastmile.customer.deprecated;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.deprecatred.RequestConstants;
import com.appzoneltd.lastmile.customer.deprecatred.SharedManager;
import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;

import java.util.Random;

/**
 * IntentService responsible for handling GCM messages.
 */
@Deprecated
public class GCMIntentService extends GCMBaseIntentService {

    @SuppressWarnings("hiding")
    private final String TAG = "GCMIntentService";
    private final String EXTRA_NOTIFICATION = "com.appzoneltd.lastmile.NOTIFICATION";
    private final int MAX_ATTEMPTS = 5;
    private final int BACKOFF_MILLI_SECONDS = 2000;
    private final Random random = new Random();

    public GCMIntentService() {
        super(RequestConstants.SENDER_ID);
    }

    @Override
    protected void onRegistered(Context context, String registrationId) {
        SharedManager.getInstance().setREgId(registrationId);
        register(context);
    }

    @Override
    protected void onUnregistered(Context context, String registrationId) {
        //displayMessage(context, getString(R.string.gcm_unregistered));
        if (GCMRegistrar.isRegisteredOnServer(context)) {
            unregister(context, registrationId);
        } else {
            // This callback results from the call to unregister made on
            // ServerUtilities when the registration to the server failed.
        }
    }

    @Override
    protected void onMessage(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String message = bundle.getString("message");
        //displayMessage(context, message);
        if (message != null && message.equals(""))
            generateNotification(context, message);
    }

    @Override
    protected void onDeletedMessages(Context context, int total) {
        //String message = getString(R.string.gcm_deleted, total);
        //displayMessage(context, message);
        // notifies user
        //generateNotification(context, message);
    }

    @Override
    public void onError(Context context, String errorId) {
        String s = errorId;
        //displayMessage(context, getString(R.string.gcm_error, errorId));
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
//        displayMessage(context, getString(R.string.gcm_recoverable_error,
//                errorId));
        return super.onRecoverableError(context, errorId);
    }

    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    private static void generateNotification(Context context, String message) {
        NotificationCompat.Builder notification;
        String title = context.getString(R.string.app_name);
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notification = new NotificationCompat.Builder(context);
        notification.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
        notification.setSmallIcon(R.mipmap.ic_launcher);
        notification.setContentTitle(title);
        notification.setContentText(message);
        Intent notificationIntent = new Intent(context, PushNotificationActivity.class);
        PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        notification.setContentIntent(intent);
        notification.setAutoCancel(true);
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        notificationManager.notify(1, notification.build());
    }

    public static boolean register(final Context context) {
        GCMRegistrar.setRegisteredOnServer(context, true);
        return true;
    }

    public static void unregister(final Context context, final String regId) {
        GCMRegistrar.setRegisteredOnServer(context, false);
    }

}
