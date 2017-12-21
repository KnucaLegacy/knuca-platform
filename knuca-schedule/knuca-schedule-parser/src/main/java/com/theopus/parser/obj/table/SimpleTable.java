package com.theopus.parser.obj.table;

import com.theopus.parser.obj.Patterns;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleTable implements Table {

    private String table;


    private static DateTimeFormatter dateTimeFormat = new DateTimeFormatterBuilder()
            .appendPattern("d.MM")
            .parseDefaulting(ChronoField.YEAR, LocalDate.now().getYear())
            .toFormatter();

    public SimpleTable(String content) {
        this.table = extractTable(content);
    }

    private String extractTable(String content) {
        Pattern compile = Pattern.compile(Patterns.Sheet.TABLE_BOUND);
        Matcher matcher = compile.matcher(content);
        String table = null;
        if(matcher.find())
        {
            table = matcher.group(0);
        }
        return table;
    }

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
        LocalDate now = LocalDate.now();
        now.plusDays(1);
        return null;
    }

    private LocalDate convert(String date){
        return LocalDate.parse(date, dateTimeFormat);
    }

}
