package com.theopus.parser.obj.sheets;

import com.theopus.entity.schedule.Curriculum;
import com.theopus.entity.schedule.enums.LessonOrder;
import com.theopus.parser.obj.line.LessonLine;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public class DaySheet<T> {

    private DayOfWeek dayOfWeek;
    private String content;

    private Sheet<T> parent;
    private LessonLine child;

    public List<Curriculum> parse() {
        List<Curriculum> curriculums = new ArrayList<>();

        LessonOrder lessonOrder = null;
        for (String line : splitToLines()) {
            List<Curriculum> parse = child.prepare(line, lessonOrder).parse();
            curriculums.addAll(parse);
            lessonOrder = child.parseOrder();
        }
        return curriculums;
    }

    public DaySheet<T> prepare(DayOfWeek dayOfWeek, String content) {
        this.dayOfWeek = dayOfWeek;
        this.content = content;
        return this;
    }

    List<String> splitToLines() {
        return new ArrayList<>();
    }

    public Sheet<T> getParent() {
        return parent;
    }

    public DaySheet<T> child(LessonLine lessonLine) {
        this.child = lessonLine;
        return this;
    }

    public DaySheet<T> parent(Sheet<T> sheet) {
        this.parent = sheet;
        return this;
    }
}
