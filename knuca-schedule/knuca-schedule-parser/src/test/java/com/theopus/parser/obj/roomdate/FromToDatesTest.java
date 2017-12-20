package com.theopus.parser.obj.roomdate;

import com.google.common.collect.Sets;
import com.theopus.entity.schedule.Circumstance;
import com.theopus.entity.schedule.Curriculum;
import com.theopus.entity.schedule.Group;
import com.theopus.entity.schedule.Room;
import com.theopus.parser.obj.line.LessonLine;
import com.theopus.parser.obj.sheets.DaySheet;
import org.junit.Assert;
import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class FromToDatesTest {

    String from_to_no_room = "[з 1.11 до 29.11]";
    String from_to_no_room_ws = "[з 13.10 до 24.11(Ч/Т)]";


    RoomDateBrackets roomDateBrackets = RoomDateBrackets
            .create()
            .defaultPatterns()
            .build();

    @Test
    public void parseFromTo() throws Exception {
        Set<Circumstance> expected = Sets.newHashSet(
                new Circumstance(null, new Room(Room.NO_AUDITORY),
                        Sets.newHashSet(
                                LocalDate.of(2017,11,1),
                                LocalDate.of(2017,11,8),
                                LocalDate.of(2017,11,15),
                                LocalDate.of(2017,11,22),
                                LocalDate.of(2017,11,29)
                        ))
        );

        Set<Circumstance> actual = roomDateBrackets
                .parent(new MockLessonLine().parent(new MockDaySheet(DayOfWeek.WEDNESDAY)))
                .prepare(from_to_no_room, null)
                .parseBrackets();

        Assert.assertEquals(expected,actual);
    }

    @Test
    public void parseFromTo_WS() throws Exception {
        Set<Circumstance> expected = Sets.newHashSet(
                new Circumstance(null, new Room(Room.NO_AUDITORY),
                        Sets.newHashSet(
                                LocalDate.of(2017,11,1),
                                LocalDate.of(2017,11,15),
                                LocalDate.of(2017,11,29)
                        ))
        );

        Set<Circumstance> actual = roomDateBrackets
                .parent(new MockLessonLine().parent(new MockDaySheet(DayOfWeek.WEDNESDAY)))
                .prepare(from_to_no_room_ws, null)
                .parseBrackets();

        Assert.assertEquals(expected,actual);
    }
}


class MockLessonLine extends LessonLine {

    @Override
    public List<Curriculum> parse() {
        return null;
    }
}

class MockDaySheet extends DaySheet{

    public DaySheet sheet = new DaySheet();

    public MockDaySheet(DayOfWeek dayOfWeek) {
        sheet.prepare(dayOfWeek, "null");
    }
}
