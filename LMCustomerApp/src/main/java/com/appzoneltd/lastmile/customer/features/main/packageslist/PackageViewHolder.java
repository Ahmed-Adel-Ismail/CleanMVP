package com.appzoneltd.lastmile.customer.features.main.packageslist;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.features.main.packageslist.viewholderfactory.PackageCancelled;
import com.appzoneltd.lastmile.customer.features.main.packageslist.viewholderfactory.PackageDelivered;
import com.appzoneltd.lastmile.customer.features.main.packageslist.viewholderfactory.PackageInProcessAwaiting;
import com.appzoneltd.lastmile.customer.features.main.packageslist.viewholderfactory.PackageInProcessInHub;
import com.appzoneltd.lastmile.customer.features.main.packageslist.viewholderfactory.PackageInProcessInPickupVerification;
import com.appzoneltd.lastmile.customer.features.main.packageslist.viewholderfactory.PackageInProcessNew;
import com.appzoneltd.lastmile.customer.features.main.packageslist.viewholderfactory.PackageInProcessOutForDelivery;
import com.appzoneltd.lastmile.customer.features.main.packageslist.viewholderfactory.PackageInProcessPickedUp;
import com.appzoneltd.lastmile.customer.features.main.packageslist.viewholderfactory.PackageReturned;
import com.appzoneltd.lastmile.customer.features.main.packageslist.viewholderfactory.PackageScheduledForDelivery;
import com.appzoneltd.lastmile.customer.features.main.packageslist.viewholderfactory.PackageScheduledForPickup;
import com.appzoneltd.lastmile.customer.features.main.packageslist.viewholderfactory.PackageScheduledForReturn;
import com.appzoneltd.lastmile.customer.features.main.packageslist.viewholderfactory.ViewHolderFactory;
import com.base.abstraction.events.Event;
import com.base.abstraction.system.AppResources;
import com.entities.cached.NotificationTypes;
import com.entities.cached.PickupStatus;
import com.base.presentation.listeners.OnListItemEventListenerParams;
import com.base.presentation.listeners.OnListItemEventListener;
import com.base.presentation.views.adapters.AbstractViewHolder;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Wafaa on 12/15/2016.
 */


public class PackageViewHolder extends AbstractViewHolder<PickupStatus> {

    @BindView(R.id.layout_package_list_item_layout)
    ViewGroup backgroundRectangle;
    @BindView(R.id.layout_package_list_item_image_background)
    public ViewGroup imageBackground;
    @BindView(R.id.layout_package_list_item_image)
    public ImageView image;
    @BindView(R.id.package_item_nickname_value)
    TextView nicknameValue;
    @BindView(R.id.package_item_status_value)
    TextView statusValue;
    @BindView(R.id.package_item_recipient_name_value)
    TextView recipientNameValue;
    @BindView(R.id.package_item_recipient_address)
    TextView recipientAddress;
    @BindView(R.id.layout_package_list_item_nickname_layout)
    ViewGroup nicknameLayout;

    private OnListItemEventListener onListItemEventListener;
    private OnListItemEventListenerParams params;
    private Map<NotificationTypes, ViewHolderFactory> viewHolderMap = initializeViewHolderMap();


    private Map<NotificationTypes, ViewHolderFactory> initializeViewHolderMap() {
        Map<NotificationTypes, ViewHolderFactory> viewHolderMap = new HashMap<>();
        viewHolderMap.put(NotificationTypes.NEW
                , new PackageInProcessNew(this));
        viewHolderMap.put(NotificationTypes.ACTION_NEEDED
                , new PackageInProcessNew(this));
        viewHolderMap.put(NotificationTypes.AWAITING_PICKUP
                , new PackageInProcessAwaiting(this));
        viewHolderMap.put(NotificationTypes.WAITING_FOR_CUSTOMER_ALTERNATIVE
                , new PackageInProcessInPickupVerification(this));
        viewHolderMap.put(NotificationTypes.Out_For_Delivery
                , new PackageInProcessOutForDelivery(this));
        viewHolderMap.put(NotificationTypes.PICKEDUP
                , new PackageInProcessPickedUp(this));
        viewHolderMap.put(NotificationTypes.IN_PICKUP_VERIFICATION
                , new PackageInProcessInHub(this));
        viewHolderMap.put(NotificationTypes.CANCELED
                , new PackageCancelled(this));
        return viewHolderMap;
    }

    public PackageViewHolder(View itemView) {
        super(itemView);
        params = new OnListItemEventListenerParams();
        onListItemEventListener = new OnListItemEventListener(this);
        itemView.setOnClickListener(onListItemEventListener);
    }


    @Override
    protected void draw(PickupStatus item) {
        params.setEntity(item);
        params.setPosition(getAdapterPosition());
        onListItemEventListener.setParams(params);
        if (viewHolderMap.get(item.getStatus()) != null) {
            viewHolderMap.get(item.getStatus()).onDrawITemLayout();
        } else {
            viewHolderMap.get(NotificationTypes.ACTION_NEEDED).onDrawITemLayout();
        }
        drawNickName(item.getNickname());
        if (item.getStatus() != null) {
            statusValue.setText(item.getStatus().toString());
        }
        recipientNameValue.setText(item.getRecipientName());
        recipientAddress.setText(item.getRecipientAddress());
    }


    private void drawNickName(String nickname) {
        if (TextUtils.isEmpty(nickname)) {
            nicknameLayout.setVisibility(View.GONE);
        } else {
            nicknameValue.setText(nickname);
        }
    }

    @Override
    public void onUpdate(Event event) throws RuntimeException {

    }

}
