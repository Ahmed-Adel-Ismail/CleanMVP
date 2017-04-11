package com.entities.cached;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Wafaa on 6/1/2016.
 */
public class ShipmentService implements Serializable {

    private Long shipmentServiceId;
    private String service;
    private String description;
    private long created;
    private Long version;
    private final String SHIPPING_TITLE = "Service Type *";


    private List<ShipmentService> shipmentServices;


    public String getDescription() {
        return description;
    }


    public long getShipmentServiceId() {
        return shipmentServiceId;
    }

    public void setShipmentServiceId(long shipmentServiceId) {
        this.shipmentServiceId = shipmentServiceId;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    public void setShipmentList(List<ShipmentService> list) {
        this.shipmentServices = list;
    }


    public List<ShipmentService> getShipmentServices() {
        return shipmentServices;
    }
}
