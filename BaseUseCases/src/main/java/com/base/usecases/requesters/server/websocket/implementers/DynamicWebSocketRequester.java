package com.base.usecases.requesters.server.websocket.implementers;

import com.base.abstraction.api.usecases.RequestUrlLocator;
import com.base.abstraction.events.Message;
import com.base.usecases.requesters.server.ssl.HttpUrlGenerator;
import com.base.usecases.requesters.server.websocket.StaticWebSocketRequester;

/**
 * Created by Wafaa on 2/18/2017.
 */


public class DynamicWebSocketRequester extends StaticWebSocketRequester {

    public DynamicWebSocketRequester(RequestUrlLocator requestUrlLocator){
        super.initialize(requestUrlLocator);
    }


    @Override
    protected String createUrl(long eventId, Message content) {
        String baseUrl = getRequestUrlLocator().execute(eventId);
        return new HttpUrlGenerator(baseUrl).execute(content);
    }
}
