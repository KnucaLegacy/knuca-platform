package com.theopus.parser.obj.sheets;

import com.theopus.entity.schedule.Curriculum;
import com.theopus.entity.schedule.enums.LessonOrder;
import com.theopus.parser.ParserUtils;
import com.theopus.parser.obj.Patterns;
import com.theopus.parser.obj.line.LessonLine;
import com.theopus.parser.obj.validator.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DaySheet<T> {

    private static final Logger LOG = LoggerFactory.getLogger(DaySheet.class);
    protected DayOfWeek dayOfWeek;
    private String content;

    protected Sheet<T> parent;
    private LessonLine child;
    private Validator validator;

    private Pattern begin;
    private Pattern finish;

    public DaySheet() {
    }

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
        this.content = ParserUtils.normalize(content);
        return this;
    }

    List<String> splitToLines() {
        Matcher mb = begin.matcher(content);
        Matcher mf = finish.matcher(content);
        List<String> result = new ArrayList<>();
        while (mb.find()) {
            if (mf.find(mb.end())) {
                result.add(ParserUtils.trimWhitespaces(content.substring(mb.start(), mf.start())));
            } else {
                result.add(ParserUtils.trimWhitespaces(content.substring(mb.start())));
            }
        }
        return result;
    }

    public Sheet<T> getParent() {
        return parent;
    }


    public DaySheet<T> parent(LessonLine lessonLine) {
        this.child = lessonLine;
        lessonLine.parent(this);
        return this;
    }

    public DaySheet<T> parent(Sheet<T> sheet) {
        this.parent = sheet;
        return this;
    }

    public static <T> DaySheet<T>.Builder create() {
        return new DaySheet<T>().new Builder();
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public DaySheet<T> child(LessonLine lessonLine) {
        this.child = lessonLine;
        lessonLine.parent(this);
        return this;
    }


    public class Builder {

        public DaySheet<T> build() {
            return DaySheet.this;
        }

        public Builder defaultPatterns() {
            begin = Pattern.compile(Patterns.LessonLine.BEGIN_LESSON_SPLITTER,Pattern.DOTALL | Pattern.MULTILINE);
            finish = Pattern.compile(Patterns.LessonLine.END_LESSON_SPLITTER, Pattern.DOTALL | Pattern.MULTILINE);
            return this;
        }
    }

    @Override
    public String toString() {
        return "DaySheet{" +
                "content='" + content + '\'' +
                '}';
    }
}
