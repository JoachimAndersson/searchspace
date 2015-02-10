package com.trind.searchspace.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Joachim on 2015-02-09.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Parameter {
    String name;
    GuiTypes guiType;
    String value;

    public Parameter() {
    }

    public Parameter(String name, GuiTypes guiType, String value) {
        this.name = name;
        this.guiType = guiType;
        this.value = value;
    }

    public Parameter(String name) {
        this.name = name;
    }

    public Parameter(String name, GuiTypes guiTypes) {
        this.name = name;
        this.guiType = guiTypes;
    }

    public Parameter(String name, String value) {
        this.name = name;
        this.value = value;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GuiTypes getGuiType() {
        return guiType;
    }

    public void setGuiType(GuiTypes guiType) {
        this.guiType = guiType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Parameter)) return false;

        Parameter parameter = (Parameter) o;

        if (name != null ? !name.equals(parameter.name) : parameter.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
