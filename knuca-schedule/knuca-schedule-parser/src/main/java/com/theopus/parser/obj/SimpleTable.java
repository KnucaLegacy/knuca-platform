package com.theopus.parser.obj;

import com.google.common.collect.Lists;
import com.theopus.entity.schedule.enums.LessonOrder;
import com.theopus.entity.schedule.enums.LessonType;
import com.theopus.parser.exceptions.IllegalPdfException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SimpleTable implements Table {

    private Sheet parent;
    private Pattern pattern = Pattern.compile("={1,40}(.+)={1,40}", Pattern.DOTALL | Pattern.MULTILINE);
    private Pattern equalSpattern = Pattern.compile("={10,}", Pattern.DOTALL | Pattern.MULTILINE);
    private Pattern patternDays = Pattern.compile("\\|([\\\\.А-яІA-z]{1,8})\\|", Pattern.DOTALL | Pattern.MULTILINE);
    private Pattern firstDate = Pattern.compile("\\d?\\d\\.\\d\\d", Pattern.DOTALL | Pattern.MULTILINE);
    private Map<LocalDate, String> daysMap;
    private Map<String, LessonType> lessonTypeMap;

    @Override
    public SimpleTable prepare(String content) {
        String table = parseTable(content);

        this.daysMap = parseToDays(table);
        return this;
    }

    String parseTable(String content) {
        Matcher matcher = pattern.matcher(content);
        Matcher eq = equalSpattern.matcher(content);
        if (eq.find()) {
            if (matcher.find(eq.end())) {
                return matcher.group();
            }
        }
        throw new IllegalPdfException("Not found table in sheet");
    }

    public SimpleTable parent(Sheet sheet) {
        this.parent = sheet;

        return this;
    }

    Map<LocalDate, String> parseToDays(String content) {
        Map<LocalDate, String> map = new LinkedHashMap<>();
        Matcher matcher = patternDays.matcher(content);
        Matcher firstDay = firstDate.matcher(content);

        LocalDate convert;
        if (firstDay.find()) {
            convert = parent.convert(firstDay.group());
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
    public Map<LocalDate, Set<TableEntry>> getScheduleMap() {
        return daysMap.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        v -> parseEntries(v.getValue())
                ));
    }

    private Set<TableEntry> parseEntries(String string) {
        LessonOrder[] values = LessonOrder.values();
        AtomicInteger index = new AtomicInteger(1);
        return Lists.charactersOf(string).stream()
                .map(character -> new TableEntry(
                                values[index.getAndIncrement()], Optional.ofNullable(lessonTypeMap.get(character.toString()))
                                .orElse(LessonType.NONE)
                        )
                )
                .filter(tableEntry -> !tableEntry.getLessonType().equals(LessonType.NONE))
                .collect(Collectors.toSet());
    }

    public SimpleTable patternsMap(Map<String, LessonType> lessonTypeMap) {
        this.lessonTypeMap = lessonTypeMap;
        return this;
    }

    public SimpleTable defaultPatternsMap() {
        this.lessonTypeMap = new HashMap<>();
        lessonTypeMap.put("Л", LessonType.LECTURE);
        lessonTypeMap.put("К", LessonType.CONSULTATION);
        lessonTypeMap.put("С", LessonType.INDIVIDUAL);
        lessonTypeMap.put("л", LessonType.LAB);
        lessonTypeMap.put("П", LessonType.PRACTICE);
        lessonTypeMap.put("Ф", LessonType.FACULTY);
        lessonTypeMap.put("І", LessonType.UNIDENTIFIED);
        lessonTypeMap.put("Н", LessonType.UNIDENTIFIED);
        lessonTypeMap.put("Е", LessonType.EXAM);
        return this;
    }

    @Override
    public Map<LocalDate, String> getDaysMap() {
        return daysMap;
    }

    public Sheet getParent() {
        return parent;
    }
}
