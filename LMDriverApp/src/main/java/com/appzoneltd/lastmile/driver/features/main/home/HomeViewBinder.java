package com.appzoneltd.lastmile.driver.features.main.home;

import android.view.View;

import com.appzoneltd.lastmile.driver.R;
import com.base.presentation.annotations.interfaces.BindLayout;
import com.base.presentation.annotations.interfaces.Presenter;
import com.base.presentation.base.abstracts.features.ViewBinder;

import butterknife.BindView;

/**
 * the {@link ViewBinder} for the home screen
 * <p>
 * Created by Ahmed Adel on 12/1/2016.
 */
@BindLayout(R.layout.screen_home)
public class HomeViewBinder extends ViewBinder {


    @BindView(R.id.screen_host_container_id)
    View containerLayout;
    @BindView(R.id.screen_home_map_view)
    View mapView;
    @BindView(R.id.screen_home_main_progress_bar)
    View mainProgressBar;
    @BindView(R.id.screen_home_floating_action_button)
    View floatingActionButton;
    @Presenter
    HomePresenter presenter;

}
