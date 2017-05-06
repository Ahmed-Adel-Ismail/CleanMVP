package com.base.presentation.requests;

import com.base.abstraction.system.AppResources;

import java.io.Serializable;

/**
 * The parent class for all XxxResult classes that are a result ofr a request-code based requests
 * <p/>
 * Created by Ahmed Adel on 10/3/2016.
 */
class CodedResult implements Serializable {

    public final int requestCode;


    CodedResult(int requestCode) {
        this.requestCode = requestCode;

    }

    /**
     * check if the request code is the same as the value of the passed {@code integer} resource
     *
     * @param requestCodeIntResource the {@link com.base.abstraction.R.integer} resource
     *                               that contains the request code value
     * @return {@code true} if the value of the {@code integer} resource is the same as the
     * {@link #requestCode}
     */
    public boolean hasRequestCode(int requestCodeIntResource) {
        return requestCode == AppResources.integer(requestCodeIntResource);
    }


}
