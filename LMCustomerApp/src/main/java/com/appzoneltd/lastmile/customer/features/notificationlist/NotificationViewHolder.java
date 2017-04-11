package com.appzoneltd.lastmile.customer.features.notificationlist;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.features.pickup.models.Package;
import com.base.abstraction.annotations.interfaces.Executable;
import com.base.abstraction.events.Event;
import com.base.abstraction.logs.Logger;
import com.base.abstraction.serializers.StringJsonParser;
import com.base.abstraction.system.AppResources;
import com.base.presentation.listeners.OnListItemEventListenerParams;
import com.base.presentation.listeners.OnListItemEventListener;
import com.base.presentation.views.adapters.AbstractViewHolder;
import com.entities.Notification;
import com.entities.cached.PayloadTrackingNumber;
import com.google.gson.JsonStreamParser;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Wafaa on 11/14/2016.
 */

public class NotificationViewHolder extends AbstractViewHolder<Notification> {

    @BindView(R.id.notification_options_layout)
    ViewGroup notificationOptionsLayout;
    @BindView(R.id.notification_image)
    CircleImageView notificationImage;
    @BindView(R.id.notification_title)
    TextView notificationTitle;
    @BindView(R.id.notification_time)
    TextView notificationTime;
    @BindView(R.id.notification_msg)
    TextView notificationMsg;
    @BindView(R.id.notification_reschedule)
    TextView reschedule;
    @BindView(R.id.notification_try_later)
    TextView tryLater;
    @BindView(R.id.notification_cancel)
    TextView cancelRequest;
    @BindView(R.id.layout_notification_item_progress)
    ProgressBar layoutNotificationItemProgress;

    private OnListItemEventListener onListItemEventListener;
    private OnListItemEventListenerParams params;
    private Notification notificationItem;

    NotificationViewHolder(View itemView) {
        super(itemView);
        params = new OnListItemEventListenerParams();
        onListItemEventListener = new OnListItemEventListener(this);
        itemView.setOnClickListener(onListItemEventListener);
    }

    @Override
    protected void draw(Notification item) {
        this.notificationItem = item;
        notificationTitle.setText(item.getNotificationItemTitle());
        setNotificationMsg(extractPayload());
        notificationTime.setText(item.getTimeFormatted(item.getTime()));
        params.setEntity(item);
        params.setPosition(getAdapterPosition());
        onListItemEventListener.setParams(params);
        reschedule.setOnClickListener(onListItemEventListener);
        tryLater.setOnClickListener(onListItemEventListener);
        cancelRequest.setOnClickListener(onListItemEventListener);
        if (item.isRequesting()) {
            layoutNotificationItemProgress.setVisibility(View.VISIBLE);
        } else {
            layoutNotificationItemProgress.setVisibility(View.INVISIBLE);
        }
    }

    private void setNotificationMsg(PayloadTrackingNumber payload) {
        if (payload != null) {
            if (notificationItem.getType().equals(AppResources.string(
                    R.string.notification_tracking_number))) {
                notificationMsg.setText(notificationItem.getNotificationItemBody()
                        + " " + payload.getValue());
                return;
            }
        }
        notificationMsg.setText(notificationItem.getNotificationItemBody());
    }

    private PayloadTrackingNumber extractPayload() {
        try {
            return new StringJsonParser<>(PayloadTrackingNumber.class)
                    .execute(notificationItem.getPayload());
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public void onUpdate(Event event) throws RuntimeException {
        OnListItemEventListenerParams params = event.getMessage().getContent();
        if (params.getViewId() == reschedule.getId()) {
            Logger.getInstance().info(getClass(), "reschedule");
        } else if (params.getViewId() == tryLater.getId()) {
            Logger.getInstance().info(getClass(), "try later");
        } else if (params.getViewId() == cancelRequest.getId()) {
            layoutNotificationItemProgress.setVisibility(View.VISIBLE);
            notificationOptionsLayout.setVisibility(View.GONE);
        } else if (notificationItem.getType().endsWith(
                AppResources.string(R.string.notification_driver_on_his_way))) {
            layoutNotificationItemProgress.setVisibility(View.VISIBLE);
            notificationOptionsLayout.setVisibility(View.GONE);
        } else if (notificationItem.getType().equals(
                AppResources.string(R.string.notification_driver_busy))) {
            switchOptionsLayoutVisibility();
        }
    }

    private void switchOptionsLayoutVisibility() {
        if (notificationOptionsLayout.getVisibility() == View.VISIBLE) {
            notificationOptionsLayout.setVisibility(View.GONE);
        } else {
            notificationOptionsLayout.setVisibility(View.VISIBLE);
        }
    }

}
