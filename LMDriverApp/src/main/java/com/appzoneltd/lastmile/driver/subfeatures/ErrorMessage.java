package com.appzoneltd.lastmile.driver.subfeatures;

import android.text.TextUtils;

import com.appzoneltd.lastmile.driver.R;
import com.base.abstraction.commands.Command;
import com.base.abstraction.system.AppResources;
import com.base.cached.ServerMessage;
import com.base.usecases.events.ResponseMessage;

/**
 * a class that reads the {@link ServerMessage} returned in a failure {@link ResponseMessage}, and
 * if there is no {@link ServerMessage}, it generates the required message to be displayed
 * <p>
 * Created by Ahmed Adel on 2/14/2017.
 */
public class ErrorMessage implements Command<ResponseMessage, String> {

    @Override
    public String execute(ResponseMessage message) {

        String text = null;

        Object content = message.getContent();
        if (content != null && content instanceof ServerMessage) {
            text = ((ServerMessage) content).getMessage();
        }

        if (TextUtils.isEmpty(text)) {
            text = AppResources.string(R.string.server_error_message);
            text += " (" + message.getStatusCode() + ") ";
        }
        
        return text;
    }

}
