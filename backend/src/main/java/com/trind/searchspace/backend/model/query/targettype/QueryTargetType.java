package com.trind.searchspace.backend.model.query.targettype;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.trind.searchspace.backend.model.Parameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Joachim on 2014-09-15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryTargetType {

    String id;
    String queryTarget;
    List<Parameter> settings = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQueryTarget() {
        return queryTarget;
    }

    public void setQueryTarget(String queryTarget) {
        this.queryTarget = queryTarget;
    }

    public List<Parameter> getSettings() {
        return settings;
    }

    public void setSettings(List<Parameter> settings) {
        this.settings = settings;
    }

    public String getParameter(String parameter) {

        for (Parameter param : settings) {
            if (param.getName().equalsIgnoreCase(parameter)) {
                return param.getValue();
            }
        }
        return null;
    }

    public void addParameter(Parameter parameter) {
        settings.remove(parameter);
        settings.add(parameter);
    }

    public void addParameter(String parameter, String value) {
        settings.remove(new Parameter(parameter, value));
        settings.add(new Parameter(parameter, value));
    }

    public void removeParameter(Parameter parameter) {
        settings.remove(parameter);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QueryTargetType)) return false;

        QueryTargetType that = (QueryTargetType) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
