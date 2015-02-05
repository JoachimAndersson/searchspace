package com.trind.searchspace.backend.model.result;

/**
 * Created by Joachim on 2014-09-15.
 */
public class EmptyQueryResult extends AbstractQueryResult {


    @Override
    public boolean equals(Object obj) {
        if (obj.getClass().equals(this.getClass())) {
            return true;
        }
        return super.equals(obj);
    }
}

