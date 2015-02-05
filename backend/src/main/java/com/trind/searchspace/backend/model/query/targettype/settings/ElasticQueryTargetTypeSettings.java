package com.trind.searchspace.backend.model.query.targettype.settings;

import com.trind.searchspace.backend.model.query.targettype.QueryTargetEnum;

/**
 * Created by Joachim on 2014-10-25.
 */
public class ElasticQueryTargetTypeSettings extends AbstractQueryTargetTypeSettings {

    String clusterName;
    String address;

    @Override
    public QueryTargetEnum getQueryTarget() {
        return QueryTargetEnum.ELASTICSEARCH;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ElasticQueryTargetTypeSettings that = (ElasticQueryTargetTypeSettings) o;

        if (address != null ? !address.equals(that.address) : that.address != null) return false;
        if (clusterName != null ? !clusterName.equals(that.clusterName) : that.clusterName != null) return false;
        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = clusterName != null ? clusterName.hashCode() : 0;
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (getId() != null ? getId().hashCode() : 0);
        return result;
    }
}
