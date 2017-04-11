package com.entities.requesters;

/**
 * an Object that holds the image ids or barcode required to validate the documents related
 * to a package
 * <p>
 * Created by Ahmed Adel on 12/19/2016.
 */
public class PackageDocuments extends BaseRequest {

    private String barcode;
    private long requestId;
    private long customerIdImageId;
    private long creditCardImageId;

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public long getCreditCardImageId() {
        return creditCardImageId;
    }

    public void setCreditCardImageId(long creditCardImageId) {
        this.creditCardImageId = creditCardImageId;
    }

    public long getCustomerIdImageId() {
        return customerIdImageId;
    }

    public void setCustomerIdImageId(long customerIdImageId) {
        this.customerIdImageId = customerIdImageId;
    }

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }
}
