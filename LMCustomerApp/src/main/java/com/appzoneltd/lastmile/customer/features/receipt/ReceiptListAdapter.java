package com.appzoneltd.lastmile.customer.features.receipt;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appzoneltd.lastmile.customer.R;
import com.entities.cached.PickupService;
import com.base.presentation.views.adapters.AbstractAdapter;

/**
 * Created by Wafaa on 12/26/2016.
 */

public class ReceiptListAdapter extends AbstractAdapter<PickupService, ReceiptViewHolder> {

    @Override
    protected ReceiptViewHolder createAbstractViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_receipt_list_item, parent, false);
        return new ReceiptViewHolder(view);
    }
}
