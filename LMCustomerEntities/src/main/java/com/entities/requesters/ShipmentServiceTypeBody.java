package com.entities.requesters;

import java.io.Serializable;

/**
 * Created by wafaa2016 on 06/08/2016.
 */
public class ShipmentServiceTypeBody implements Serializable, Cloneable {

    private long companyId = 0 ;
    private long shipmentServiceId ;

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public long getShipmentServiceId() {
        return shipmentServiceId;
    }

    public void setShipmentServiceId(long shipmentServiceId) {
        this.shipmentServiceId = shipmentServiceId;
    }
}
