package com.appzoneltd.lastmile.driver.requesters.servers.urls;

import com.appzoneltd.lastmile.driver.R;
import com.base.usecases.requesters.server.urls.ResourcesUrlLocator;

/**
 * a {@link ResourcesUrlLocator} that generates secure {@code wss} URLs
 * <p>
 * Created by Ahmed Adel on 1/22/2017.
 */
public class WebSocketsUrlLocator extends ResourcesUrlLocator {

    public WebSocketsUrlLocator() {
        super(R.string.serverProductionUrlWss, R.string.serverTestUrlWss);
    }
}
