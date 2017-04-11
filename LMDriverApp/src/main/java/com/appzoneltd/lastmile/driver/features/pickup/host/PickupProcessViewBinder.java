package com.appzoneltd.lastmile.driver.features.pickup.host;

import android.view.View;

import com.appzoneltd.lastmile.driver.Flow.PickupProcessActivity;
import com.appzoneltd.lastmile.driver.R;
import com.base.presentation.annotations.interfaces.BindLayout;
import com.base.presentation.annotations.interfaces.Presenter;
import com.base.presentation.base.abstracts.features.ViewBinder;

import butterknife.BindView;

/**
 * the {@link ViewBinder} of the {@link PickupProcessActivity}
 * <p>
 * Created by Ahmed Adel on 12/24/2016.
 */
@BindLayout(R.layout.screen_pickup_process)
public class PickupProcessViewBinder extends ViewBinder {

    @Presenter
    PickupProcessPresenter pickupProcessPresenter;
    @BindView(R.id.screen_host_container_id)
    View screen_host_container_id;
    @BindView(R.id.screen_pickup_process_main_scroll_view)
    View screen_pickup_process_main_scroll_view;
    @BindView(R.id.screen_pickup_process_progress_bars_frame_layout)
    View screen_pickup_process_progress_bars_frame_layout;
    @BindView(R.id.screen_pickup_process_progress_bar_top_right)
    View screen_pickup_process_progress_bar_top_right;
    @BindView(R.id.screen_pickup_process_progress_bar_top_left)
    View screen_pickup_process_progress_bar_top_left;
    @BindView(R.id.screen_pickup_process_progress_bar_bottom_right)
    View screen_pickup_process_progress_bar_bottom_right;
    @BindView(R.id.screen_pickup_process_progress_bar_bottom_left)
    View screen_pickup_process_progress_bar_bottom_left;
    @BindView(R.id.screen_pickup_process_progress_bar_center)
    View screen_pickup_process_progress_bar_center;
    @BindView(R.id.screen_pickup_process_progress_failed_relative_layout)
    View screen_pickup_process_progress_failed_relative_layout;
    @BindView(R.id.screen_pickup_process_verify_package_fragment_layout)
    View screen_pickup_process_verify_package_fragment_layout;
    @BindView(R.id.screen_pickup_process_payment_fragment_layout)
    View screen_pickup_process_payment_fragment_layout;
    @BindView(R.id.screen_pickup_process_documents_fragment_layout)
    View screen_pickup_process_documents_fragment_layout;


}
