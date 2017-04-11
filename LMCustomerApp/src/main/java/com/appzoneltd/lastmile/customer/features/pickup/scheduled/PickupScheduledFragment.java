package com.appzoneltd.lastmile.customer.features.pickup.scheduled;

import android.os.Bundle;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.features.pickup.models.PickupModel;
import com.base.abstraction.annotations.interfaces.Address;
import com.base.presentation.base.abstracts.features.AbstractFragment;
import com.base.presentation.base.abstracts.features.ViewBinder;

/**
 * Created by Ahmed Adel on 9/21/2016.
 */
@Address(R.id.addressPickupScheduleFragment)
public class PickupScheduledFragment extends AbstractFragment<PickupModel> {

    @Override
    public ViewBinder createViewBinder(Bundle savedInstanceState) {
        return new PickupScheduledViewBinder(this);
    }

}
