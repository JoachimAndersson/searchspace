package com.trind.searchspace.backend.model.filter;

/**
 * Created by Joachim on 2014-09-17.
 */
public class ExistsFilter extends AbstractBaseFilter{

    String field;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

}
