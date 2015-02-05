package com.trind.searchspace.backend.model.query;

import javax.xml.bind.annotation.XmlType;

/**
 * Created by Joachim on 2014-09-15.
 */
@XmlType(name = "stat")
public class StatQuery extends AbstractBaseQuery{
    @Override
    public QueryType getQueryType() {
        return QueryType.STAT;
    }
}
