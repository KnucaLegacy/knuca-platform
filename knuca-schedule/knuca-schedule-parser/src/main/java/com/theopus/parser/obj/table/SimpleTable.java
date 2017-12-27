package com.theopus.parser.obj.table;

import com.theopus.parser.exceptions.IllegalPdfException;
import com.theopus.parser.obj.sheets.Sheet;
import org.springframework.cglib.core.Local;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleTable implements Table {

    private Sheet parent;
    private Pattern pattern = Pattern.compile("={1,4}(.+)={1,4}", Pattern.DOTALL | Pattern.MULTILINE);
    private Pattern patternDays = Pattern.compile("\\|([\\\\.А-я]{1,8})\\|", Pattern.DOTALL | Pattern.MULTILINE);
    private Pattern firstDate = Pattern.compile("\\d?\\d\\.\\d\\d", Pattern.DOTALL | Pattern.MULTILINE);
    private Map<LocalDate, String> daysMap;

    @Override
    public SimpleTable prepare(String content) {
        this.daysMap = parseToDays(parseTable(content));
        return this;
    }

    String parseTable(String content) {
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group();
        }
        throw new IllegalPdfException("Not found table in sheet");
    }

    Map<LocalDate, String> parseToDays(String content) {
        Map<LocalDate, String> map = new LinkedHashMap<>();
        Matcher matcher = patternDays.matcher(content);
        Matcher firstDay = firstDate.matcher(content);

        LocalDate convert;
        if (firstDay.find()) {
            convert = convert(firstDay.group());
        } else {
            throw new IllegalPdfException("No date at table.");
        }

        while (matcher.find()) {
            String group = matcher.group(1);
            map.put(convert, group);
            if (Pattern.matches("[\\s\\d\\r\\n]", new StringBuilder().append(content.charAt(matcher.end() + 1)))) {
                convert = convert.plusDays(1);
            }
            convert = convert.plusDays(1);
            matcher.region(matcher.end() - 2, content.length());
        }
        return map;
    }

    @Override
    public LocalDate getFromBound(DayOfWeek day) {
        return daysMap.keySet()
                .stream()
                .filter(ld -> ld.getDayOfWeek().equals(day))
                .min(LocalDate::compareTo)
                .orElseThrow(() -> new IllegalPdfException("No min date for current day of Week"));
    }

    @Override
    public LocalDate getToBound(DayOfWeek day) {
        return daysMap.keySet()
                .stream()
                .filter(ld -> ld.getDayOfWeek().equals(day))
                .max(LocalDate::compareTo)
                .orElseThrow(() -> new IllegalPdfException("No max date for current day of Week"));
    }

    @Override
    public Map<LocalDate, TableEntry> getScheduleMap() {
        return null;
    }

    private LocalDate convert(String date) {
        return LocalDate.parse(date, dateTimeFormat);
    }

    private static DateTimeFormatter dateTimeFormat = new DateTimeFormatterBuilder()
            .appendPattern("d.MM")
            .parseDefaulting(ChronoField.YEAR, LocalDate.now().getYear())
            .toFormatter();

}
