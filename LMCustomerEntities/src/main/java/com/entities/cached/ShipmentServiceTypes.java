package com.entities.cached;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Wafaa on 6/2/2016.
 */
public class ShipmentServiceTypes implements Serializable {

    private Long shipmentServiceTypeId;

    private String type;
    private String description;
    private long created;
    private Long version;
    private Long shipmentServiceId;
    private final String TYPE_TITLE = "Service Interval *";
    private List<ShipmentServiceTypes> shipmentServiceTypesList;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getShipmentServiceId() {
        return shipmentServiceId;
    }

    public void setShipmentServiceId(long shipmentServiceId) {
        this.shipmentServiceId = shipmentServiceId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ShipmentServiceTypes> getShipmentServiceTypesList() {
        return shipmentServiceTypesList;
    }

    public void setShipmentServiceTypesList(List<ShipmentServiceTypes> shipmentServiceTypesList) {
        this.shipmentServiceTypesList = shipmentServiceTypesList;
    }

    public Long getShipmentServiceTypeId() {
        return shipmentServiceTypeId;
    }

    public void setShipmentServiceTypeId(Long shipmentServiceTypeId) {
        this.shipmentServiceTypeId = shipmentServiceTypeId;
    }

    public void setShipmentServiceId(Long shipmentServiceId) {
        this.shipmentServiceId = shipmentServiceId;
    }
}
