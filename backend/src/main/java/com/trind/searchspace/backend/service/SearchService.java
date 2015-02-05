package  com.trind.searchspace.backend.service;

import com.trind.searchspace.backend.model.DateTimeField;
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

    QueryTargetEnum getQueryTarget();

    HistogramQueryResult search(QueryTargetTypeSettings abstractQueryTargetTypeSettings,
                                HistogramQuery histogramQuery,
                                List<Filter> filter,
                                DateTimeField timeFiled,
                                LocalDateTime timeFrom,
                                LocalDateTime timeTo);

    TermQueryResult search(QueryTargetTypeSettings abstractQueryTargetTypeSettings,
                           TermQuery termQuery, List<Filter> filter,
                           DateTimeField timeFiled,
                           LocalDateTime timeFrom,
                           LocalDateTime timeTo);

    ListQueryResult search(QueryTargetTypeSettings abstractQueryTargetTypeSettings,
                           ListQuery listQuery,
                           List<Filter> filter,
                           DateTimeField timeFiled,
                           LocalDateTime timeFrom,
                           LocalDateTime timeTo);

    StatQueryResult search(QueryTargetTypeSettings abstractQueryTargetTypeSettings,
                           StatQuery statQuery,
                           List<Filter> filter,
                           DateTimeField timeFiled,
                           LocalDateTime timeFrom,
                           LocalDateTime timeTo);

    List<String> autoComplete(QueryTargetTypeSettings abstractQueryTargetTypeSettings,
                              QueryTargetType queryTargetType,
                              String field, String value);

    List<String> list(QueryTargetTypeSettings abstractQueryTargetTypeSettings,
                      QueryTargetType queryTargetType, String field);
}
