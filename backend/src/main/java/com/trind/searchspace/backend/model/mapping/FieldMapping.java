package com.trind.searchspace.backend.model.mapping;

import java.util.List;

/**
 * Created by Joachim on 2014-11-13.
 */
public class FieldMapping {

    String mappingName;
    List<String> fieldName;
    List<QueryTargetTypeWithField> queryTargetTypes;

    public String getMappingName() {
        return mappingName;
    }

    public void setMappingName(String mappingName) {
        this.mappingName = mappingName;
    }

    public List<String> getFieldName() {
        return fieldName;
    }

    public void setFieldName(List<String> fieldName) {
        this.fieldName = fieldName;
    }

    public List<QueryTargetTypeWithField> getQueryTargetTypes() {
        return queryTargetTypes;
    }

    public void setQueryTargetTypes(List<QueryTargetTypeWithField> queryTargetTypes) {
        this.queryTargetTypes = queryTargetTypes;
    }
}
