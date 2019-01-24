package com.theopus.parser.obj.ident;

import com.theopus.entity.schedule.Group;
import com.theopus.parser.exceptions.IllegalParserFileException;
import com.theopus.parser.obj.Patterns;

import java.util.regex.Matcher;

public class IdentifierGroupSheet extends IdentifierSheet<Group> {

    public IdentifierGroupSheet() {
        super(Patterns.Sheet.TABLE_BOUND, Patterns.Sheet.EXACT_GROUP_PATTERN);
    }

    public IdentifierGroupSheet(String tableBound, String anchorPattern) {
        super(tableBound, anchorPattern);
    }

    @Override
    public Group parseAnchor() throws IllegalParserFileException {
        Matcher matcher = anchorPattern.matcher(content);
        matcher.region(0, content.indexOf(tableBound));
        if (matcher.find()) {
            return new Group(matcher.group(2));
        }
        throw new IllegalParserFileException("Cannot parse anchor from sheet " + this);
    }
}
