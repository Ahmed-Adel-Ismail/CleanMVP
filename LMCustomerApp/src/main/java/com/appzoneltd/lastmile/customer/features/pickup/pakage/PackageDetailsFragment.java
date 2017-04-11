package com.appzoneltd.lastmile.customer.features.pickup.pakage;

import android.os.Bundle;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.features.pickup.models.PickupModel;
import com.base.abstraction.annotations.interfaces.Address;
import com.base.presentation.base.abstracts.features.AbstractFragment;
import com.base.presentation.base.abstracts.features.ViewBinder;

/**
 * Created by Wafaa on 8/4/2016.
 */
@Address(R.id.addressPackageDetailsFragment)
public class PackageDetailsFragment extends AbstractFragment<PickupModel> {

    @Override
    public ViewBinder createViewBinder(Bundle savedInstanceState) {
        return new PackageDetailsViewBinder(this);
    }
}
