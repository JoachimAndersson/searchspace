package com.trind.searchspace.backend.model.query.targettype.settings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.trind.searchspace.backend.model.query.targettype.QueryTargetEnum;

/**
 * Created by Joachim on 2014-10-25.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "queryTarget")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ElasticQueryTargetTypeSettings.class, name = "ELASTICSEARCH")
})
@JsonIgnoreProperties(ignoreUnknown = true)
public interface QueryTargetTypeSettings {

    QueryTargetEnum getQueryTarget();

    String getId();
}
