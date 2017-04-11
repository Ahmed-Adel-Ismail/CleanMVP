package com.appzoneltd.lastmile.customer.features.receipt;

import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.appzoneltd.lastmile.customer.R;
import com.base.presentation.annotations.interfaces.BindLayout;
import com.base.presentation.annotations.interfaces.Presenter;
import com.base.presentation.base.abstracts.features.ViewBinder;

import butterknife.BindView;

/**
 * Created by Wafaa on 12/24/2016.
 */

@BindLayout(R.layout.activity_receipt)
public class ReceiptViewBinder extends ViewBinder{

    @Presenter
    ReceiptPresenter receiptPresenter;

    @BindView(R.id.pre_receipt_code)
    TextView preReceiptCode;
    @BindView(R.id.pre_receipt_date)
    TextView preReceiptDate;
    @BindView(R.id.pre_receipt_time)
    TextView preReceiptTime;
    @BindView(R.id.pre_receipt_details_list)
    RecyclerView preReceiptDetailsList;
    @BindView(R.id.mid_receipt_total_service_value)
    TextView midReceiptTotalService;
    @BindView(R.id.mid_receipt_total_without_tax_value)
    TextView midReceiptTotalWithoutTax;
    @BindView(R.id.mid_receipt_value_Added_tax_value)
    TextView midReceiptValueAdded;
    @BindView(R.id.mid_receipt_total_amount_value)
    TextView midReceiptTotalAmount;
    @BindView(R.id.mid_receipt_payment_method_value)
    TextView midReceiptPaymentMethod;
    @BindView(R.id.post_receipt_terms_value)
    TextView postReceiptTerms;
}
