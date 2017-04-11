package com.entities.cached;

import com.base.annotations.MockEntity;
import com.entities.mocks.MockedReceipt;

import java.io.Serializable;

/**
 * Created by Wafaa on 12/26/2016.
 */

@MockEntity(MockedReceipt.class)
public class Receipt implements Serializable {

    protected String code;
    protected String date;
    protected String time;
    protected PickupServiceGroup services;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public PickupServiceGroup getServices() {
        return services;
    }

    public void setServices(PickupServiceGroup services) {
        this.services = services;
    }

    public String getTotalWithoutTaxes() {
        return totalWithoutTaxes;
    }

    public void setTotalWithoutTaxes(String totalWithoutTaxes) {
        this.totalWithoutTaxes = totalWithoutTaxes;
    }

    public String getTaxes() {
        return taxes;
    }

    public void setTaxes(String taxes) {
        this.taxes = taxes;
    }

    public String getTotalWithTaxes() {
        return totalWithTaxes;
    }

    public void setTotalWithTaxes(String totalWithTaxes) {
        this.totalWithTaxes = totalWithTaxes;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
