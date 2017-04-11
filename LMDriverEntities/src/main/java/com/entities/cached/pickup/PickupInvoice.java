package com.entities.cached.pickup;

import com.base.annotations.MockEntity;
import com.entities.cached.payment.PaymentMethod;
import com.entities.mocks.pickup.MockedPickupInvoice;

import java.io.Serializable;

/**
 * the Invoice Object that the client display it for approving payments
 * <p>
 * Created by Ahmed Adel on 1/1/2017.
 */
@MockEntity(MockedPickupInvoice.class)
public class PickupInvoice implements Serializable {

    protected String code;
    protected String date;
    protected String time;
    protected PickupServicesGroup services;
    protected String totalWithoutTaxes;
    protected String taxes;
    protected String totalWithTaxes;
    protected PaymentMethod paymentMethod;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PickupServicesGroup getServices() {
        return services;
    }

    public void setServices(PickupServicesGroup services) {
        this.services = services;
    }

    public String getTaxes() {
        return taxes;
    }

    public void setTaxes(String taxes) {
        this.taxes = taxes;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTotalWithoutTaxes() {
        return totalWithoutTaxes;
    }

    public void setTotalWithoutTaxes(String totalWithoutTaxes) {
        this.totalWithoutTaxes = totalWithoutTaxes;
    }

    public String getTotalWithTaxes() {
        return totalWithTaxes;
    }

    public void setTotalWithTaxes(String totalWithTaxes) {
        this.totalWithTaxes = totalWithTaxes;
    }
}
