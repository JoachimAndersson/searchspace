package com.trind.searchspace.backend.factory;

import com.trind.searchspace.backend.model.DateTimeField;
import com.trind.searchspace.backend.model.Parameter;
import com.trind.searchspace.backend.model.Search;
import com.trind.searchspace.backend.model.SearchResult;
import com.trind.searchspace.backend.model.filter.Filter;
import com.trind.searchspace.backend.model.mapping.FieldMapping;
import com.trind.searchspace.backend.model.mapping.QueryTargetTypeWithField;
import com.trind.searchspace.backend.model.query.Query;
import com.trind.searchspace.backend.model.query.targettype.QueryTargetType;
import com.trind.searchspace.backend.model.query.targettype.settings.QueryTargetTypeSettings;
import com.trind.searchspace.backend.model.result.EmptyQueryResult;
import com.trind.searchspace.backend.model.result.QueryResult;
import com.trind.searchspace.backend.service.SearchService;
import com.trind.searchspace.backend.service.impl.ElasticsearchServiceImpl;
import com.trind.searchspace.backend.service.impl.MappingService;
import com.trind.searchspace.backend.service.impl.SourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Joachim on 2014-09-15.
 */
@Service
public class SearchServiceFactory {

    public static final EmptyQueryResult EMPTY_QUERY_RESULT = new EmptyQueryResult();

    @Autowired
    ElasticsearchServiceImpl elasticsearchService;

    @Autowired
    SourceService sourceService;

    @Autowired
    MappingService mappingService;

    public List<Parameter> getSearchParameters(QueryTargetType queryTargetType) {
        return getSearchService(queryTargetType.getQueryTarget()).getSearchParameters();
    }

    public List<Parameter> getSettingsParameters(QueryTargetType queryTargetType) {
        return getSearchService(queryTargetType.getQueryTarget()).getSettingsParameters();
    }

    public SearchService getSearchService(QueryTargetType queryTargetType) {
        return getSearchService(queryTargetType.getQueryTarget());
    }

    public SearchService getSearchService(String queryTarget) {
        if (elasticsearchService.getQueryTarget().equals(queryTarget)) {
            return elasticsearchService;
        }
        return null;
    }


    public SearchResult search(Search search) {
        SearchResult searchResult = new SearchResult();
        ExecutorService executor = Executors.newFixedThreadPool(20);
        try {
            List<Future<QueryResult>> queryResultFutures = new ArrayList<>();
            for (Query query : search.getQueries()) {
                SearchService searchService = getSearchService(query.getQueryTargetType());
                QueryTargetTypeSettings sourceById = sourceService.getSourceById(query.getQueryTargetType().getId());

                Future<QueryResult> submit = executor.submit(new SearchTask(searchService,
                        search,
                        sourceById,
                        query,
                        replaceFieldsInFilter(search, query),
                        getTimeField(query)));
                queryResultFutures.add(submit);
            }
            for (Future<QueryResult> queryResultFuture : queryResultFutures) {
                QueryResult queryResult = queryResultFuture.get();
                if (!EMPTY_QUERY_RESULT.equals(queryResult))
                    searchResult.getQueryResultList().add(queryResult);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
        return searchResult;
    }

    /**
     * Replace fields in filters based on the query.
     *
     * @param search
     * @param query
     * @return
     */
    private List<Filter> replaceFieldsInFilter(Search search, Query query) {
        //query.getQueryTargetType()

        List<FieldMapping> allMappings = mappingService.getAllMappings();

        for (Filter filter : search.getFilters()) {
            if (filter.getSourceTargetType() != null) {
                for (FieldMapping allMapping : allMappings) {
                    boolean exists = false;
                    for (QueryTargetTypeWithField queryTargetTypeWithField : allMapping.getQueryTargetTypes()) {
                        if (queryTargetTypeWithField.getQueryTargetType().equals(filter.getSourceTargetType())) {
                            if (filter.getField().equals(queryTargetTypeWithField.getField())) {
                                exists = true;
                            }
                        }
                    }
                    if (exists) {
                        for (QueryTargetTypeWithField queryTargetTypeWithField : allMapping.getQueryTargetTypes()) {
                            if (queryTargetTypeWithField.getQueryTargetType().equals(query.getQueryTargetType())) {
                                filter.setField(queryTargetTypeWithField.getField());
                                filter.setSourceTargetType(query.getQueryTargetType());
                            }
                        }
                    }
                }
            }
        }
        return search.getFilters();
    }


    private DateTimeField getTimeField(Query query) {
        return new DateTimeField("_timestamp", DateTimeField.DateTimeFieldType.MILLIS);
    }
}
