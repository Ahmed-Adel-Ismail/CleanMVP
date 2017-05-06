package com.base.presentation.listeners;

import android.view.View;

import com.base.abstraction.events.Event;
import com.base.abstraction.events.Message;
import com.base.abstraction.observer.Observer;
import com.base.abstraction.system.App;
import com.base.abstraction.R;
import com.base.presentation.interfaces.Clickable;

import java.io.Serializable;

/**
 * Created by Wafaa on 11/15/2016.
 */

public class OnListItemEventListener<T extends Serializable & Cloneable> implements
        Clickable,
        View.OnClickListener {

    private OnListItemEventListenerParams params;
    private Observer observer;

    public OnListItemEventListener(Observer observer) {
        params = new OnListItemEventListenerParams();
        this.observer = observer;
    }

    @Override
    public void onClick(View view) {
        params.setView(view);
        params.setViewId(view.getId());
        Message message = new Message.Builder().content(params).build();
        Event event = new Event.Builder(R.id.onListItemEventListener)
                .message(message).build();
        App.getInstance().getActorSystem()
                .get((long) R.id.addressActivity)
                .execute(event);
        observer.onUpdate(event);
    }

    public OnListItemEventListenerParams getParams() {
        return params;
    }

    public void setParams(OnListItemEventListenerParams params) {
        this.params = params;
    }

}
