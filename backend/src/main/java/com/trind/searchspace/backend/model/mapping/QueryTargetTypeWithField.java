package com.trind.searchspace.backend.model.mapping;

import com.trind.searchspace.backend.model.query.targettype.QueryTargetType;

/**
 * Created by Joachim on 2014-11-13.
 */
public class QueryTargetTypeWithField {

    QueryTargetType queryTargetType;
    String field;


    public QueryTargetType getQueryTargetType() {
        return queryTargetType;
    }

    public void setQueryTargetType(QueryTargetType queryTargetType) {
        this.queryTargetType = queryTargetType;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
