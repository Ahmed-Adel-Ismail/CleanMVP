package com.appzoneltd.lastmile.customer.features.main.home;

import android.os.Bundle;

import com.appzoneltd.lastmile.customer.R;
import com.base.abstraction.annotations.interfaces.Address;
import com.base.presentation.base.abstracts.features.AbstractFragment;
import com.base.presentation.base.abstracts.features.ViewBinder;
import com.base.presentation.models.NullModel;

/**
 * Created by Wafaa on 12/1/2016.
 */
@Address(R.id.addressHomeFragment)
public class HomeFragment extends AbstractFragment<NullModel> {

    @Override
    public ViewBinder createViewBinder(Bundle savedInstanceState) {
        return new HomeFragmentViewBinder(this);
    }
}
