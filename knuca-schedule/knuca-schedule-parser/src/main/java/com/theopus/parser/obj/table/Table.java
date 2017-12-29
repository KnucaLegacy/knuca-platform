package com.theopus.parser.obj.table;

import com.theopus.entity.schedule.enums.LessonOrder;
import com.theopus.entity.schedule.enums.LessonType;
import com.theopus.parser.obj.Patterns;
import com.theopus.parser.obj.sheets.Sheet;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public interface Table {

    Table prepare(String content);

    LocalDate getFromBound(DayOfWeek day);

    LocalDate getToBound(DayOfWeek day);

    Map<LocalDate, Set<TableEntry>> getScheduleMap();

    Map<LocalDate, String> getDaysMap();

    Sheet getParent();

    Table parent(Sheet sheet);

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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TableEntry that = (TableEntry) o;
            if (!lessonOrder.equals(lessonOrder)) return false;
            if (lessonType == LessonType.UNIDENTIFIED) return true;
            if (that.lessonType == LessonType.UNIDENTIFIED) return true;
            return lessonType == that.lessonType;
        }

        @Override
        public int hashCode() {
            return Objects.hash(lessonOrder);
        }

        @Override
        public String toString() {
            return "TableEntry{" +
                    "lessonOrder=" + lessonOrder +
                    ", lessonType=" + lessonType +
                    '}';
        }
    }
}
