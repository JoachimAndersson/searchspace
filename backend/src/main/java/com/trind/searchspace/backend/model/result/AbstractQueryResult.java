package com.trind.searchspace.backend.model.result;

import com.trind.searchspace.backend.model.query.Query;

/**
 * Created by Joachim on 2014-09-15.
 */
public abstract class AbstractQueryResult implements QueryResult {
    Query searchQuery;
    long searchTime;

    public Query getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(Query searchQuery) {
        this.searchQuery = searchQuery;
    }

    public long getSearchTime() {
        return searchTime;
    }

    public void setSearchTime(long searchTime) {
        this.searchTime = searchTime;
    }
}
