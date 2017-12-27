package com.theopus.parser.obj.table;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Map;

public interface Table {

    Table prepare(String content);

    LocalDate getFromBound(DayOfWeek day);

    LocalDate getToBound(DayOfWeek day);

    Map<LocalDate, Integer> getScheduleMap();

}
