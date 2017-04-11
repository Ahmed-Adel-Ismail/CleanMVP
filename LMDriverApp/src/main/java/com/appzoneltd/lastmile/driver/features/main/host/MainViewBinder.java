package com.appzoneltd.lastmile.driver.features.main.host;

import android.os.Bundle;
import android.view.View;

import com.appzoneltd.lastmile.driver.R;
import com.base.presentation.annotations.interfaces.BindLayout;
import com.base.presentation.annotations.interfaces.Presenter;
import com.base.presentation.base.abstracts.features.ViewBinder;
import com.base.presentation.models.Model;
import com.base.presentation.receivers.GPSStateReceiver;

import butterknife.BindView;

/**
 * the {@link Model} for the
 * <p>
 * Created by Ahmed Adel on 11/21/2016.
 */
@BindLayout(R.layout.screen_main)
public class MainViewBinder extends ViewBinder {

    @Presenter
    MainPresenter presenter;
    @BindView(R.id.screen_host_container_id)
    View screen_host_container_id;
    @BindView(R.id.screen_main_requests_progress_bar)
    View screen_main_requests_progress_bar;


    @Override
    public void initializeAfterBindingViews(Bundle savedInstanceState) {
        addEventsSubscriber(new GPSStateReceiver(this.getFeature(), true));
    }

}
