package com.appzoneltd.lastmile.customer.features.pickup.review;

import android.os.Bundle;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.features.pickup.models.PickupModel;
import com.base.abstraction.annotations.interfaces.Address;
import com.base.presentation.base.abstracts.features.AbstractFragment;
import com.base.presentation.base.abstracts.features.ViewBinder;

/**
 * the Fragment that shows the Pickup CreatePickupRequestParams "Review" Screen
 * <p/>
 * Created by Wafaa on 9/24/2016.
 */
@Address(R.id.addressPickupReviewFragment)
public class PickupReviewFragment extends AbstractFragment<PickupModel> {


    @Override
    public ViewBinder createViewBinder(Bundle savedInstanceState) {
        return new PickupReviewViewBinder(this);
    }

}
