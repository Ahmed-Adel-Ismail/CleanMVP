package com.entities.requesters;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Wafaa on 12/20/2016.
 */

public class QueryModelParamsContainer implements Serializable {

    protected List<QueryModelParams> queryModels;

    public List<QueryModelParams> getQueryModels() {
        return queryModels;
    }

    public void setQueryModels(List<QueryModelParams> queryModels) {
        this.queryModels = queryModels;
    }
}
