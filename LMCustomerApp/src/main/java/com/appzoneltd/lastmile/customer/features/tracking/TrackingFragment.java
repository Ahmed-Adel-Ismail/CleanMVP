package com.appzoneltd.lastmile.customer.features.tracking;

import android.os.Bundle;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.features.tracking.model.TrackingModel;
import com.base.abstraction.annotations.interfaces.Address;
import com.base.presentation.base.abstracts.features.AbstractFragment;
import com.base.presentation.base.abstracts.features.ViewBinder;

/**
 * Created by Wafaa on 11/13/2016.
 */
@Address(R.id.addressTrackingFragment)
public class TrackingFragment extends AbstractFragment<TrackingModel> {


    public ViewBinder createViewBinder(Bundle savedInstanceState) {
        return new TrackingFragmentViewBinder(this);
    }
}
