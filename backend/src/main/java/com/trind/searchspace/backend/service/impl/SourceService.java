package com.trind.searchspace.backend.service.impl;

import com.trind.searchspace.backend.factory.SearchServiceFactory;
import com.trind.searchspace.backend.model.Parameter;
import com.trind.searchspace.backend.model.query.targettype.settings.QueryTargetTypeSettings;
import com.trind.searchspace.backend.mongo.MongoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Joachim on 2014-09-29.
 */
@Service
public class SourceService {

    public static final String COLLECTION = "sources";

    @Autowired
    MongoService mongoService;

    @Autowired
    SearchServiceFactory searchServiceFactory;

    public List<QueryTargetTypeSettings> getAllSourcesWithoutSettings() {

        QueryTargetTypeSettings query = new QueryTargetTypeSettings();
        query.setId("Test1");
        query.setQueryTarget("ELASTICSEARCH");
        query.addParameter(ElasticsearchServiceImpl.ADDRESS, "192.168.0.28:9300");
        query.addParameter(ElasticsearchServiceImpl.CLUSTER_NAME, "elasticsearch");

        save(query);

        List<QueryTargetTypeSettings> allObjects = mongoService.getAllObjects(COLLECTION, QueryTargetTypeSettings.class);

        for (QueryTargetTypeSettings queryTargetTypeSettings : allObjects) {
            addEmptyParameters(queryTargetTypeSettings);
        }

        return allObjects;
    }

    private void addEmptyParameters(QueryTargetTypeSettings queryTargetTypeSettings) {
        List<Parameter> searchParameters = searchServiceFactory.getSearchParameters(queryTargetTypeSettings);

        for (Parameter searchParameter : searchParameters) {
            queryTargetTypeSettings.addParameter(new Parameter(searchParameter.getName(), searchParameter.getGuiTypes(), queryTargetTypeSettings.getParameter(searchParameter.getName())));
        }

        List<Parameter> settingsParameters = searchServiceFactory.getSettingsParameters(queryTargetTypeSettings);

        for (Parameter settingsParameter : settingsParameters) {
            queryTargetTypeSettings.removeParameter(settingsParameter);
        }
    }


    public QueryTargetTypeSettings getSourceByIdWithoutSettings(String id) {
        QueryTargetTypeSettings queryTargetTypeSettings = mongoService.getObject(id, COLLECTION, QueryTargetTypeSettings.class);
        addEmptyParameters(queryTargetTypeSettings);
        return queryTargetTypeSettings;
    }

    public QueryTargetTypeSettings getSourceById(String id) {
        QueryTargetTypeSettings queryTargetTypeSettings = mongoService.getObject(id, COLLECTION, QueryTargetTypeSettings.class);
        return queryTargetTypeSettings;
    }

    public void save(QueryTargetTypeSettings queryTargetTypeSettings) {
        mongoService.save(queryTargetTypeSettings, queryTargetTypeSettings.getId(), COLLECTION);
    }

    public void remove(QueryTargetTypeSettings queryTargetTypeSettings) {
        mongoService.remove(queryTargetTypeSettings.getId(), COLLECTION);
    }

    public void remove(String id) {
        mongoService.remove(id, COLLECTION);
    }


}
