package com.theopus.parser.obj.sheets;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class DaySheet<T> {

    private DayOfWeek dayOfWeek;
    private String content;

    private LocalDate rangeFrom;
    private LocalDate rangeTo;

    private Sheet<T> parent;

    public Sheet<T> getParent() {
        return parent;
    }
}
