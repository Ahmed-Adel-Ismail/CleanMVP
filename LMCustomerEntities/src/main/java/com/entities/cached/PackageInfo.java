package com.entities.cached;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wafaa on 11/14/2016.
 */

public class PackageInfo extends Package {

    private String packageType;
    private List<String> labels;
    private String shipmentServiceType;
    private Long shipmentServiceId;
    private String shipmentService;
    private String packageDimension;

    public String getPackageType() {
        return packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public String getShipmentServiceType() {
        return shipmentServiceType;
    }

    public void setShipmentServiceType(String shipmentServiceType) {
        this.shipmentServiceType = shipmentServiceType;
    }

    public Long getShipmentServiceId() {
        return shipmentServiceId;
    }

    public void setShipmentServiceId(Long shipmentServiceId) {
        this.shipmentServiceId = shipmentServiceId;
    }

    public String getShipmentService() {
        return shipmentService;
    }

    public void setShipmentService(String shipmentService) {
        this.shipmentService = shipmentService;
    }

    public String getPackageDimension() {
        return packageDimension;
    }

    public void setPackageDimension(String packageDimension) {
        this.packageDimension = packageDimension;
    }

    public List<PackageInfo> retrievePackageList() {
        return new ArrayList<>();
    }
}
