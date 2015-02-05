package com.trind.searchspace.backend.model;

import com.trind.searchspace.backend.model.result.QueryResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joachim on 2014-09-15.
 */
public class SearchResult {
    List<QueryResult> queryResultList = new ArrayList<>();



    public List<QueryResult> getQueryResultList() {
        return queryResultList;
    }
}
