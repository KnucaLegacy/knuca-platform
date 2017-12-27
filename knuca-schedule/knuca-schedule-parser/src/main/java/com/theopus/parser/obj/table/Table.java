package com.theopus.parser.obj.table;

import com.theopus.entity.schedule.enums.LessonOrder;
import com.theopus.entity.schedule.enums.LessonType;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Map;

public interface Table {

    Table prepare(String content);

    LocalDate getFromBound(DayOfWeek day);

    LocalDate getToBound(DayOfWeek day);

    Map<LocalDate, TableEntry> getScheduleMap();

    class TableEntry {
        private LessonOrder lessonOrder;
        private LessonType lessonType;

        public TableEntry(LessonOrder lessonOrder, LessonType lessonType) {
            this.lessonOrder = lessonOrder;
            this.lessonType = lessonType;
        }

        public LessonOrder getLessonOrder() {
            return lessonOrder;
        }

        public LessonType getLessonType() {
            return lessonType;
        }
    }
}
