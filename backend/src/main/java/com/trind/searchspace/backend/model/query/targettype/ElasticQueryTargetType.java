package com.trind.searchspace.backend.model.query.targettype;

/**
 * Created by Joachim on 2014-09-15.
 */
public class ElasticQueryTargetType extends AbstractQueryTargetType {

    String index;
    String type;

    @Override
    public QueryTargetEnum getQueryTarget() {
        return QueryTargetEnum.ELASTICSEARCH;
    }

    public String getIndex() {
        return index;
    }

    public String getType() {
        return type;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ElasticQueryTargetType)) return false;
        if (!super.equals(o)) return false;

        ElasticQueryTargetType that = (ElasticQueryTargetType) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (index != null ? !index.equals(that.index) : that.index != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (index != null ? index.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
