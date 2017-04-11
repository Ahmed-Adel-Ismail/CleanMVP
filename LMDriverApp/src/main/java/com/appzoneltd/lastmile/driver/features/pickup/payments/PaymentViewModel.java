package com.appzoneltd.lastmile.driver.features.pickup.payments;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appzoneltd.lastmile.driver.R;
import com.appzoneltd.lastmile.driver.subfeatures.PaymentMethodString;
import com.base.abstraction.annotations.interfaces.Executable;
import com.base.abstraction.annotations.interfaces.Initializer;
import com.base.abstraction.commands.Command;
import com.base.abstraction.logs.Logger;
import com.base.presentation.references.BooleanProperty;
import com.base.abstraction.system.App;
import com.base.abstraction.system.AppResources;
import com.base.presentation.annotations.interfaces.OnResponse;
import com.base.presentation.annotations.interfaces.Sync;
import com.base.presentation.base.abstracts.features.ViewBinder;
import com.base.presentation.base.presentation.ViewModel;
import com.base.presentation.references.Entity;
import com.entities.cached.pakage.PackageDetails;
import com.entities.cached.payment.PaymentMethod;
import com.entities.cached.pickup.PickupInvoice;
import com.entities.cached.pickup.PickupService;
import com.entities.cached.pickup.PickupServicesGroup;

class PaymentViewModel extends ViewModel {

    @Sync("pickupInvoice")
    final Entity<PickupInvoice> pickupInvoice = new Entity<>();

    @Sync("packageDetails")
    final Entity<PackageDetails> packageDetails = new Entity<>();

    final BooleanProperty progress = new BooleanProperty();

    @Override
    public void initialize(ViewBinder viewBinder) {
        super.initialize(viewBinder);
        progress.onUpdate(invalidateViewsOnProgressToggle());
    }

    @NonNull
    private Command<Boolean, Boolean> invalidateViewsOnProgressToggle() {
        return new Command<Boolean, Boolean>() {
            @Override
            public Boolean execute(Boolean p) {
                invalidateViews();
                return p;
            }
        };
    }

    @OnResponse(R.id.requestPickupInvoice)
    @Initializer(R.id.screen_pickup_process_payment_header_invoice_text_view)
    void invoiceCodeTextView(TextView textView) {
        String code = pickupInvoice.get().getCode();
        if (!TextUtils.isEmpty(code)) {
            String txt = AppResources.string(R.string.screen_pickup_process_payment_invoice_formatted);
            code = String.format(App.getInstance().getLocale(), txt, code);
        } else {
            code = AppResources.string(R.string.screen_pickup_process_payment_invoice);
        }
        textView.setText(code);

    }

    @OnResponse(R.id.requestPickupInvoice)
    @Initializer(R.id.screen_pickup_process_payment_header_date_text_view)
    void invoiceDateTextView(TextView textView) {
        String code = pickupInvoice.get().getDate();
        if (!TextUtils.isEmpty(code)) {
            String txt = AppResources.string(R.string.screen_pickup_process_payment_date_formatted);
            code = String.format(App.getInstance().getLocale(), txt, code);
        } else {
            code = AppResources.string(R.string.screen_pickup_process_payment_date);
        }
        textView.setText(code);

    }

    @OnResponse(R.id.requestPickupInvoice)
    @Initializer(R.id.screen_pickup_process_payment_header_time_text_view)
    void invoiceTimeTextView(TextView textView) {
        String code = pickupInvoice.get().getTime();
        if (!TextUtils.isEmpty(code)) {
            String txt = AppResources.string(R.string.screen_pickup_process_payment_time_formatted);
            code = String.format(App.getInstance().getLocale(), txt, code);
        } else {
            code = AppResources.string(R.string.screen_pickup_process_payment_time);
        }
        textView.setText(code);

    }

    @OnResponse(R.id.requestPickupInvoice)
    @Initializer(R.id.screen_pickup_process_payment_total_services_right_text_view)
    void totalServicesTextView(TextView textView) {
        PickupServicesGroup services = pickupInvoice.get().getServices();
        int count = (services != null) ? services.size() : 0;
        textView.setText(String.valueOf(count));
    }

    @OnResponse(R.id.requestPickupInvoice)
    @Initializer(R.id.screen_pickup_process_payment_total_without_tax_right_text_view)
    void totalWithoutTaxTextView(TextView textView) {
        String text = pickupInvoice.get().getTotalWithoutTaxes();
        text = (!TextUtils.isEmpty(text)) ? text : "0";
        textView.setText(text);
    }

    @OnResponse(R.id.requestPickupInvoice)
    @Initializer(R.id.screen_pickup_process_payment_value_added_tax_right_text_view)
    void valueAddedTaxesTextView(TextView textView) {
        String text = pickupInvoice.get().getTaxes();
        text = (!TextUtils.isEmpty(text)) ? text : "0";
        textView.setText(text);
    }

    @OnResponse(R.id.requestPickupInvoice)
    @Initializer(R.id.screen_pickup_process_payment_total_amount_right_text_view)
    void totalAmountTextView(TextView textView) {
        String text = pickupInvoice.get().getTotalWithTaxes();
        text = (!TextUtils.isEmpty(text)) ? text : "0";
        textView.setText(text);
    }

    @OnResponse({R.id.requestPickupInvoice, R.id.requestPackageDetails})
    @Executable(R.id.screen_pickup_process_payment_method_right_text_view)
    void paymentMethodTextView(TextView textView) {
        PaymentMethod method;
        if (!packageDetails.isEmpty() && packageDetails.get().getPaymentMethod() != null) {
            method = packageDetails.get().getPaymentMethod();
        } else if (!pickupInvoice.isEmpty()) {
            method = pickupInvoice.get().getPaymentMethod();
        } else {
            Logger.getInstance().exception(new NullPointerException("Payment method unknown"));
            method = PaymentMethod.CASH;
        }
        textView.setText(new PaymentMethodString().execute(method));
    }

    @OnResponse(R.id.requestPickupInvoice)
    @Initializer(R.id.screen_pickup_process_payment_service_types_outer_relative_layout)
    void serviceTypesLayout(ViewGroup viewGroup) {
        PickupServicesGroup services = pickupInvoice.get().getServices();
        if (services == null) {
            return;
        }

        for (int viewIndex = 1, serviceIndex = 0;
             viewIndex < viewGroup.getChildCount() && serviceIndex < services.size();
             viewIndex++, serviceIndex++) {

            drawLine(serviceIndex, viewIndex, viewGroup, services);

        }

    }

    private void drawLine(
            int serviceIndex,
            int viewIndex,
            ViewGroup viewGroup,
            PickupServicesGroup services) {

        PickupService pickupService = services.get(serviceIndex);
        String typeText = getTypeAndLocation(pickupService);
        String quantityText = getNonNullValue(pickupService.getQuantity());
        String priceText = getNonNullValue(pickupService.getPrice());

        ViewGroup child = (ViewGroup) viewGroup.getChildAt(viewIndex);
        child.setVisibility(View.VISIBLE);
        ((TextView) child.getChildAt(0)).setText(typeText);
        ((TextView) child.getChildAt(1)).setText(quantityText);
        ((TextView) child.getChildAt(2)).setText(priceText);
    }

    @NonNull
    private String getTypeAndLocation(PickupService pickupService) {

        String type = pickupService.getType();
        type = (!TextUtils.isEmpty(type)) ? type + "\n" : "";

        String location = pickupService.getLocation();
        if (TextUtils.isEmpty(location)) {
            location = "";
        }

        return type + location;
    }

    @NonNull
    private String getNonNullValue(String value) {
        if (TextUtils.isEmpty(value)) {
            value = "";
        }
        return value;
    }

    @Executable(R.id.screen_pickup_process_payment_main_layout)
    void mainLayout(View view) {
        view.setVisibility(progress.isTrue() ? View.GONE : View.VISIBLE);
    }

    @Executable(R.id.screen_pickup_process_payment_main_progress)
    void progressLayout(View view) {
        view.setVisibility(progress.isTrue() ? View.VISIBLE : View.GONE);
    }


}
