package com.trind.searchspace.backend.model;

/**
 * Created by Joachim on 2015-02-09.
 */
public class Parameter {
    String name;
    GuiTypes guiTypes;
    String value;

    public Parameter(String name, GuiTypes guiTypes, String value) {
        this.name = name;
        this.guiTypes = guiTypes;
        this.value = value;
    }

    public Parameter(String name) {
        this.name = name;
    }

    public Parameter(String name, GuiTypes guiTypes) {
        this.name = name;
        this.guiTypes = guiTypes;
    }

    public Parameter(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public Parameter() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GuiTypes getGuiTypes() {
        return guiTypes;
    }

    public void setGuiTypes(GuiTypes guiTypes) {
        this.guiTypes = guiTypes;
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
