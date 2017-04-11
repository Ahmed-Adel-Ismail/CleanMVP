package com.appzoneltd.lastmile.customer.features.tracking.host;

import android.os.Bundle;

import com.appzoneltd.lastmile.customer.abstracts.LastMileActivity;
import com.appzoneltd.lastmile.customer.features.tracking.model.TrackingModel;
import com.appzoneltd.lastmile.customer.interfaces.ActivityHiddenKeyboard;
import com.base.presentation.base.abstracts.features.ViewBinder;

public class TrackingActivity extends LastMileActivity<TrackingModel> implements
        ActivityHiddenKeyboard {

    @Override
    protected ViewBinder createLastMileViewBinder(Bundle savedInstanceState) {
        return new TrackingActivityViewBinder(this);
    }

}
