package com.trind.searchspace.backend.service.impl;

import com.trind.searchspace.backend.model.DateTimeField;
import com.trind.searchspace.backend.model.GuiTypes;
import com.trind.searchspace.backend.model.Parameter;
import com.trind.searchspace.backend.model.filter.ExistsFilter;
import com.trind.searchspace.backend.model.filter.Filter;
import com.trind.searchspace.backend.model.filter.Occur;
import com.trind.searchspace.backend.model.filter.TermFilter;
import com.trind.searchspace.backend.model.query.HistogramQuery;
import com.trind.searchspace.backend.model.query.ListQuery;
import com.trind.searchspace.backend.model.query.StatQuery;
import com.trind.searchspace.backend.model.query.TermQuery;
import com.trind.searchspace.backend.model.query.targettype.QueryTargetEnum;
import com.trind.searchspace.backend.model.query.targettype.QueryTargetType;
import com.trind.searchspace.backend.model.query.targettype.settings.QueryTargetTypeSettings;
import com.trind.searchspace.backend.model.result.HistogramQueryResult;
import com.trind.searchspace.backend.model.result.ListQueryResult;
import com.trind.searchspace.backend.model.result.StatQueryResult;
import com.trind.searchspace.backend.model.result.TermQueryResult;
import com.trind.searchspace.backend.service.*;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.metadata.AliasMetaData;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.cluster.metadata.MetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.collect.Lists;
import org.elasticsearch.common.hppc.ObjectLookupContainer;
import org.elasticsearch.common.hppc.cursors.ObjectCursor;
import org.elasticsearch.common.hppc.cursors.ObjectObjectCursor;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filter.InternalFilter;
import org.elasticsearch.search.aggregations.bucket.missing.Missing;
import org.elasticsearch.search.aggregations.bucket.terms.InternalTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCount;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Joachim on 2014-09-15.
 */
@Service
public class ElasticsearchServiceImpl
        implements SearchService, HistogramSearchService,
        ListSearchService, StatSearchService, TermSearchService {

    public static final String CLUSTER_NAME = "ClusterName";
    public static final String ADDRESS = "Address";
    public static final String INDEX = "Index";
    public static final String TYPE = "Type";
    public static final String FIELD = "Field";

    private static Map<QueryTargetTypeSettings, TransportClient> elasticClients = new HashMap<>();

    private synchronized Client getClient(QueryTargetTypeSettings abstractQueryTargetTypeSettings) {
        TransportClient client = elasticClients.get(abstractQueryTargetTypeSettings);
        if (client != null) {
            return client;
        }


        Settings settings = ImmutableSettings.settingsBuilder()
                .put("cluster.name", abstractQueryTargetTypeSettings.getParameter(CLUSTER_NAME)).build();
        client = new TransportClient(settings);

        String[] addresses = abstractQueryTargetTypeSettings.getParameter(ADDRESS).split(";");

        for (String address : addresses) {
            String[] split = address.split(":");
            if (split.length == 2) {
                client.addTransportAddress(new InetSocketTransportAddress(split[0], Integer.parseInt(split[1])));
            }
        }
        elasticClients.put(abstractQueryTargetTypeSettings, client);

        return client;

    }


    private FilterBuilder createFilter(Filter filter) {
        if (filter instanceof TermFilter) {
            return FilterBuilders.termFilter(((TermFilter) filter).getField(), ((TermFilter) filter).getValue());
        }
        if (filter instanceof ExistsFilter) {
            return FilterBuilders.existsFilter(((ExistsFilter) filter).getField());
        }
        return FilterBuilders.boolFilter();
    }


    private BoolFilterBuilder createFilter(List<Filter> filters, DateTimeField timeField, LocalDateTime from, LocalDateTime to) {

        BoolFilterBuilder returnFilter = FilterBuilders.boolFilter();

        BoolFilterBuilder boolFilterBuilder = FilterBuilders.boolFilter();
        for (Filter filter : filters) {
            if (Occur.MUST.equals(filter.getOccur())) {
                boolFilterBuilder = boolFilterBuilder.must(createFilter(filter));
            } else if (Occur.SHOULD.equals(filter.getOccur())) {
                boolFilterBuilder = boolFilterBuilder.should(createFilter(filter));
            } else if (Occur.MUST_NOT.equals(filter.getOccur())) {
                boolFilterBuilder = boolFilterBuilder.mustNot(createFilter(filter));
            }
        }

        if (timeField != null && (from != null || to != null)) {
            RangeFilterBuilder rangeFilterBuilder = FilterBuilders.rangeFilter(timeField.getField());

            try {
                if (to != null) {
                    rangeFilterBuilder.to(timeField.getDateTimeFieldType().getAsString(to));
                }
                if (from != null) {
                    rangeFilterBuilder.from(timeField.getDateTimeFieldType().getAsString(from));
                }
                returnFilter.must(rangeFilterBuilder);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (boolFilterBuilder.hasClauses()) {
            returnFilter.must(boolFilterBuilder);
        }
        return returnFilter;
    }


    private SearchRequestBuilder getSearchRequestBuilder(QueryTargetTypeSettings abstractQueryTargetTypeSettings, TermQuery termQuery) {

        QueryStringQueryBuilder queryStringQueryBuilder = QueryBuilders.queryString(termQuery.getQueryString());

        SearchRequestBuilder searchRequestBuilder = getClient(abstractQueryTargetTypeSettings).prepareSearch(getIndexes(abstractQueryTargetTypeSettings)).
                setQuery(queryStringQueryBuilder);

        if (StringUtils.isNotBlank(abstractQueryTargetTypeSettings.getParameter(TYPE))) {
            searchRequestBuilder = searchRequestBuilder.setTypes(getTypes(abstractQueryTargetTypeSettings));
        }
        return searchRequestBuilder;
    }

    private String[] getIndexes(QueryTargetType elastichQueryTargetType) {
        if (StringUtils.isEmpty(elastichQueryTargetType.getParameter(INDEX))) {
            return new String[0];
        }
        return elastichQueryTargetType.getParameter(INDEX).split(",");
    }

    private String[] getTypes(QueryTargetType elastichQueryTargetType) {
        if (StringUtils.isEmpty(elastichQueryTargetType.getParameter(TYPE))) {
            return new String[0];
        }

        return StringUtils.split(elastichQueryTargetType.getParameter(TYPE), ",");
    }


    @Override
    public String getQueryTarget() {
        return QueryTargetEnum.ELASTICSEARCH.name();
    }

    @Override
    public HistogramQueryResult search(QueryTargetTypeSettings abstractQueryTargetTypeSettings,
                                       HistogramQuery histogramQuery,
                                       List<Filter> filter,
                                       DateTimeField timeFiled,
                                       LocalDateTime timeFrom,
                                       LocalDateTime timeTo) {
        return null;
    }

    @Override
    public TermQueryResult search(QueryTargetTypeSettings abstractQueryTargetTypeSettings, TermQuery termQuery, List<Filter> filters, DateTimeField timeFiled, LocalDateTime timeFrom, LocalDateTime timeTo) {

        long totalTerms = 0;
        BoolFilterBuilder filter = createFilter(filters, timeFiled, timeFrom, timeTo);


        AggregationBuilder terms = AggregationBuilders.terms("terms").field(termQuery.getField()).size(termQuery.getSize());

        AbstractAggregationBuilder count = AggregationBuilders.count("count").field(termQuery.getField());

        AggregationBuilder missing = AggregationBuilders.missing("missing").field(termQuery.getField());

        SearchRequestBuilder searchRequestBuilder = getSearchRequestBuilder(abstractQueryTargetTypeSettings, termQuery)
                .setFetchSource(false).setNoFields()
                .setTrackScores(false).setSearchType(SearchType.COUNT);

        if (filter.hasClauses()) {
            searchRequestBuilder = searchRequestBuilder.setPostFilter(filter);
            terms = AggregationBuilders.filter("termsFilter").filter(filter).subAggregation(terms);
            missing = AggregationBuilders.filter("missingFilter").filter(filter).subAggregation(missing);
            count = AggregationBuilders.filter("countFilter").filter(filter).subAggregation(count);
        }

        searchRequestBuilder = searchRequestBuilder.addAggregation(terms);

        if (termQuery.isIncludeOther()) {
            searchRequestBuilder = searchRequestBuilder.addAggregation(count);
        }

        if (termQuery.isIncludeUnknown()) {
            searchRequestBuilder = searchRequestBuilder.addAggregation(missing);
        }

        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();

        TermQueryResult termQueryResult = new TermQueryResult();

        InternalFilter termsFilter = searchResponse.getAggregations().get("termsFilter");
        InternalTerms termsResult;

        if (termsFilter != null) {
            termsResult = termsFilter.getAggregations().get("terms");
        } else {
            termsResult = searchResponse.getAggregations().get("terms");
        }

        if (termsResult != null) {
            for (Terms.Bucket bucket : termsResult.getBuckets()) {
                termQueryResult.getList().add(new TermQueryResult.Term(bucket.getKeyAsText().string(), bucket.getDocCount()));
                totalTerms = totalTerms + bucket.getDocCount();
            }
        }

        InternalFilter missingFilter = searchResponse.getAggregations().get("missingFilter");
        Missing missingResult;
        if (missingFilter != null) {
            missingResult = missingFilter.getAggregations().get("missing");
        } else {
            missingResult = searchResponse.getAggregations().get("missing");
        }

        if (missingResult != null) {
            termQueryResult.getList().add(new TermQueryResult.Term(TermQueryResult.Term.UNKNOWN, missingResult.getDocCount()));
        }

        if (termQuery.isIncludeOther()) {

            InternalFilter countFilter = searchResponse.getAggregations().get("countFilter");
            ValueCount countResult;

            if (countFilter != null) {
                countResult = countFilter.getAggregations().get("count");
            } else {
                countResult = searchResponse.getAggregations().get("count");
            }
            termQueryResult.getList().add(new TermQueryResult.Term(TermQueryResult.Term.OTHERVALUE, countResult.getValue() - totalTerms));
        }

        return termQueryResult;
    }

    @Override
    public ListQueryResult search(QueryTargetTypeSettings abstractQueryTargetTypeSettings, ListQuery histogramQuery, List<Filter> filter, DateTimeField timeFiled, LocalDateTime timeFrom, LocalDateTime timeTo) {
        return null;
    }

    @Override
    public StatQueryResult search(QueryTargetTypeSettings abstractQueryTargetTypeSettings, StatQuery histogramQuery, List<Filter> filter, DateTimeField timeFiled, LocalDateTime timeFrom, LocalDateTime timeTo) {
        return null;
    }

    @Override
    public List<String> autoComplete(QueryTargetTypeSettings abstractQueryTargetTypeSettings, QueryTargetType queryTargetType, String field, String value) {
        List<String> collect = list(abstractQueryTargetTypeSettings, queryTargetType, field)
                .stream()
                .filter(p -> p.toLowerCase().startsWith(value.toLowerCase())).collect(Collectors.toList());
        return collect;
    }


    @Override
    public List<String> list(QueryTargetTypeSettings abstractQueryTargetTypeSettings, QueryTargetType queryTargetType, String field) {
        List<String> returnValues = new ArrayList<>();
        if (field.equals(INDEX)) {
            returnValues = getIndexes(abstractQueryTargetTypeSettings, queryTargetType);
        } else if (field.equals(TYPE)) {
            returnValues = getTypes(abstractQueryTargetTypeSettings, queryTargetType);
        } else if (field.equals(FIELD)) {
            returnValues = getField(abstractQueryTargetTypeSettings, queryTargetType);
        }
        return returnValues;
    }

    @Override
    public List<Parameter> getSettingsParameters() {


        return Arrays.asList(new Parameter[]{
                new Parameter(CLUSTER_NAME, GuiTypes.INPUT),
                new Parameter(ADDRESS, GuiTypes.INPUT)});
    }

    @Override
    public List<Parameter> getSearchParameters() {
        return Arrays.asList(new Parameter[]{
                new Parameter(INDEX, GuiTypes.AUTOCOMPLETE_WITH_QUERY),
                new Parameter(TYPE, GuiTypes.AUTOCOMPLETE_WITH_QUERY),
                new Parameter(FIELD, GuiTypes.SELECT_WITH_QUERY)});
    }


    private List<String> getField(QueryTargetTypeSettings abstractQueryTargetTypeSettings, QueryTargetType queryTargetType) {
        MetaData metaData = getClient(abstractQueryTargetTypeSettings).admin().cluster()
                .prepareState().execute()
                .actionGet().getState()
                .getMetaData();


        List<String> indexes = Lists.newArrayList(getIndexes((queryTargetType)));
        List<String> returnValues = new ArrayList<>();
        List<String> types = Lists.newArrayList(getTypes((queryTargetType)));

        if (indexes.contains("_all")) {
            String[] strings = getClient(abstractQueryTargetTypeSettings).admin().cluster()
                    .prepareState().execute()
                    .actionGet().getState()
                    .getMetaData().concreteAllOpenIndices();

            for (String index : strings) {
                IndexMetaData indexMetaData = metaData.index(index);
                if (indexMetaData != null) {
                    for (ObjectObjectCursor<String, MappingMetaData> stringMappingMetaDataObjectObjectCursor : indexMetaData.getMappings()) {
                        if (types.contains(stringMappingMetaDataObjectObjectCursor.key) || types.size() == 0) {
                            MappingMetaData mmd = indexMetaData.mapping(stringMappingMetaDataObjectObjectCursor.key);
                            try {
                                Map<String, Object> source = mmd.sourceAsMap();
                                Map<String, Object> properties = (Map<String, Object>) source.get("properties");
                                addProperty(returnValues, properties, "");
                            } catch (Exception e) {
                            }
                        }
                    }
                }
            }
        } else {
            for (ObjectObjectCursor<String, ImmutableOpenMap<String, AliasMetaData>> stringImmutableOpenMapObjectObjectCursor : metaData.aliases()) {
                if (indexes.contains(stringImmutableOpenMapObjectObjectCursor.key)) {
                    ObjectLookupContainer<String> keys = stringImmutableOpenMapObjectObjectCursor.value.keys();
                    for (ObjectCursor<String> key : keys) {
                        indexes.add(key.value);
                    }
                }
            }
            for (String index : indexes) {
                IndexMetaData indexMetaData = metaData.index(index);
                if (indexMetaData != null) {
                    for (ObjectObjectCursor<String, MappingMetaData> stringMappingMetaDataObjectObjectCursor : indexMetaData.getMappings()) {
                        if (types.contains(stringMappingMetaDataObjectObjectCursor.key) || types.size() == 0) {
                            MappingMetaData mmd = indexMetaData.mapping(stringMappingMetaDataObjectObjectCursor.key);
                            try {
                                Map<String, Object> source = mmd.sourceAsMap();
                                Map<String, Object> properties = (Map<String, Object>) source.get("properties");
                                addProperty(returnValues, properties, "");
                            } catch (Exception e) {
                            }
                        }
                    }
                }
            }
        }
        return returnValues;
    }

    private void addProperty(List<String> returnValues, Map<String, Object> properties, String parent) {
        for (Map.Entry<String, Object> stringObjectEntry : properties.entrySet()) {
            if (stringObjectEntry.getValue() instanceof LinkedHashMap) {
                if (stringObjectEntry.getKey().equals("properties")) {
                    addProperty(returnValues, (Map<String, Object>) stringObjectEntry.getValue(), parent);
                } else if (((Map<String, Object>) stringObjectEntry.getValue()).containsKey("properties")) {
                    addProperty(returnValues, (Map<String, Object>) stringObjectEntry.getValue(), parent + stringObjectEntry.getKey() + ".");
                } else {
                    returnValues.add(parent + stringObjectEntry.getKey());
                }
            }
        }
    }

    private List<String> getTypes(QueryTargetTypeSettings abstractQueryTargetTypeSettings, QueryTargetType queryTargetType) {
        MetaData metaData = getClient(abstractQueryTargetTypeSettings).admin().cluster()
                .prepareState().execute()
                .actionGet().getState()
                .getMetaData();


        List<String> indexes = Lists.newArrayList(getIndexes((queryTargetType)));
        List<String> returnValues = new ArrayList<>();

        if (indexes.contains("_all")) {
            String[] strings = getClient(abstractQueryTargetTypeSettings).admin().cluster()
                    .prepareState().execute()
                    .actionGet().getState()
                    .getMetaData().concreteAllOpenIndices();

            for (String index : strings) {
                System.out.println(index);
                IndexMetaData indexMetaData = metaData.index(index);
                if (indexMetaData != null) {
                    for (ObjectObjectCursor<String, MappingMetaData> stringMappingMetaDataObjectObjectCursor : indexMetaData.getMappings()) {
                        returnValues.add(stringMappingMetaDataObjectObjectCursor.key);
                    }
                }
            }
        } else {
            for (ObjectObjectCursor<String, ImmutableOpenMap<String, AliasMetaData>> stringImmutableOpenMapObjectObjectCursor : metaData.aliases()) {
                if (indexes.contains(stringImmutableOpenMapObjectObjectCursor.key)) {
                    ObjectLookupContainer<String> keys = stringImmutableOpenMapObjectObjectCursor.value.keys();
                    for (ObjectCursor<String> key : keys) {
                        indexes.add(key.value);
                    }
                }
            }

            for (String index : indexes) {
                IndexMetaData indexMetaData = metaData.index(index);
                if (indexMetaData != null) {
                    for (ObjectObjectCursor<String, MappingMetaData> stringMappingMetaDataObjectObjectCursor : indexMetaData.getMappings()) {
                        returnValues.add(stringMappingMetaDataObjectObjectCursor.key);
                    }
                }
            }
        }
        return returnValues;
    }

    private ArrayList<String> getIndexes(QueryTargetTypeSettings abstractQueryTargetTypeSettings, QueryTargetType queryTargetType) {
        String[] strings = getClient(abstractQueryTargetTypeSettings).admin().cluster()
                .prepareState().execute()
                .actionGet().getState()
                .getMetaData().concreteAllOpenIndices();

        ImmutableOpenMap<String, ImmutableOpenMap<String, AliasMetaData>> aliases = getClient(abstractQueryTargetTypeSettings).admin().cluster()
                .prepareState().execute()
                .actionGet().getState()
                .getMetaData().aliases();

        ArrayList<String> objects = new ArrayList<>();

        for (String index : strings) {
            objects.add(index);
        }

        for (ObjectObjectCursor<String, ImmutableOpenMap<String, AliasMetaData>> aliase : aliases) {
            objects.add(aliase.key);
        }
        return objects;
    }


}
