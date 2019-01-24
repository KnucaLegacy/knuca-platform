package com.theopus.parser.obj.ident;

import com.theopus.entity.schedule.Room;
import com.theopus.parser.exceptions.IllegalParserFileException;
import com.theopus.parser.obj.Patterns;

import java.util.regex.Matcher;

public class IdentifierRoomSheet extends IdentifierSheet<Room> {

    public IdentifierRoomSheet() {
        super(Patterns.Sheet.TABLE_BOUND, Patterns.RoomDates.AUDITORY);
    }

    public IdentifierRoomSheet(String tableBound, String pattern) {
        super(tableBound, pattern);
    }

    @Override
    public Room parseAnchor() throws IllegalParserFileException {
        Matcher matcher = anchorPattern.matcher(content);
        matcher.region(0, content.indexOf(tableBound));
        if (matcher.find()) {
            return new Room(matcher.group(2));
        }
        throw new IllegalParserFileException("Cannot parse anchor from sheet " + this);
    }
}
