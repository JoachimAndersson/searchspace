package com.trind.searchspace.backend.model.filter;

import com.trind.searchspace.backend.model.query.targettype.QueryTargetType;

/**
 * Created by Joachim on 2014-09-15.
 */
public abstract class AbstractBaseFilter implements Filter{

    String field;

    Occur occur;

    QueryTargetType sourceTargetType;

    public Occur getOccur() {
        return occur;
    }

    public void setOccur(Occur occur) {
        this.occur = occur;
    }

    public QueryTargetType getSourceTargetType() {
        return sourceTargetType;
    }

    public void setSourceTargetType(QueryTargetType sourceTargetType) {
        this.sourceTargetType = sourceTargetType;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
