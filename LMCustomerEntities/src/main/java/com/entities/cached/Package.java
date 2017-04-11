package com.entities.cached;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class Package implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long packageId;
    private String nickName;
    private Long packageTypeId;
    private BigDecimal actualWeight;
    private Long shipmentServiceTypeId;
    private String status;
    private String description;
    private long created;
    private Long version;
    private List<Long> imageIds;
    private List<Long> labelIds;


    public Package() {

    }


    public void setPackageId(long packageId) {
        this.packageId = packageId;
    }

    public long getPackageId() {
        return this.packageId;
    }

    public void setNickName(String nickname) {
        this.nickName = nickname;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setPackageTypeId(long packageTypeId) {
        this.packageTypeId = packageTypeId;
    }

    public long getPackageTypeId() {
        return this.packageTypeId;
    }

    public void setActualWeight(BigDecimal actualWeight) {
        this.actualWeight = actualWeight;
    }

    public BigDecimal getActualWeight() {
        return this.actualWeight;
    }


    public List<Long> getImageIds() {
        return imageIds;
    }

    public void setImageIds(List<Long> imageIds) {
        this.imageIds = imageIds;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }

    public void setPackageTypeId(Long packageTypeId) {
        this.packageTypeId = packageTypeId;
    }

    public Long getShipmentServiceTypeId() {
        return shipmentServiceTypeId;
    }

    public void setShipmentServiceTypeId(Long shipmentServiceTypeId) {
        this.shipmentServiceTypeId = shipmentServiceTypeId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
