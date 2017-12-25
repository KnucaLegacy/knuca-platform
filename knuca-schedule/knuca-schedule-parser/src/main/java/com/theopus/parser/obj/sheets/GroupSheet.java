package com.theopus.parser.obj.sheets;

import com.theopus.entity.schedule.Group;
import com.theopus.parser.exceptions.IllegalPdfException;

import java.util.regex.Matcher;

public class GroupSheet extends Sheet<Group> {
    @Override
    Group parseAnchor() {
        Matcher matcher = anchorPattern.matcher(content);
        matcher.region(0, content.indexOf(tableBound));
        if (matcher.find()) {
            return new Group(matcher.group(2));
        }
        throw new IllegalPdfException("Cannot parse anchor from sheet " + this);
    }

}
