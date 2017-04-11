package com.entities.cached;

import com.entities.requesters.QueryModelParamsContainer;

/**
 * Created by Wafaa on 12/20/2016.
 */

public class QueryModelResponse extends QueryModelParamsContainer{

    protected long port;
    protected long serverId;

    public long getPort() {
        return port;
    }

    public void setPort(long port) {
        this.port = port;
    }

    public long getServerId() {
        return serverId;
    }

    public void setServerId(long serverId) {
        this.serverId = serverId;
    }
}
