package com.appzoneltd.lastmile.customer.features.pickup.recipient;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.abstracts.LastMileViewBinder;
import com.appzoneltd.lastmile.customer.features.pickup.models.PickupModel;
import com.appzoneltd.lastmile.customer.subfeatures.refresh.SwipeRefreshPresenter;
import com.appzoneltd.lastmile.customer.subfeatures.refresh.SwipeRefreshViewModel;
import com.base.presentation.base.abstracts.features.Feature;
import com.base.presentation.base.presentation.ValidatorViewModel;

import butterknife.BindView;

/**
 * Bind view of recipient feature
 * <p>
 * Created by Wafaa on 9/24/2016.
 */
class RecipientDetailsViewBinder extends LastMileViewBinder<PickupModel> {

    @BindView(R.id.shipping_service)
    Spinner shippingService;
    @BindView(R.id.shipping_service_msg)
    TextView shippingErrorMsg;
    @BindView(R.id.shipment_service_type)
    Spinner shippingServiceType;
    @BindView(R.id.shipping_service_type_msg)
    TextView shippingTypeErrorMsg;
    @BindView(R.id.recip_name)
    EditText recipientName;
    @BindView(R.id.recipient_name_msg)
    TextView nameErrorMsg;
    @BindView(R.id.recipient_phone_number)
    EditText recipientPhoneNumber;
    @BindView(R.id.recipient_phone_msg)
    TextView phoneErrorMsg;
    @BindView(R.id.country)
    Spinner country;
    @BindView(R.id.country_msg)
    TextView countryErrorMsg;
    @BindView(R.id.city)
    Spinner city;
    @BindView(R.id.city_msg)
    TextView cityErrorMsg;
    @BindView(R.id.recipient_full_address)
    EditText recipientFullAddress;
    @BindView(R.id.recipient_address_msg)
    TextView addressErrorMsg;
    @BindView(R.id.recipient_notes)
    EditText recipientNotes;
    @BindView(R.id.pickup_next_review)
    Button pickupNextReview;
    @BindView(R.id.recipient_layout)
    ViewGroup recipientLayout;
    @BindView(R.id.recipient_swipe_layout)
    SwipeRefreshLayout recipientSwipeLayout;


    RecipientDetailsViewBinder(Feature<PickupModel> feature) {
        super(feature);
    }

    @Override
    public void initializeAfterBindingViews(Bundle savedInstanceState) {
        RecipientDetailsViewModel viewModel = new RecipientDetailsViewModel(this);
        viewModel.addView(recipientLayout);
        viewModel.addView(shippingService);
        viewModel.addView(shippingErrorMsg);
        viewModel.addView(shippingServiceType);
        viewModel.addView(shippingTypeErrorMsg);
        viewModel.addView(country);
        viewModel.addView(countryErrorMsg);
        viewModel.addView(city);
        viewModel.addView(cityErrorMsg);
        viewModel.addView(recipientName, nameErrorMsg, ValidatorViewModel.Visibility.INVISIBLE);
        viewModel.addView(recipientPhoneNumber, phoneErrorMsg, ValidatorViewModel.Visibility.INVISIBLE);
        viewModel.addView(recipientFullAddress, addressErrorMsg, ValidatorViewModel.Visibility.INVISIBLE);
        viewModel.addView(recipientNotes);
        viewModel.addView(pickupNextReview);
        addEventsSubscriber(new RecipientDetailsPresenter(viewModel));
        SwipeRefreshViewModel multiSwipeRefreshViewModel = new SwipeRefreshViewModel(this) {
            @Override
            public long getSwipeRefreshViewId() {
                return R.id.recipient_swipe_layout;
            }
        };
        multiSwipeRefreshViewModel.addView(recipientSwipeLayout);
        addEventsSubscriber(new SwipeRefreshPresenter<PickupModel>(multiSwipeRefreshViewModel));
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_recipient_detials;
    }

}
