package com.trind.searchspace.backend.model.query;

import com.trind.searchspace.backend.model.query.targettype.QueryTargetType;

/**
 * Created by Joachim on 2014-09-15.
 */
public abstract class AbstractBaseQuery implements Query {

    QueryTargetType queryTargetType;
    String queryString;

    @Override
    public QueryTargetType getQueryTargetType() {
        return queryTargetType;
    }

    @Override
    public void setQueryTargetType(QueryTargetType queryTargetType) {
        this.queryTargetType = queryTargetType;
    }

    public String getQueryString() {
        return queryString != null ? queryString : "*";
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }
}
