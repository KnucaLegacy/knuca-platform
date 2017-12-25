package com.theopus.parser.obj.table;

import com.theopus.parser.obj.sheets.Sheet;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Map;

public class SimpleTable implements Table {

    private Sheet parent;

    @Override
    public LocalDate getFromBound(DayOfWeek day) {
        return null;
    }

    @Override
    public LocalDate getToBound(DayOfWeek day) {
        return null;
    }

    @Override
    public Map<LocalDate, Integer> getScheduleMap() {
        return null;
    }

}
