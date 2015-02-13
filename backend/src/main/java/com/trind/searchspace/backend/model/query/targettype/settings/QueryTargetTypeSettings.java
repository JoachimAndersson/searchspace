package com.trind.searchspace.backend.model.query.targettype.settings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.trind.searchspace.backend.model.query.QueryType;
import com.trind.searchspace.backend.model.query.targettype.QueryTargetType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joachim on 2014-10-25.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryTargetTypeSettings extends QueryTargetType {

    List<QueryType> supportedQueryTypes = new ArrayList<>();

    public List<QueryType> getSupportedQueryTypes() {
        return supportedQueryTypes;
    }

    public void setSupportedQueryTypes(List<QueryType> supportedQueryTypes) {
        this.supportedQueryTypes = supportedQueryTypes;
    }
}
