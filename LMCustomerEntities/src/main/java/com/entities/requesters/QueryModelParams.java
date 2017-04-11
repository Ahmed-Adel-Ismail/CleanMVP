package com.entities.requesters;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Wafaa on 12/20/2016.
 */

public class QueryModelParams implements Serializable {

    protected QueryParams query;
    protected String queryName;

    public QueryParams getQuery() {
        return query;
    }

    public void setQuery(QueryParams query) {
        this.query = query;
    }


    public void setQueryName(String queryName) {
        this.queryName = queryName;
    }
}
