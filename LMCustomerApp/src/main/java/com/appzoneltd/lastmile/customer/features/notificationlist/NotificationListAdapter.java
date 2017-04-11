package com.appzoneltd.lastmile.customer.features.notificationlist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appzoneltd.lastmile.customer.R;
import com.base.presentation.views.adapters.AbstractAdapter;
import com.entities.Notification;

/**
 * Created by Wafaa on 11/14/2016.
 */

public class NotificationListAdapter extends AbstractAdapter<Notification, NotificationViewHolder> {


    @Override
    protected NotificationViewHolder createAbstractViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_notification_item, parent, false);
        return new NotificationViewHolder(view);
    }

}
