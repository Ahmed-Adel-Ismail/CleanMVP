package com.appzoneltd.lastmile.customer.features.main.menu;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.abstracts.LastMileActivity;
import com.base.presentation.base.abstracts.features.MultipleClicksHandler;
import com.base.presentation.base.abstracts.features.ViewBinder;
import com.base.presentation.models.NullModel;

public class PickupFloatingMenuActivity extends LastMileActivity<NullModel> {

    public PickupFloatingMenuActivity() {
        setMultipleClicksHandler(new MultipleClicksHandler(R.id.floating_btn_pickup_now,
                R.id.floating_btn_pickup_scheduled));
    }

    @Override
    public ViewBinder createLastMileViewBinder(Bundle savedInstanceState) {
        return new PickupFloatingMenuViewBinder(this);
    }

    @NonNull
    @Override
    protected NullModel createModel() {
        NullModel model = new NullModel();
        model.initialize(this);
        return model;
    }

}
