package com.appzoneltd.lastmile.customer.features.main.packageslist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appzoneltd.lastmile.customer.R;
import com.entities.cached.PickupStatus;
import com.base.presentation.views.adapters.AbstractAdapter;

/**
 * Created by Wafaa on 12/15/2016.
 */

public class PackageListAdapter extends AbstractAdapter<PickupStatus, PackageViewHolder> {


    @Override
    protected PackageViewHolder createAbstractViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_package_list_item, parent, false);
        PackageViewHolder packageViewHolder = new PackageViewHolder(view);
        return packageViewHolder;
    }



}
