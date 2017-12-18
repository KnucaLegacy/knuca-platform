package com.theopus.parser.obj.sheets;

import org.springframework.cglib.core.Local;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

public class DaySheet<T> {

    private DayOfWeek dayOfWeek;
    private String content;

    private LocalDate rangeFrom;
    private LocalDate rangeTo;

    private Sheet<T> parent;


}
