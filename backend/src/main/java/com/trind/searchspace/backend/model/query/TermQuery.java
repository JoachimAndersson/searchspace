package com.trind.searchspace.backend.model.query;

/**
 * Created by Joachim on 2014-09-15.
 */
public class TermQuery extends AbstractBaseQuery {

    int size;
    String field;
    boolean includeUnknown = true;
    boolean includeOther = true;


    @Override
    public QueryType getQueryType() {
        return QueryType.TERM;
    }


    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public boolean isIncludeUnknown() {
        return includeUnknown;
    }

    public void setIncludeUnknown(boolean includeUnknown) {
        this.includeUnknown = includeUnknown;
    }

    public boolean isIncludeOther() {
        return includeOther;
    }

    public void setIncludeOther(boolean includeOther) {
        this.includeOther = includeOther;
    }
}
