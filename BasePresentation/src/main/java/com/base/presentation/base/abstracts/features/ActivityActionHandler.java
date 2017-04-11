package com.base.presentation.base.abstracts.features;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.base.abstraction.commands.Command;
import com.base.abstraction.events.EventBuilder;
import com.base.abstraction.events.Message;
import com.base.presentation.R;
import com.base.presentation.requests.ActivityActionRequest;

/**
 * A class used by {@link AbstractActivity} to handle {@link ActivityActionRequest}
 * <p/>
 * Created by Ahmed Adel on 10/16/2016.
 */
class ActivityActionHandler implements Command<ViewBinder, Void> {

    private ActivityActionRequest request;

    ActivityActionHandler(ActivityActionRequest request) {
        this.request = request;
    }

    @Override
    public Void execute(ViewBinder viewBinder) {
        viewBinder.onUpdate(new EventBuilder(R.id.onUpdateModel).execute(viewBinder));
        notifyOnActivityActionRequest(viewBinder);
        startNewAction(viewBinder.getHostActivity());
        return null;
    }


    private void notifyOnActivityActionRequest(ViewBinder viewBinder) {
        Message message = new Message.Builder().id(request.getCode()).content(request).build();
        viewBinder.onUpdate(new EventBuilder(R.id.onActivityActionRequest, message).execute(viewBinder));
    }


    private void startNewAction(AbstractActivity<?> activity) {
        activity.lockInteractions();
        switch (request.getActionType()) {
            case START_ACTIVITY:
                startActivityFromActionRequest(activity, createStartActivityIntent(activity));
                break;
            case START_SERVICE:
                activity.startService(createStartActivityIntent(activity));
                break;
            case FINISH:
                finishFromActionRequest(activity);
                break;
        }
        activity.unlockInteractions();
    }

    @NonNull
    private Intent createStartActivityIntent(AbstractActivity<?> activity) {
        Intent newIntent = createNewIntent(activity);
        Intent options = request.getData();
        if (options != null) {
            addAllOptions(newIntent, options);
        }
        return newIntent;
    }

    private Intent createNewIntent(AbstractActivity<?> activity) {
        String actionString = request.getActionString();
        Class<?> actionClass = request.getActionClass();
        Intent newIntent;
        if (!TextUtils.isEmpty(actionString)) {
            newIntent = new Intent(actionString);
        } else if (actionClass != null) {
            newIntent = new Intent(activity, actionClass);
        } else {
            return throwUnsupportedException(activity);
        }
        return newIntent;
    }

    private void startActivityFromActionRequest(AbstractActivity<?> activity, Intent newIntent) {
        if (request.hasCode()) {
            activity.startActivityForResult(newIntent, request.getCode());
        } else {
            activity.startActivity(newIntent);
        }
    }

    private void finishFromActionRequest(AbstractActivity<?> activity) {
        if (request.hasCode()) {
            activity.setResult(request.getCode(), request.getData());
        }
        activity.finish();
    }


    private Intent throwUnsupportedException(AbstractActivity<?> activity) {
        activity.unlockInteractions();
        throw new UnsupportedOperationException("null action @ "
                + request.getClass().getSimpleName());
    }

    private void addAllOptions(Intent newIntent, Intent options) {
        newIntent.addFlags(options.getFlags());
        Bundle bundle = options.getExtras();
        if (bundle != null) {
            newIntent.putExtras(options.getExtras());
        }
        Uri uri = options.getData();
        if(uri != null){
            newIntent.setData(uri);
        }
    }


}
