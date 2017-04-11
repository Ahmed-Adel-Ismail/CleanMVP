package com.appzoneltd.lastmile.customer.features.pickup.host;

import android.os.Bundle;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.abstracts.LastMileActivity;
import com.appzoneltd.lastmile.customer.annotations.MenuGroup;
import com.appzoneltd.lastmile.customer.features.pickup.models.PickupModel;
import com.appzoneltd.lastmile.customer.interfaces.ActivityHiddenKeyboard;
import com.base.presentation.annotations.interfaces.Menu;
import com.base.presentation.base.abstracts.features.MultipleClicksHandler;
import com.base.presentation.base.abstracts.features.ViewBinder;

@MenuGroup
@Menu(R.menu.pickup_menu)
public class PickupActivity extends LastMileActivity<PickupModel>
        implements ActivityHiddenKeyboard {

    public PickupActivity() {
        setMultipleClicksHandler(new MultipleClicksHandler(R.id.first_capture_layout,
                R.id.second_capture_layout));
    }


    @Override
    protected ViewBinder createLastMileViewBinder(Bundle savedInstanceState) {
        return new PickupViewBinder(this);
    }


}
