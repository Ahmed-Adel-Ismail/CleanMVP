package com.appzoneltd.lastmile.customer.features.pickup.review;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.abstracts.LastMileViewBinder;
import com.appzoneltd.lastmile.customer.features.pickup.models.PickupModel;
import com.base.presentation.base.abstracts.features.Feature;
import com.base.presentation.base.abstracts.features.ViewBinder;

import butterknife.BindView;

/**
 * A {@link ViewBinder} to handle the Pickup Review Screen
 * <p/>
 * Created by Ahmed Adel on 9/27/2016.
 */
class PickupReviewViewBinder extends LastMileViewBinder<PickupModel> {


    @BindView(R.id.pickup_review_top_left_img)
    ImageView topLeftImageView;
    @BindView(R.id.pickup_review_top_right_img)
    ImageView topRightImageView;
    @BindView(R.id.pickup_review_payment_at_txt)
    TextView paymentAtTextView;
    @BindView(R.id.pickup_review_estimate_cost_txt)
    TextView estimateCostTextView;
    @BindView(R.id.pickup_review_shipment_type_txt)
    TextView shipmentTypeTextView;
    @BindView(R.id.pickup_review_pickup_time_txt)
    TextView pickupTimeTextView;
    @BindView(R.id.pickup_review_address_txt)
    TextView addressTextView;
    @BindView(R.id.pickup_review_submit_request_btn)
    Button submitRequestButton;
    @BindView(R.id.pickup_review_nickname_txt)
    TextView nicknameTextView;
    @BindView(R.id.pickup_review_package_type_txt)
    TextView packageTypeTextView;
    @BindView(R.id.pickup_review_package_weight_txt)
    TextView packageWeightTextView;
    @BindView(R.id.pickup_review_whats_inside_txt)
    TextView whatsInsideTextView;
    @BindView(R.id.pickup_review_additional_service_txt)
    TextView additionalServiceTextView;
    @BindView(R.id.pickup_review_recipient_name_txt)
    TextView recipientNameTextView;
    @BindView(R.id.pickup_review_recioient_phone_no_txt)
    TextView recipientPhoneNumberTextView;
    @BindView(R.id.pickup_review_recipient_address_txt)
    TextView recipientAddressTextView;
    @BindView(R.id.pickup_review_recipient_notes_txt)
    TextView recipientNotesTextView;
    @BindView(R.id.nickname_layout)
    ViewGroup nickNameLayout;
    @BindView(R.id.package_brief_layout)
    ViewGroup packageBriefLayout;
    @BindView(R.id.additional_services_layout)
    ViewGroup additionalServicesLayout;
    @BindView(R.id.notes_layout)
    ViewGroup notesLayout;
    @BindView(R.id.pickup_review_images_layout)
    ViewGroup imagesLayout;
    @BindView(R.id.review_layout)
    ViewGroup reviewLayout;
    @BindView(R.id.pickup_review_progress)
    ProgressBar pickupReviewProgress;


    public PickupReviewViewBinder(Feature<PickupModel> feature) {
        super(feature);
    }

    @Override
    public void initializeAfterBindingViews(Bundle savedInstanceState) {
        PickupReviewViewModel viewModel = new PickupReviewViewModel(this);
        viewModel.addView(reviewLayout);
        viewModel.addView(topLeftImageView);
        viewModel.addView(topRightImageView);
        viewModel.addView(paymentAtTextView);
        viewModel.addView(estimateCostTextView);
        viewModel.addView(shipmentTypeTextView);
        viewModel.addView(pickupTimeTextView);
        viewModel.addView(addressTextView);
        viewModel.addView(submitRequestButton);
        viewModel.addView(nicknameTextView);
        viewModel.addView(packageTypeTextView);
        viewModel.addView(packageWeightTextView);
        viewModel.addView(whatsInsideTextView);
        viewModel.addView(additionalServiceTextView);
        viewModel.addView(recipientNameTextView);
        viewModel.addView(recipientPhoneNumberTextView);
        viewModel.addView(recipientAddressTextView);
        viewModel.addView(recipientNotesTextView);
        viewModel.addView(nickNameLayout);
        viewModel.addView(packageBriefLayout);
        viewModel.addView(additionalServicesLayout);
        viewModel.addView(notesLayout);
        viewModel.addView(imagesLayout);
        viewModel.addView(pickupReviewProgress);
        addEventsSubscriber(new PickupReviewPresenter(viewModel));
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_pickup_review;
    }


}
