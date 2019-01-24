package com.theopus.parser.obj;

import com.theopus.entity.schedule.Group;
import com.theopus.parser.exceptions.IllegalParserFileException;

import java.util.regex.Matcher;

public class GroupSheet extends Sheet<Group> {
    @Override
    public Group parseAnchor() {
        Matcher matcher = anchorPattern.matcher(content);
        matcher.region(0, content.indexOf(tableBound));
        if (matcher.find()) {
            return new Group(matcher.group(2));
        }
        throw new IllegalParserFileException("Cannot parse anchor from sheet " + this);
    }

}
