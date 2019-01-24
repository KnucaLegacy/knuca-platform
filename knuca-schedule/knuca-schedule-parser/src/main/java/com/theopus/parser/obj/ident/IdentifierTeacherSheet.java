package com.theopus.parser.obj.ident;

import com.theopus.entity.schedule.Teacher;
import com.theopus.parser.exceptions.IllegalParserFileException;
import com.theopus.parser.obj.Patterns;

import java.util.regex.Matcher;

public class IdentifierTeacherSheet extends IdentifierSheet<Teacher> {

    public IdentifierTeacherSheet() {
        super(Patterns.Sheet.TABLE_BOUND, Patterns.LessonLine.TEACHER);
    }

    public IdentifierTeacherSheet(String tableBound, String pattern) {
        super(tableBound, pattern);
    }

    @Override
    public Teacher parseAnchor() throws IllegalParserFileException {
        Matcher matcher = anchorPattern.matcher(content);
        matcher.region(0, content.indexOf(tableBound));
        if (matcher.find()) {
            return new Teacher(matcher.group(2));
        }
        throw new IllegalParserFileException("Cannot parse anchor from sheet " + this);
    }
}
