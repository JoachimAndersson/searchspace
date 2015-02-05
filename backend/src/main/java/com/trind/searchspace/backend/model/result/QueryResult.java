package com.trind.searchspace.backend.model.result;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.trind.searchspace.backend.model.query.Query;

/**
 * Created by Joachim on 2014-09-15.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "queryType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = TermQueryResult.class, name = "TERM"),
        @JsonSubTypes.Type(value = StatQueryResult.class, name = "STAT"),
        @JsonSubTypes.Type(value = HistogramQueryResult.class, name = "HISTOGRAM"),
        @JsonSubTypes.Type(value = ListQueryResult.class, name = "LIST")
})
public interface QueryResult {

    Query getSearchQuery();

    void setSearchQuery(Query searchQuery);

    long getSearchTime();

    public void setSearchTime(long searchTime);

}
