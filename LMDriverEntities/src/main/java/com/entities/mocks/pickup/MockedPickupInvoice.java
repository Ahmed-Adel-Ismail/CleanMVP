package com.entities.mocks.pickup;

import com.entities.cached.payment.PaymentMethod;
import com.entities.cached.pickup.PickupInvoice;
import com.entities.mocks.cache.CacheKeys;
import com.entities.mocks.cache.MocksCache;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Date;

public class MockedPickupInvoice extends PickupInvoice {

    public MockedPickupInvoice() {
        super.code = String.valueOf((int) (Math.random() * 10000000000L));

        Date date = new Date();
        super.date = DateFormat.getDateInstance(DateFormat.SHORT).format(date);
        super.time = DateFormat.getTimeInstance(DateFormat.SHORT).format(date);

        MockedPickupServicesGroup mockedServices = new MockedPickupServicesGroup();
        super.services = mockedServices;

        int total = mockedServices.priceOne
                + mockedServices.priceTwo
                + mockedServices.priceThree;

        DecimalFormat f = new DecimalFormat("000.00");
        super.totalWithTaxes = String.valueOf(total);
        super.totalWithoutTaxes = f.format(total * 0.9);
        super.taxes = f.format(total * 0.1);
        super.paymentMethod = createMockedPaymentMethod(total);
    }

    private PaymentMethod createMockedPaymentMethod(int total) {
        PaymentMethod p;
        p = (PaymentMethod) MocksCache.getInstance().get(CacheKeys.PACKAGE_DETAILS_PAYMENT_METHOD);
        if (p == null) {
            p = (total % 2) == 0 ? PaymentMethod.CASH : PaymentMethod.CREDIT_CARD;
        }
        return p;
    }


}
