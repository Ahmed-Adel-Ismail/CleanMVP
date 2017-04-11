package com.entities.cached.orders;

import com.base.cached.AvailablePackageTypes;
import com.entities.cached.pakage.PackageType;

import java.io.Serializable;

/**
 * a class that indicates an Order in the LoadingActivity screen
 * <p>
 * Created by Ahmed Adel on 2/16/2017.
 */
public class LoadingActivityOrder implements Serializable {

    private long id;
    private long imageId;
    private PackageType packageType;
    private String content;
    private double actualWeight;

    public double getActualWeight() {
        return actualWeight;
    }

    public void setActualWeight(double actualWeight) {
        this.actualWeight = actualWeight;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getImageId() {
        return imageId;
    }

    public void setImageId(long imageId) {
        this.imageId = imageId;
    }

    public PackageType getPackageType() {
        return packageType;
    }

    public AvailablePackageTypes getAvailablePackageType() {
        if (packageType != null) {
            return packageType.getPackageTypeIdEnum();
        } else {
            return AvailablePackageTypes.FACTORY;
        }
    }

    public void setPackageType(PackageType packageType) {
        this.packageType = packageType;
    }
}
