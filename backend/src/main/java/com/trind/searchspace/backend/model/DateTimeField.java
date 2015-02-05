package com.trind.searchspace.backend.model;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.function.Function;

/**
 * Created by Joachim on 2014-10-23.
 */
public class DateTimeField {
    String field;
    DateTimeFieldType dateTimeFieldType;

    public DateTimeField(String field, DateTimeFieldType dateTimeFieldType) {
        this.field = field;
        this.dateTimeFieldType = dateTimeFieldType;
    }

    public String getField() {
        return field;
    }

    public DateTimeFieldType getDateTimeFieldType() {
        return dateTimeFieldType;
    }

    public enum DateTimeFieldType {
        MILLIS((p) -> {
            return p.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000;
        });

        private final Function<LocalDateTime, ?> function;

        DateTimeFieldType(Function<LocalDateTime, ?> function) {
            this.function = function;
        }

        public String getAsString(LocalDateTime localDateTime) {
            return function.apply(localDateTime).toString();
        }


    }


}
