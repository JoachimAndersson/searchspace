package com.trind.searchspace.backend.model.query.targettype;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
/**
 * Created by Joachim on 2014-09-15.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "queryTarget")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ElasticQueryTargetType.class, name = "ELASTICSEARCH")
})
@JsonIgnoreProperties(ignoreUnknown = true)
public interface QueryTargetType {

    QueryTargetEnum getQueryTarget();

    String getId();

}
