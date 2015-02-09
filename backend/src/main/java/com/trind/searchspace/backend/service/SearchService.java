package  com.trind.searchspace.backend.service;

import com.trind.searchspace.backend.model.DateTimeField;
import com.trind.searchspace.backend.model.Parameter;
import com.trind.searchspace.backend.model.filter.Filter;
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

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Joachim on 2014-09-15.
 */
public interface SearchService extends Serializable {

    String getQueryTarget();

    List<String> autoComplete(QueryTargetTypeSettings abstractQueryTargetTypeSettings,
                              QueryTargetType queryTargetType,
                              String field, String value);

    List<String> list(QueryTargetTypeSettings abstractQueryTargetTypeSettings,
                      QueryTargetType queryTargetType, String field);

    List<Parameter> getSettingsParameters();

    List<Parameter> getSearchParameters();
}
