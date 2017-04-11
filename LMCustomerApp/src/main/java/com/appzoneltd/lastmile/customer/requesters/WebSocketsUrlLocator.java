package com.appzoneltd.lastmile.customer.requesters;

import com.appzoneltd.lastmile.customer.R;
import com.base.usecases.requesters.server.urls.ResourcesUrlLocator;

/**
 * Created by Wafaa on 12/20/2016.
 */

public class WebSocketsUrlLocator extends ResourcesUrlLocator {

    public WebSocketsUrlLocator() {
        super(R.string.serverProductionUrlWss, R.string.serverTestUrlWss);
    }
}
