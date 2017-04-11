package com.entities.cached.pickup;

import java.io.Serializable;

/**
 * the Pickup service provided to the client
 * <p>
 * Created by Ahmed Adel on 1/1/2017.
 */
public class PickupService implements Serializable {

    protected String type;
    protected String location;
    protected String quantity;
    protected String price;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
