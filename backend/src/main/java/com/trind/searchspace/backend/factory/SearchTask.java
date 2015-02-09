package com.trind.searchspace.backend.factory;

import com.trind.searchspace.backend.model.DateTimeField;
import com.trind.searchspace.backend.model.Search;
import com.trind.searchspace.backend.model.filter.Filter;
import com.trind.searchspace.backend.model.query.*;
import com.trind.searchspace.backend.model.query.targettype.settings.QueryTargetTypeSettings;
import com.trind.searchspace.backend.model.result.EmptyQueryResult;
import com.trind.searchspace.backend.model.result.QueryResult;
import com.trind.searchspace.backend.service.*;
import com.trind.searchspace.backend.util.DateUtil;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Joachim on 2014-09-15.
 */
public class SearchTask implements Callable<QueryResult> {

    SearchService searchService;
    QueryTargetTypeSettings abstractQueryTargetTypeSettings;
    Search search;
    Query query;
    List<Filter> filters;
    DateTimeField timeField;


    public SearchTask(SearchService searchService, Search search, QueryTargetTypeSettings abstractQueryTargetTypeSettings, Query query, List<Filter> filters, DateTimeField timeField) {
        this.searchService = searchService;
        this.search = search;
        this.query = query;
        this.filters = filters;
        this.timeField = timeField;
        this.abstractQueryTargetTypeSettings = abstractQueryTargetTypeSettings;
    }

    @Override
    public QueryResult call() throws Exception {
        QueryResult queryResult = new EmptyQueryResult();
        long searchTime = System.currentTimeMillis();
        if (QueryType.HISTOGRAM.equals(query.getQueryType())
                && searchService instanceof HistogramSearchService) {
            HistogramQuery histogramQuery = (HistogramQuery) query;
            queryResult = ((HistogramSearchService) searchService).search(abstractQueryTargetTypeSettings, histogramQuery, filters, timeField, DateUtil.parse(search.getTimeFrom()), DateUtil.parse(search.getTimeTo()));
        } else if (QueryType.TERM.equals(query.getQueryType())
                && searchService instanceof TermSearchService) {
            TermQuery termQuery = (TermQuery) query;
            queryResult = ((TermSearchService) searchService).search(abstractQueryTargetTypeSettings, termQuery, filters, timeField, DateUtil.parse(search.getTimeFrom()), DateUtil.parse(search.getTimeTo()));
        } else if (QueryType.LIST.equals(query.getQueryType())
                && searchService instanceof ListSearchService) {
            ListQuery listQuery = (ListQuery) query;
            queryResult = ((ListSearchService) searchService).search(abstractQueryTargetTypeSettings, listQuery, filters, timeField, DateUtil.parse(search.getTimeFrom()), DateUtil.parse(search.getTimeTo()));
        } else if (QueryType.STAT.equals(query.getQueryType())
                && searchService instanceof StatSearchService) {
            StatQuery statQuery = (StatQuery) query;
            queryResult = ((StatSearchService) searchService).search(abstractQueryTargetTypeSettings, statQuery, filters, timeField, DateUtil.parse(search.getTimeFrom()), DateUtil.parse(search.getTimeTo()));
        }

        if (queryResult == null) {
            queryResult = new EmptyQueryResult();
        }
        queryResult.setSearchQuery(query);
        queryResult.setSearchTime(System.currentTimeMillis() - searchTime);
        return queryResult;
    }

}
