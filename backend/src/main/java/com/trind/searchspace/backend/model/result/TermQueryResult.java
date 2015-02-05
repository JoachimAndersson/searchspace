package com.trind.searchspace.backend.model.result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joachim on 2014-09-15.
 */
public class TermQueryResult extends AbstractQueryResult {

    List<Term> list = new ArrayList<>();

    public List<Term> getList() {
        return list;
    }

    public void setList(List<Term> list) {
        this.list = list;
    }

    public static class Term {
        public static final String UNKNOWN = "unknown";
        public static final String OTHERVALUE = "other";

        long size;
        String value;

        public Term(String value, long size) {
            this.size = size;
            this.value = value;
        }

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

}


