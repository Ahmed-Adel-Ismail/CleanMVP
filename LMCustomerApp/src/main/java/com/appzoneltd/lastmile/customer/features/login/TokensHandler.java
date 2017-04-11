package com.appzoneltd.lastmile.customer.features.login;

import android.text.TextUtils;

import com.appzoneltd.lastmile.customer.R;
import com.base.abstraction.commands.Command;
import com.base.abstraction.concurrency.ExecutionThread;
import com.base.abstraction.concurrency.Future;
import com.base.abstraction.events.Event;
import com.base.abstraction.logs.Logger;
import com.base.abstraction.serializers.JsonSaver;
import com.base.cached.Token;
import com.base.presentation.notifications.PushNotificationsTokenUploader;
import com.base.usecases.events.ResponseMessage;
import com.google.firebase.iid.FirebaseInstanceId;

/**
 * a class to handle tokens, it saves the login token and uploads the push-notifications
 * <p>
 * Created by Ahmed Adel on 12/14/2016.
 */
class TokensHandler implements Command<ResponseMessage, Future<Event>> {

    @Override
    public Future<Event> execute(ResponseMessage responseMessage) {
        saveLoginToken(responseMessage);
        return uploadPushNotificationToken();
    }

    private void saveLoginToken(ResponseMessage responseMessage) {
        Token token = responseMessage.getContent();
        new JsonSaver<Token>(R.string.PREFS_KEY_TOKEN).execute(token);
    }

    private Future<Event> uploadPushNotificationToken() {
        String pushNotificationToken = FirebaseInstanceId.getInstance().getToken();
        if (!TextUtils.isEmpty(pushNotificationToken)) {
            return new PushNotificationsTokenUploader()
                    .execute(pushNotificationToken)
                    .onThread(ExecutionThread.CURRENT);
        } else {
            Logger.getInstance().error(getClass(), "no push notification token saved to upload");
            return null;
        }
    }


}

