package com.trind.searchspace.backend.model.query;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.trind.searchspace.backend.model.query.targettype.QueryTargetType;

/**
 * Created by Joachim on 2014-09-15.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "queryType" )
@JsonSubTypes({
        @JsonSubTypes.Type(value = TermQuery.class, name = "TERM"),
        @JsonSubTypes.Type(value = StatQuery.class, name = "STAT"),
        @JsonSubTypes.Type(value = HistogramQuery.class, name = "HISTOGRAM"),
        @JsonSubTypes.Type(value = ListQuery.class, name = "LIST")
})
@JsonIgnoreProperties(ignoreUnknown = true)
public interface Query {

    QueryTargetType getQueryTargetType();

    void setQueryTargetType(QueryTargetType queryTargetType);

    QueryType getQueryType();
}
