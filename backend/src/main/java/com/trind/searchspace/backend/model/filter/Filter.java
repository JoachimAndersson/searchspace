package com.trind.searchspace.backend.model.filter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.trind.searchspace.backend.model.query.targettype.QueryTargetType;

/**
 * Created by Joachim on 2014-09-15.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = TermFilter.class, name = "TERMFILTER"),
        @JsonSubTypes.Type(value = ExistsFilter.class, name = "EXISTSFILTER"),
/*        @JsonSubTypes.Type(value = StatQuery.class, name = "STAT"),
        @JsonSubTypes.Type(value = HistogramQuery.class, name = "HISTOGRAM"),
        @JsonSubTypes.Type(value = ListQuery.class, name = "LIST")*/
})
@JsonIgnoreProperties(ignoreUnknown = true)
public interface Filter {

    Occur getOccur();

    QueryTargetType getSourceTargetType();

    String getField();

    void setSourceTargetType(QueryTargetType sourceTargetType);

    void setField(String field);

}
