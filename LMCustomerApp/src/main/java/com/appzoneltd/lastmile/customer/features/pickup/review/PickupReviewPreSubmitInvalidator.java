package com.appzoneltd.lastmile.customer.features.pickup.review;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.deprecated.utilities.BitmapUtils;
import com.base.abstraction.commands.Command;

/**
 * a {@link PickupReviewInvalidator} that invalidates Views before the submit button
 * (Submit button is invalidated in this class as well)
 * <p/>
 * Created by Ahmed Adel on 9/27/2016.
 *
 * @see PickupReviewInvalidator
 * @see PickupReviewPostSubmitInvalidator
 */
class PickupReviewPreSubmitInvalidator extends PickupReviewInvalidator {

    public PickupReviewPreSubmitInvalidator(PickupReviewViewModel viewModel) {
        super(viewModel);
        Command<View, Void> command = createOnTopLeftImageInvalidateCommand();
        put((long) R.id.pickup_review_top_left_img, command);
        command = createOnTopRightImageInvalidateCommand();
        put((long) R.id.pickup_review_top_right_img, command);
        command = createOnPaymentAtInvalidateCommand();
        put((long) R.id.pickup_review_payment_at_txt, command);
        command = createOnEstimateCostInvalidateCommand();
        put((long) R.id.pickup_review_estimate_cost_txt, command);
        command = createOnShipmentTypeInvalidateCommand();
        put((long) R.id.pickup_review_shipment_type_txt, command);
        command = createOnPickupTimeInvalidateCommand();
        put((long) R.id.pickup_review_pickup_time_txt, command);
        command = createOnAddressInvalidateCommand();
        put((long) R.id.pickup_review_address_txt, command);
        command = createOnSubmitRequestInvalidateCommand();
        put((long) R.id.pickup_review_submit_request_btn, command);
        command = createOnProgressInvalidateCommand();
        put((long) R.id.pickup_review_progress, command);
    }

    private Command<View, Void> createOnTopLeftImageInvalidateCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View p) {
                Bitmap bmp = getViewModel().getTopLeftBitmap();
                updateImageView(bmp, p);
                return null;
            }
        };
    }


    private Command<View, Void> createOnTopRightImageInvalidateCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                Bitmap bmp = getViewModel().getTopRightBitmap();
                updateImageView(bmp, view);
                return null;
            }
        };
    }

    private void updateImageView(Bitmap bitmap, View view) {
        ImageView imageView = (ImageView) view;
        if (bitmap != null) {
            bitmap = BitmapUtils.resizeImageForImageView(bitmap);
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setVisibility(View.INVISIBLE);
        }
    }

    private Command<View, Void> createOnPaymentAtInvalidateCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View p) {
                updateTextView(p, getViewModel().getPaymentAt());
                return null;
            }
        };
    }


    private Command<View, Void> createOnEstimateCostInvalidateCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View p) {
                updateTextView(p, getViewModel().getEstimateCost());
                return null;
            }
        };
    }

    private Command<View, Void> createOnShipmentTypeInvalidateCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View p) {
                updateTextView(p, getViewModel().getShipmentType());
                return null;
            }
        };
    }

    private Command<View, Void> createOnPickupTimeInvalidateCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View p) {
                updateTextView(p, getViewModel().getPickupTime());
                return null;
            }
        };
    }

    private Command<View, Void> createOnAddressInvalidateCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View p) {
                updateTextView(p, getViewModel().getPickupDisplayedAddress());
                return null;
            }
        };
    }

    private Command<View, Void> createOnSubmitRequestInvalidateCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View p) {
                Button submit = (Button) p;
                submit.setOnClickListener(getViewModel().getOnEventListener());
                if (getViewModel().isRequesting()) {
                    submit.setText("");
                } else {
                    submit.setText(R.string.submit_label);
                }
                return null;
            }
        };
    }

    private Command<View, Void> createOnProgressInvalidateCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View p) {
                ProgressBar progress = (ProgressBar) p;
                if (getViewModel().isRequesting()) {
                    progress.setVisibility(View.VISIBLE);
                } else {
                    progress.setVisibility(View.GONE);
                }
                return null;
            }
        };
    }

}
