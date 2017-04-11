package com.appzoneltd.lastmile.driver.features.pickup.verification;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatRadioButton;
import android.text.Editable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.appzoneltd.lastmile.driver.R;
import com.appzoneltd.lastmile.driver.subfeatures.ServerImageDrawer;
import com.base.abstraction.annotations.interfaces.Executable;
import com.base.abstraction.annotations.interfaces.Initializer;
import com.base.abstraction.logs.Logger;
import com.base.presentation.references.BooleanProperty;
import com.base.abstraction.system.AppResources;
import com.base.cached.ServerImage;
import com.base.presentation.annotations.interfaces.EditTextWatcher;
import com.base.presentation.annotations.interfaces.OnResponse;
import com.base.presentation.annotations.interfaces.Sync;
import com.base.presentation.base.abstracts.features.AbstractActivity;
import com.base.presentation.base.presentation.WatcherViewModel;
import com.base.presentation.listeners.OnEventListener;
import com.base.presentation.references.Entity;
import com.entities.cached.pakage.PackageDetails;
import com.entities.cached.pakage.PackageLabel;
import com.entities.cached.pakage.PackageLabelsGroup;
import com.entities.cached.pakage.PackageType;
import com.entities.cached.pakage.PackageTypesGroup;
import com.entities.cached.payment.PaymentMethod;
import com.entities.cached.payment.PaymentType;

import java.text.DecimalFormat;

class VerificationViewModel extends WatcherViewModel {

    static final int IMAGE_INDEX_LEFT = 0;
    static final int IMAGE_INDEX_RIGHT = 1;

    @Sync("packageDetails")
    final Entity<PackageDetails> packageDetails = new Entity<>();
    @Sync("packageTypes")
    final Entity<PackageTypesGroup> packageTypes = new Entity<>();
    @Sync("packageLabels")
    final Entity<PackageLabelsGroup> packageLabels = new Entity<>();
    final BooleanProperty submitting = new BooleanProperty();

    private boolean invalidWeight;
    private boolean invalidWhatsInside;


    // package nickname

    @OnResponse(R.id.requestPackageDetails)
    @Initializer(R.id.screen_pickup_process_verify_package_nick_name_details_text_view)
    void nickname(TextView view) {
        view.setText(packageDetails.get().getNickname());
    }

    // package types :

    @OnResponse(R.id.requestAllPackageTypes)
    @Initializer(R.id.screen_pickup_process_verify_package_type_details_spinner)
    void spinnerInitializer(Spinner spinner) {

        if (packageTypes.isEmpty()) {
            Logger.getInstance().error(getClass(), "null @ packageTypes");
            return;
        }

        AbstractActivity activity = getFeature().getHostActivity();
        OnEventListener eventListener = new OnEventListener(activity);
        spinner.setOnTouchListener(eventListener);
        spinner.setOnItemSelectedListener(eventListener);

        String[] itemsArray = packageTypes.get().createTypesArray();
        int itemViewResource = android.R.layout.simple_spinner_item;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, itemViewResource, itemsArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }

    @OnResponse({R.id.requestAllPackageTypes, R.id.requestPackageDetails})
    @Executable(R.id.screen_pickup_process_verify_package_type_details_spinner)
    void onInvalidateSpinner(Spinner spinner) {

        if (packageTypes.isEmpty()) {
            Logger.getInstance().error(getClass(), "null @ packageTypes");
            return;
        }

        PackageType p = packageDetails.get().getType();
        spinner.setSelection(packageTypes.get().getIndexFor(p));
        spinner.invalidate();
    }

    // package weight :

    @OnResponse(R.id.requestPackageDetails)
    @Initializer(R.id.screen_pickup_process_verify_package_wight_text_input_edit_text)
    void packageWeightInitializer(TextInputEditText view) {
        view.setText(new DecimalFormat("#0.00").format(packageDetails.get().getWeight()));
    }

    @OnResponse(R.id.requestPackageDetails)
    @EditTextWatcher(R.id.screen_pickup_process_verify_package_wight_text_input_edit_text)
    void packageWeightWatcher(Editable editable) {
        double weight;
        try {
            weight = Double.valueOf(editable.toString());
        } catch (Throwable e) {
            weight = 0;
        }
        packageDetails.get().setWeight(weight);
        invalidWeight = weight == 0;
        invalidateView(R.id.screen_pickup_process_verify_package_wight_text_input_layout);
    }

    @Executable(R.id.screen_pickup_process_verify_package_wight_text_input_layout)
    void packageWeightLayout(TextInputLayout view) {
        if (invalidWeight) {
            view.setErrorEnabled(true);
            int stringResource = R.string.screen_pickup_process_verify_package_package_weight_error;
            view.setError(AppResources.string(stringResource));
        } else {
            view.setError(null);
            view.setErrorEnabled(false);
        }
    }

    // what's inside

    @OnResponse(R.id.requestPackageDetails)
    @Initializer(R.id.screen_pickup_process_verify_package_whats_inside_text_input_edit_text)
    void whatsInsideInitializer(TextInputEditText view) {
        view.setText(packageDetails.get().getDescription());
    }

    @OnResponse(R.id.requestPackageDetails)
    @EditTextWatcher(R.id.screen_pickup_process_verify_package_whats_inside_text_input_edit_text)
    void whatsInsideWatcher(Editable editable) {
        packageDetails.get().setDescription(editable.toString());
        invalidWhatsInside = TextUtils.isEmpty(editable);
        invalidateView(R.id.screen_pickup_process_verify_package_whats_inside_text_input_layout);
    }


    @Executable(R.id.screen_pickup_process_verify_package_whats_inside_text_input_layout)
    void whatsInsideLayout(TextInputLayout view) {
        if (invalidWhatsInside) {
            view.setErrorEnabled(true);
            int stringResource = R.string.screen_pickup_process_verify_package_whats_inside_error;
            view.setError(AppResources.string(stringResource));
        } else {
            view.setError(null);
            view.setErrorEnabled(false);
        }
    }

    // additional services

    @OnResponse(R.id.requestPackageDetails)
    @Initializer(R.id.screen_pickup_process_verify_package_additional_services_details_check_box_one)
    void additionalServicesWrappingView(AppCompatCheckBox checkBox) {
        String wrappingLabel = packageDetails.get().getWrappingLabel();
        boolean checked = !TextUtils.isEmpty(wrappingLabel);
        if (checked) {
            wrappingLabel = " \"" + wrappingLabel + "\"";
        } else {
            wrappingLabel = "";
        }
        int stringRes = R.string.screen_pickup_process_verify_package_additional_service_wrapping;
        wrappingLabel = AppResources.string(stringRes) + wrappingLabel;

        checkBox.setChecked(checked);
        checkBox.setText(wrappingLabel);
        checkBox.setVisibility(View.VISIBLE);
        checkBox.setOnCheckedChangeListener(new OnEventListener(this));

    }

    @OnResponse(R.id.requestPackageDetails)
    @Initializer(R.id.screen_pickup_process_verify_package_additional_services_details_check_box_two)
    void additionalServicesBoxingView(AppCompatCheckBox checkBox) {
        boolean checked = !TextUtils.isEmpty(packageDetails.get().getBoxing());
        checkBox.setChecked(checked);
        checkBox.setText(R.string.screen_pickup_process_verify_package_additional_service_boxing);
        checkBox.setVisibility(View.VISIBLE);
        checkBox.setOnCheckedChangeListener(new OnEventListener(this));
    }

    // package labels

    @OnResponse(R.id.requestAllPackageLabels)
    @Initializer(R.id.screen_pickup_process_verify_package_labels_relative_layout)
    void packageLabelsOuterLayout(View view) {
        view.setVisibility(!packageLabels.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @OnResponse({R.id.requestAllPackageLabels, R.id.requestPackageDetails})
    @Initializer(R.id.screen_pickup_process_verify_package_labels_details_linear_layout)
    void packageLabelsLayoutInitializer(LinearLayout linearLayout) {
        if (packageLabels.isEmpty()) {
            return;
        }
        int labelsCount = packageLabels.get().size();
        if (labelsCount > linearLayout.getChildCount()) {
            labelsCount = linearLayout.getChildCount();
        }

        OnEventListener onEventListener = new OnEventListener(this);

        for (int i = 0; i < labelsCount; i++) {
            PackageLabel label = packageLabels.get().get(i);
            AppCompatCheckBox checkBox = (AppCompatCheckBox) linearLayout.getChildAt(i);
            checkBox.setVisibility(View.VISIBLE);
            checkBox.setTag(label);
            checkBox.setText(label.getLabel());
            checkBox.setChecked(packageDetails.get().containsPackageLabel(label));
            checkBox.setOnCheckedChangeListener(onEventListener);
        }
    }

    @OnResponse(R.id.requestPackageDetails)
    @Executable({
            R.id.screen_pickup_process_verify_package_labels_details_check_box_one,
            R.id.screen_pickup_process_verify_package_labels_details_check_box_two,
            R.id.screen_pickup_process_verify_package_labels_details_check_box_three,
            R.id.screen_pickup_process_verify_package_labels_details_check_box_four,
            R.id.screen_pickup_process_verify_package_labels_details_check_box_five,
            R.id.screen_pickup_process_verify_package_labels_details_check_box_six,
            R.id.screen_pickup_process_verify_package_labels_details_check_box_seven,
            R.id.screen_pickup_process_verify_package_labels_details_check_box_eight})
    void packageServicesCheckbox(AppCompatCheckBox checkBox) {
        Object label = checkBox.getTag();
        if (label != null) {
            checkBox.setChecked(packageDetails.get().containsPackageLabel((PackageLabel) label));
        }
    }

    // payment at

    @Initializer(R.id.screen_pickup_process_verify_package_payment_at_details_check_box_one)
    void paymentMethodPickupInitializer(AppCompatRadioButton view) {
        view.setOnCheckedChangeListener(new OnEventListener(this));
        view.setText(R.string.screen_pickup_process_verify_package_payment_at_pickup);
        view.setTag(PaymentType.PICKUP);
    }

    @Initializer(R.id.screen_pickup_process_verify_package_payment_at_details_check_box_two)
    void paymentMethodDeliveryInitializer(AppCompatRadioButton view) {
        view.setOnCheckedChangeListener(new OnEventListener(this));
        view.setText(R.string.screen_pickup_process_verify_package_payment_at_delivery);
        view.setTag(PaymentType.DELIVERY);
    }

    @OnResponse(R.id.requestPackageDetails)
    @Executable(R.id.screen_pickup_process_verify_package_payment_at_details_check_box_one)
    void paymentMethodPickupView(AppCompatRadioButton view) {
        view.setChecked(PaymentType.PICKUP.equals(packageDetails.get().getPaymentType()));
    }

    @OnResponse(R.id.requestPackageDetails)
    @Executable(R.id.screen_pickup_process_verify_package_payment_at_details_check_box_two)
    void paymentMethodDeliveryView(AppCompatRadioButton view) {
        view.setChecked(PaymentType.DELIVERY.equals(packageDetails.get().getPaymentType()));
    }

    // Buy with :

    @Initializer(R.id.screen_pickup_process_verify_package_buy_with_details_check_box_one)
    void buyWithCashInitializer(AppCompatRadioButton view) {
        view.setOnCheckedChangeListener(new OnEventListener(this));
        view.setText(R.string.payment_method_cash);
        view.setTag(PaymentMethod.CASH);
    }

    @Initializer(R.id.screen_pickup_process_verify_package_buy_with_details_check_box_two)
    void buyWithCreditCardInitializer(AppCompatRadioButton view) {
        view.setOnCheckedChangeListener(new OnEventListener(this));
        view.setText(R.string.payment_method_credit_card);
        view.setTag(PaymentMethod.CREDIT_CARD);
    }

    @OnResponse(R.id.requestPackageDetails)
    @Executable(R.id.screen_pickup_process_verify_package_buy_with_details_check_box_one)
    void buyWithCashView(AppCompatRadioButton view) {
        view.setChecked(PaymentMethod.CASH.equals(packageDetails.get().getPaymentMethod()));
    }


    @OnResponse(R.id.requestPackageDetails)
    @Executable(R.id.screen_pickup_process_verify_package_buy_with_details_check_box_two)
    void buyWithCreditCardView(AppCompatRadioButton view) {
        view.setChecked(PaymentMethod.CREDIT_CARD.equals(packageDetails.get().getPaymentMethod()));
    }

    // submit :

    @Executable(R.id.screen_pickup_process_verify_package_main_layout)
    void mainLayout(View view) {
        view.setVisibility(submitting.isTrue() ? View.GONE : View.VISIBLE);
    }

    @Executable(R.id.screen_pickup_process_verify_package_main_progress)
    void progressLayout(View view) {
        view.setVisibility(submitting.isTrue() ? View.VISIBLE : View.GONE);
    }

    @Initializer({
            R.id.screen_pickup_process_verify_package_images_left_image_main_image,
            R.id.screen_pickup_process_verify_package_images_right_image_main_image})
    void imagesInitializer(ImageView imageView) {
        OnEventListener.Options options = new OnEventListener.Options();
        options.onTouchMotionEvent(MotionEvent.ACTION_DOWN);
        OnEventListener onEventListener = new OnEventListener(this, options);
        imageView.setFocusableInTouchMode(false);
        imageView.setOnTouchListener(onEventListener);
    }

    @OnResponse(R.id.requestPackageDetails)
    @Executable(R.id.screen_pickup_process_verify_package_images_left_image_main_image)
    void leftImage(ImageView imageView) {
        if (!packageDetails.isEmpty()) {
            drawImage(IMAGE_INDEX_LEFT, imageView);
        }
    }

    @OnResponse(R.id.requestPackageDetails)
    @Executable(R.id.screen_pickup_process_verify_package_images_right_image_main_image)
    void rightImage(ImageView imageView) {
        if (!packageDetails.isEmpty()) {
            drawImage(IMAGE_INDEX_RIGHT, imageView);
        }
    }

    private void drawImage(int index, ImageView imageView) {
        try {
            ServerImage serverImage = packageDetails.get().getServerImageByIndex(index);
            new ServerImageDrawer(getHostActivity(), serverImage).execute(imageView);
        } catch (UnsupportedOperationException e) {
            Logger.getInstance().error(getClass(), "[" + index + "] " + e);
        }


    }

}
