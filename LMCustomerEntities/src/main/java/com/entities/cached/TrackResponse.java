package com.entities.cached;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Wafaa on 6/26/2016.
 */
public class TrackResponse implements Serializable {


    private long companyId;
    private long vehicleId;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private String area;
    private long isactive;
    private long isdeleted;
    private long created;

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public long getIsactive() {
        return isactive;
    }

    public void setIsactive(long isactive) {
        this.isactive = isactive;
    }

    public long getIsdeleted() {
        return isdeleted;
    }

    public void setIsdeleted(long isdeleted) {
        this.isdeleted = isdeleted;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }
}
