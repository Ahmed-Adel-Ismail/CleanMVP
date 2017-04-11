package com.appzoneltd.lastmile.customer.features.tracking;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.abstracts.LastMileViewBinder;
import com.appzoneltd.lastmile.customer.features.tracking.driverdetails.DriverDetailsPresenter;
import com.appzoneltd.lastmile.customer.features.tracking.driverdetails.DriverDetailsViewModel;
import com.appzoneltd.lastmile.customer.features.tracking.model.TrackingModel;
import com.appzoneltd.lastmile.customer.subfeatures.RingProgressBar;
import com.base.presentation.annotations.interfaces.BindLayout;
import com.base.presentation.base.abstracts.features.Feature;
import com.google.android.gms.maps.MapView;

import butterknife.BindView;

/**
 * Created by Wafaa on 11/13/2016.
 */
@BindLayout(R.layout.fragment_tracking)
public class TrackingFragmentViewBinder extends LastMileViewBinder<TrackingModel> {

    @BindView(R.id.driver_map_view)
    MapView driverMapView;
    @BindView(R.id.driver_layout)
    ViewGroup driverLayout;
    @BindView(R.id.time_wheel)
    RingProgressBar timeWheel;
    @BindView(R.id.driver_rating)
    RatingBar driverRating;
    @BindView(R.id.driver_name)
    TextView DriverName;
    @BindView(R.id.vehicle_details)
    TextView vehicleDetails;
    @BindView(R.id.driver_details_image)
    ImageView driverDetailsImage;
    @BindView(R.id.fragment_tracking_loading)
    ProgressBar fragmentTrackingLoading;

    public TrackingFragmentViewBinder(Feature<TrackingModel> feature) {
        super(feature);
    }

    @Override
    public void initializeAfterBindingViews(Bundle savedInstanceState) {
        TrackingFragmentViewModel viewModel = new TrackingFragmentViewModel(this);
        viewModel.addView(driverMapView);
        viewModel.addView(driverLayout);
        viewModel.addView(fragmentTrackingLoading);
        addEventsSubscriber(new TrackingFragmentPresenter(viewModel));
        DriverDetailsViewModel driverDetailsViewModel = new DriverDetailsViewModel(this);
        driverDetailsViewModel.addView(timeWheel);
        driverDetailsViewModel.addView(driverRating);
        viewModel.addView(DriverName);
        viewModel.addView(vehicleDetails);
        viewModel.addView(driverDetailsImage);
        addEventsSubscriber(new DriverDetailsPresenter(driverDetailsViewModel));
    }

}
