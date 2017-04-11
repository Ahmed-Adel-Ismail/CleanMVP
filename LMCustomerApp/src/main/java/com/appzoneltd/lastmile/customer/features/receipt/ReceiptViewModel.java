package com.appzoneltd.lastmile.customer.features.receipt;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.appzoneltd.lastmile.customer.R;
import com.base.abstraction.annotations.interfaces.Executable;
import com.base.abstraction.system.AppResources;
import com.base.presentation.annotations.interfaces.Sync;
import com.base.presentation.base.presentation.ViewModel;
import com.entities.cached.Receipt;

/**
 * Created by Wafaa on 12/24/2016.
 */

public class ReceiptViewModel extends ViewModel {

    private ReceiptListAdapter receiptListAdapter;
    @Sync("receipt")
    Receipt receipt;

    public ReceiptViewModel() {
        this.receiptListAdapter = new ReceiptListAdapter();
    }


    @Executable(R.id.pre_receipt_code)
    void invalidateReceiptCode(TextView code) {
        String text = String.format(AppResources.string(R.string.invoice), receipt.getCode());
        code.setText(text);
    }

    @Executable(R.id.pre_receipt_date)
    void invalidateReceiptDate(TextView textView) {
        String text = String.format(AppResources.string(R.string.date), receipt.getDate());
        textView.setText(text);
    }

    @Executable(R.id.pre_receipt_time)
    void invalidateReceiptTime(TextView textView) {
        String text = String.format(AppResources.string(R.string.time), receipt.getTime());
        textView.setText(text);
    }

    @Executable(R.id.pre_receipt_details_list)
    void invalidateReceiptDetailsList(RecyclerView list) {
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getFeature().getHostActivity());
        list.setLayoutManager(layoutManager);
        if (receipt.getServices() != null)
            receiptListAdapter.setup(receipt.getServices(), list);
    }

    @Executable(R.id.mid_receipt_total_service_value)
    void invalidateTotalService(TextView serviceValue) {
        serviceValue.setText(String.valueOf(receipt.getServices().size()));
    }

    @Executable(R.id.mid_receipt_total_without_tax_value)
    void invalidateTotalWithoutTax(TextView textView) {
        textView.setText(String.valueOf(receipt.getTotalWithoutTaxes()));
    }

    @Executable(R.id.mid_receipt_value_Added_tax_value)
    void invalidateValueAdded(TextView textView) {
        textView.setText(String.valueOf(receipt.getTaxes()));
    }

    @Executable(R.id.mid_receipt_total_amount_value)
    void invalidateTotalAmount(TextView textView) {
        textView.setText(receipt.getTotalWithTaxes());
    }

    @Executable(R.id.mid_receipt_payment_method_value)
    void invalidatePaymentMethod(TextView textView) {
        textView.setText(receipt.getPaymentMethod().getValue());
    }

    @Executable(R.id.post_receipt_terms_value)
    void invalidateReceiptTermsValue(TextView textView){
        textView.setText(AppResources.string(R.string.terms_and_conditions_value));
    }

}
