package com.trind.searchspace.backend.model.query;

/**
 * Created by Joachim on 2014-09-15.
 */

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "list")
public class ListQuery extends AbstractBaseQuery {
    @Override
    public QueryType getQueryType() {
        return QueryType.LIST;
    }
}
