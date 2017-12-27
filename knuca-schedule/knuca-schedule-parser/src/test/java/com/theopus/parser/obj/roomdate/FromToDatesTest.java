package com.theopus.parser.obj.roomdate;

import com.google.common.collect.Sets;
import com.theopus.entity.schedule.Circumstance;
import com.theopus.entity.schedule.Curriculum;
import com.theopus.entity.schedule.Group;
import com.theopus.entity.schedule.Room;
import com.theopus.parser.obj.line.LessonLine;
import com.theopus.parser.obj.sheets.DaySheet;
import com.theopus.parser.obj.sheets.Sheet;
import com.theopus.parser.obj.table.SimpleTable;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class FromToDatesTest {

    String from_to_no_room = "[з 1.11 до 29.11]";
    String from_to_no_room_ws = "[з 1.11 до 29.11(Ч/Т)]";
    String toOnly = "[до 24.11]";
    String fromOnly = "[з 6.10]";
    String file = new String(Files.readAllBytes(Paths.get("src/test/resources/pdfs/test_file.txt")), "UTF-8");

    RoomDateBrackets roomDateBrackets = RoomDateBrackets
            .create()
            .defaultPatterns()
            .build();

    public FromToDatesTest() throws IOException {
    }

    @Test
    public void parseOnlyFrom() throws Exception {
        Set<Circumstance> expected = Sets.newHashSet(
                new Circumstance(null, new Room(Room.NO_AUDITORY),
                        Sets.newHashSet(
                                LocalDate.of(2017, 10, 6),
                                LocalDate.of(2017, 10, 13),
                                LocalDate.of(2017, 10, 20),
                                LocalDate.of(2017, 10, 27),
                                LocalDate.of(2017, 11, 3),
                                LocalDate.of(2017, 11, 10),
                                LocalDate.of(2017, 11, 17),
                                LocalDate.of(2017, 11, 24)
                        ))
        );
        Set<Circumstance> actual = roomDateBrackets
                .parent(new MockLessonLine().parent(new MockDaySheet(DayOfWeek.FRIDAY).parent(new MockSheet(file))))
                .prepare(toOnly, null)
                .parseBrackets();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void parseOnlyTo() throws Exception {
        Set<Circumstance> expected = Sets.newHashSet(
                new Circumstance(null, new Room(Room.NO_AUDITORY),
                        Sets.newHashSet(
                                LocalDate.of(2017, 10, 6),
                                LocalDate.of(2017, 10, 13),
                                LocalDate.of(2017, 10, 20),
                                LocalDate.of(2017, 10, 27),
                                LocalDate.of(2017, 11, 3),
                                LocalDate.of(2017, 11, 10),
                                LocalDate.of(2017, 11, 17),
                                LocalDate.of(2017, 11, 24),
                                LocalDate.of(2017, 12, 1),
                                LocalDate.of(2017, 12, 8)
                        ))
        );
        Set<Circumstance> actual = roomDateBrackets
                .parent(new MockLessonLine().parent(new MockDaySheet(DayOfWeek.FRIDAY).parent(new MockSheet(file))))
                .prepare(fromOnly, null)
                .parseBrackets();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void parseFromTo() throws Exception {
        Set<Circumstance> expected = Sets.newHashSet(
                new Circumstance(null, new Room(Room.NO_AUDITORY),
                        Sets.newHashSet(
                                LocalDate.of(2017, 11, 1),
                                LocalDate.of(2017, 11, 8),
                                LocalDate.of(2017, 11, 15),
                                LocalDate.of(2017, 11, 22),
                                LocalDate.of(2017, 11, 29)
                        ))
        );

        Set<Circumstance> actual = roomDateBrackets
                .parent(new MockLessonLine())
                .prepare(from_to_no_room, null)
                .parseBrackets();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void parseFromTo_WS() throws Exception {
        Set<Circumstance> expected = Sets.newHashSet(
                new Circumstance(null, new Room(Room.NO_AUDITORY),
                        Sets.newHashSet(
                                LocalDate.of(2017, 11, 1),
                                LocalDate.of(2017, 11, 15),
                                LocalDate.of(2017, 11, 29)
                        ))
        );

        Set<Circumstance> actual = roomDateBrackets
                .parent(new MockLessonLine())
                .prepare(from_to_no_room_ws, null)
                .parseBrackets();

        Assert.assertEquals(expected, actual);
    }
}


class MockLessonLine extends LessonLine {

    @Override
    public List<Curriculum> parse() {
        return null;
    }
}

class MockDaySheet extends DaySheet {


    public MockDaySheet(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
}

class MockSheet extends Sheet<Group> {

    public MockSheet(String sheet) {
        this.table = new SimpleTable();
        table.prepare(sheet);
    }

    @Override
    public Group parseAnchor() {
        return null;
    }
}
