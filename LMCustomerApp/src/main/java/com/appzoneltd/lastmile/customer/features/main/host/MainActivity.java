package com.appzoneltd.lastmile.customer.features.main.host;

import android.os.Bundle;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.abstracts.LastMileActivity;
import com.appzoneltd.lastmile.customer.features.main.models.MainModel;
import com.base.presentation.annotations.interfaces.Menu;
import com.base.presentation.base.abstracts.features.MultipleClicksHandler;
import com.base.presentation.base.abstracts.features.ViewBinder;

/**
 * Created by Wafaa on 12/1/2016.
 */

@Menu(R.menu.pickup_menu)
public class MainActivity extends LastMileActivity<MainModel> {

    public MainActivity(){
        setMultipleClicksHandler(new MultipleClicksHandler(R.id.floating_btn_detect_my_location,
                R.id.floating_btn_pickup_menu));
    }

    @Override
    protected ViewBinder createLastMileViewBinder(Bundle savedInstanceState) {
        return new MainActivityViewBinder(this);
    }

}
