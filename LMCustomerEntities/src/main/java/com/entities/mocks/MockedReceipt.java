package com.entities.mocks;

import com.entities.cached.PaymentMethod;
import com.entities.cached.Receipt;

/**
 * Created by Wafaa on 12/28/2016.
 */

public class MockedReceipt extends Receipt {

    public MockedReceipt(String code
            , String date
            , String time
            , MockedPickupServiceTypesGroup group
            , String totalWithoutTax
            , String valueAdded
            , String totalAmount
            , PaymentMethod paymentMethod) {

        super.code = code;
        super.date = date;
        super.time = time;
        super.services = group;
        super.totalWithoutTaxes = totalWithoutTax;
        super.taxes = valueAdded;
        super.totalWithTaxes = totalAmount;
        super.paymentMethod = paymentMethod;
    }

}
