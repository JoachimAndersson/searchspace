package com.trind.searchspace.backend.model.query.targettype;

/**
 * Created by Joachim on 2014-09-15.
 */
public abstract class AbstractQueryTargetType implements QueryTargetType {

    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractQueryTargetType)) return false;

        AbstractQueryTargetType that = (AbstractQueryTargetType) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
