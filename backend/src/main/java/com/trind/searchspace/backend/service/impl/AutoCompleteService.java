package  com.trind.searchspace.backend.service.impl;

import com.trind.searchspace.backend.factory.SearchServiceFactory;
import com.trind.searchspace.backend.model.query.targettype.QueryTargetType;
import com.trind.searchspace.backend.model.query.targettype.settings.QueryTargetTypeSettings;
import com.trind.searchspace.backend.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Joachim on 2014-09-29.
 */
@Service
public class AutoCompleteService {

    @Autowired
    SourceService sourceService;

    @Autowired
    SearchServiceFactory searchServiceFactory;

    public List<String> autoCompleteQueryTarget(String value, String field, QueryTargetType queryTargetType) {
        QueryTargetTypeSettings sourceById = sourceService.getSourceById(queryTargetType.getId());
        SearchService searchService = searchServiceFactory.getSearchService(queryTargetType);
        List<String> strings = searchService.autoComplete(sourceById, queryTargetType, field, value);
        return strings;
    }

    public List<String> listQueryTargetFieldValues(String field, QueryTargetType queryTargetType) {
        QueryTargetTypeSettings sourceById = sourceService.getSourceById(queryTargetType.getId());
        SearchService searchService = searchServiceFactory.getSearchService(queryTargetType);
        List<String> strings = searchService.list(sourceById, queryTargetType, field);
        return strings;
    }
}
