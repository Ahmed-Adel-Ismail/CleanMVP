package com.entities.cached;

import java.io.Serializable;

/**
 * Created by Wafaa on 12/26/2016.
 */

public class PickupService implements Serializable{

    protected String type;
    protected String location;
    protected String quantity;
    protected String price;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

}
