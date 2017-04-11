package com.appzoneltd.lastmile.customer.features.receipt;

import android.view.View;
import android.widget.TextView;

import com.appzoneltd.lastmile.customer.R;
import com.base.abstraction.system.AppResources;
import com.entities.cached.PickupService;
import com.base.abstraction.events.Event;
import com.base.presentation.listeners.OnListItemEventListenerParams;
import com.base.presentation.listeners.OnListItemEventListener;
import com.base.presentation.views.adapters.AbstractViewHolder;

import butterknife.BindView;

/**
 * Created by Wafaa on 12/26/2016.
 */

class ReceiptViewHolder extends AbstractViewHolder<PickupService> {

    @BindView(R.id.receipt_list_item_service_type)
    TextView receiptListItemServiceType;
    @BindView(R.id.receipt_list_item_weight)
    TextView receiptListItemWeight;
    @BindView(R.id.receipt_list_item_price)
    TextView receiptListItemPrice;

    private OnListItemEventListener onListItemEventListener;
    private OnListItemEventListenerParams params;

    ReceiptViewHolder(View itemView) {
        super(itemView);
        params = new OnListItemEventListenerParams();
        onListItemEventListener = new OnListItemEventListener(this);
        itemView.setOnClickListener(onListItemEventListener);
    }

    @Override
    protected void draw(PickupService item) {
        params.setEntity(item);
        params.setPosition(getAdapterPosition());
        onListItemEventListener.setParams(params);
        receiptListItemServiceType.setText(item.getType()
        + "\n" + item.getLocation());
        receiptListItemPrice.setText(String.valueOf(item.getPrice()));
        setWeightValue(item);
    }

    private void setWeightValue(PickupService item) {
        String weight = item.getQuantity();
        receiptListItemWeight.setText(weight);
    }

    @Override
    public void onUpdate(Event event) throws RuntimeException {

    }
}
