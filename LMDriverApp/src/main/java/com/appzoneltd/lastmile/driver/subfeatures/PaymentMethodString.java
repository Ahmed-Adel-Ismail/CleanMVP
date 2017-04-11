package com.appzoneltd.lastmile.driver.subfeatures;

import android.support.annotation.NonNull;

import com.appzoneltd.lastmile.driver.R;
import com.base.abstraction.commands.Command;
import com.base.abstraction.system.AppResources;
import com.entities.cached.payment.PaymentMethod;

/**
 * a Class that selects the proper {@link R.string} value to each {@link PaymentMethod}
 * <p>
 * Created by Ahmed Adel on 1/2/2017.
 */
public class PaymentMethodString implements Command<PaymentMethod, String> {

    @Override
    public String execute(PaymentMethod paymentMethod) {
        if (paymentMethod != null) {
            return selectStringResource(paymentMethod);
        } else {
            return "";
        }
    }

    private String selectStringResource(@NonNull PaymentMethod paymentMethod) {
        switch (paymentMethod) {
            case CASH:
                return AppResources.string(R.string.payment_method_cash);
            case CREDIT_CARD:
                return AppResources.string(R.string.payment_method_credit_card);
            default:
                return "";
        }
    }
}
