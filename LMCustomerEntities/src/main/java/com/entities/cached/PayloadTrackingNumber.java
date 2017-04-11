package com.entities.cached;

import java.io.Serializable;

/**
 * Created by Wafaa on 1/5/2017.
 */

public class PayloadTrackingNumber implements Serializable {

    private long value;

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
