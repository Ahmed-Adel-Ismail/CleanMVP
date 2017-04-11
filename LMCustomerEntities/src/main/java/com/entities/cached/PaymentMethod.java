package com.entities.cached;

/**
 * Created by Wafaa on 1/4/2017.
 */

public enum PaymentMethod {
    CASH("Cash"),
    CREDIT_CARD("Credit Card");

    PaymentMethod(String value){
        this.value = value;
    }

    String value;

    public String getValue(){
        return value;
    }

}
