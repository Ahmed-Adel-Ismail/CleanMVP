package com.appzoneltd.lastmile.customer.features.pickup.pakage;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.SeekBar;
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
 * Created by Wafaa on 9/22/2016.
 */
class PackageDetailsViewBinder extends LastMileViewBinder<PickupModel> {

    @BindView(R.id.document)
    RadioButton document;
    @BindView(R.id.box)
    RadioButton box;
    @BindView(R.id.package_details_seekbar)
    SeekBar seekBar;
    @BindView(R.id.package_details_box_weight_value)
    TextView boxWeightValue;
    @BindView(R.id.smallest_weight)
    TextView smallestWeight;
    @BindView(R.id.biggest_weight)
    TextView biggestWeight;
    @BindView(R.id.shipment_nickname)
    EditText shipmentNickname;
    @BindView(R.id.package_brief)
    EditText packageBrief;
    @BindView(R.id.first_capture_layout)
    FrameLayout firstCaptureLayout;
    @BindView(R.id.package_first_photo)
    ImageView firstPhoto;
    @BindView(R.id.remove_first_photo)
    ImageView removeFirstPhoto;
    @BindView(R.id.second_capture_layout)
    FrameLayout secondCaptureLayout;
    @BindView(R.id.package_second_photo)
    ImageView secondPhoto;
    @BindView(R.id.remove_second_photo)
    ImageView removeSecondPhoto;
    @BindView(R.id.rb_pickup)
    RadioButton payAtPickup;
    @BindView(R.id.rb_delivery)
    RadioButton payAtDelivery;
    @BindView(R.id.packaging_box)
    CheckBox packagingBox;
    @BindView(R.id.wrap_label_package)
    CheckBox wrapAndLabelPackage;
    @BindView(R.id.custom_label)
    EditText customLabel;
    @BindView(R.id.custom_label_error_msg)
    TextView customLabelErrorMsg;
    @BindView(R.id.next_recipient_details)
    Button nextRecipientDetails;
    @BindView(R.id.package_brief_error_msg)
    TextView BriefErrorMsg;
    @BindView(R.id.image_error_msg)
    TextView imageErrorMessage;
    @BindView(R.id.package_details_swipe_layout)
    SwipeRefreshLayout packageDetailsSwipeLayout;
    @BindView(R.id.package_details_layout)
    ViewGroup packageDetailsLayout;


    PackageDetailsViewBinder(Feature<PickupModel> feature) {
        super(feature);
    }

    @Override
    public void initializeAfterBindingViews(Bundle savedInstanceState) {
        PackageDetailsViewModel viewModel = new PackageDetailsViewModel(this);
        viewModel.addView(document);
        viewModel.addView(box);
        viewModel.addView(seekBar);
        viewModel.addView(boxWeightValue);
        viewModel.addView(smallestWeight);
        viewModel.addView(biggestWeight);
        viewModel.addView(packageBrief, BriefErrorMsg, ValidatorViewModel.Visibility.GONE);
        viewModel.addView(shipmentNickname);
        viewModel.addView(firstCaptureLayout);
        viewModel.addView(firstPhoto, imageErrorMessage, ValidatorViewModel.Visibility.GONE);
        viewModel.addView(removeFirstPhoto);
        viewModel.addView(secondCaptureLayout);
        viewModel.addView(secondPhoto);
        viewModel.addView(removeSecondPhoto);
        viewModel.addView(payAtPickup);
        viewModel.addView(payAtDelivery);
        viewModel.addView(packagingBox);
        viewModel.addView(wrapAndLabelPackage);
        viewModel.addView(nextRecipientDetails);
        viewModel.addView(packageDetailsSwipeLayout);
        viewModel.addView(packageDetailsLayout);
        viewModel.addView(customLabel, customLabelErrorMsg, ValidatorViewModel.Visibility.INVISIBLE);
        addEventsSubscriber(new PackageDetailsPresenter(viewModel));
        SwipeRefreshViewModel multiSwipeRefreshViewModel = new SwipeRefreshViewModel(this) {
            @Override
            public long getSwipeRefreshViewId() {
                return R.id.package_details_swipe_layout;
            }
        };
        multiSwipeRefreshViewModel.addView(packageDetailsSwipeLayout);
        addEventsSubscriber(new SwipeRefreshPresenter<>(multiSwipeRefreshViewModel));
    }


    @Override
    public int getContentView() {
        return R.layout.fragment_package_detials;
    }

    
}
