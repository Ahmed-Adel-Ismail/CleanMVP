package com.appzoneltd.lastmile.customer.models;

import com.base.abstraction.observer.Observer;
import com.base.presentation.models.Model;

import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;

/**
 * the parent class for all Last mile App Model classes
 * <p>
 * Created by Ahmed Adel on 10/18/2016.
 */
public abstract class LastMileModel extends Model {

    protected static final int HTTP_REQUEST_REFRESH_TOKEN = HTTP_UNAUTHORIZED;

    public LastMileModel(Observer presentationObserver) {
        super(presentationObserver);
    }


}
