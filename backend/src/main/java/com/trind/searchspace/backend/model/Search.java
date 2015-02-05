package com.trind.searchspace.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.trind.searchspace.backend.model.filter.Filter;
import com.trind.searchspace.backend.model.query.Query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joachim on 2014-09-15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Search implements Serializable {
    List<Query> queries = new ArrayList<>();
    List<Filter> filters = new ArrayList<>();
    String timeFrom;
    String timeTo;

    public Search() {
    }

    public List<Query> getQueries() {
        return queries;
    }

    public void setQueries(List<Query> queries) {
        this.queries = queries;
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }

    public String getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(String timeFrom) {
        this.timeFrom = timeFrom;
    }

    public String getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(String timeTo) {
        this.timeTo = timeTo;
    }
}
