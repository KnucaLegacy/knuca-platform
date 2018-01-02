package com.theopus.parser.obj.roomdate;

import com.google.common.collect.Sets;
import com.theopus.entity.schedule.Circumstance;
import com.theopus.entity.schedule.Curriculum;
import com.theopus.entity.schedule.Room;
import com.theopus.parser.obj.line.LessonLine;
import com.theopus.parser.obj.sheets.DaySheet;
import com.theopus.parser.obj.sheets.Sheet;
import org.junit.Test;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class LessonLineCircumstancesParserTest {

    private String single_date_single_room_one_bracket = "[11.09 ауд.302] АРХ-14а, АРХ-14б,\n" +
            "АРХ-16, АРХ-17, АРХ-18 зав.каф. Дикарева Л.Ю.";
    private String twobrackets_4_dates_1_room___1_date_1_room = "[7.11, 14.11,\n" +
            "28.11, 5.12 ауд.368]; [12.12 ауд.366] ст.викл. Гончаренко Т.А.,\n" +
            "доц. Лисицин О.Б.";

    private String two_brackets_1room__2singledate_1room = "[ ауд.358] [12.10, 19.10 ауд.468]";

    private String two_brackets_2singledate_1room__1room = "[12.10, 19.10 ауд.468] [ ауд.358];";

    private String four_brackets_1room__2dates_1room_1room = "[ ауд.358] [12.10, 19.10 ауд.468] [ ауд.351] [ ауд.222];";

    private String four_brackets_1room__2dates__1room__1room_1SinlgeDate = "[ ауд.358] [12.10, 19.10 ауд.468] [ ауд.351] [1.04 ауд.222];";

    //manifesto to one brackets pair belongs only one room or not belongs at all;

    private RoomDateBrackets parser = RoomDateBrackets
            .create()
            .defaultPatterns()
            .build()
            .parent(new LineMock(new DaySheetMock(new SheetMock(2017))));


    @Test
    public void parseCircumstaces_four_brackets_1room__2dates__1room__1room_1SinlgeDate() throws Exception {
        RoomDateBrackets rdb = parser.prepare(four_brackets_1room__2dates__1room__1room_1SinlgeDate, null);
        HashSet<Circumstance> expected = Sets.newHashSet(
                new Circumstance(null, new Room("ауд.358"),
                        Sets.newHashSet(
                                LocalDate.of(2017, 10, 12),
                                LocalDate.of(2017, 10, 19)
                        )),
                new Circumstance(null, new Room("ауд.468"),
                        Sets.newHashSet(
                                LocalDate.of(2017, 10, 12),
                                LocalDate.of(2017, 10, 19)
                        )),
                new Circumstance(null, new Room("ауд.351"),
                        Sets.newHashSet(
                                LocalDate.of(2017, 4, 1)
                        )),
                new Circumstance(null, new Room("ауд.222"),
                        Sets.newHashSet(
                                LocalDate.of(2017, 4, 1)
                        )));

        Set<Circumstance> actual = rdb.parseBrackets();
        assertEquals(expected, actual);
    }

    @Test
    public void parseCircumstances_four_brackets_1room__2dates_1room_1room() throws Exception {
        RoomDateBrackets rdb = parser.prepare(four_brackets_1room__2dates_1room_1room, null);
        HashSet<Circumstance> expected = Sets.newHashSet(
                new Circumstance(null, new Room("ауд.358"),
                        Sets.newHashSet(
                                LocalDate.of(2017, 10, 12),
                                LocalDate.of(2017, 10, 19)
                        )),
                new Circumstance(null, new Room("ауд.468"),
                        Sets.newHashSet(
                                LocalDate.of(2017, 10, 12),
                                LocalDate.of(2017, 10, 19)
                        )),
                new Circumstance(null, new Room("ауд.351"),
                        Sets.newHashSet(
                                LocalDate.of(2017, 10, 12),
                                LocalDate.of(2017, 10, 19)
                        )),
                new Circumstance(null, new Room("ауд.222"),
                        Sets.newHashSet(
                                LocalDate.of(2017, 10, 12),
                                LocalDate.of(2017, 10, 19)
                        )));

        Set<Circumstance> actual = rdb.parseBrackets();
        assertEquals(expected, actual);

    }

    @Test
    public void parseCircumstances_single_date_single_room_one_bracket() throws Exception {

    }

    @Test
    public void parse_twobrackets_4_dates_1_room___1_date_1_room() throws Exception {
        RoomDateBrackets rdb = parser.prepare(twobrackets_4_dates_1_room___1_date_1_room, null);
        HashSet<Circumstance> expected = Sets.newHashSet(
                new Circumstance(null, new Room("ауд.368"),
                        Sets.newHashSet(
                                LocalDate.of(2017, 11, 7),
                                LocalDate.of(2017, 11, 14),
                                LocalDate.of(2017, 11, 28),
                                LocalDate.of(2017, 12, 5)
                        )),
                new Circumstance(null, new Room("ауд.366"),
                        Sets.newHashSet(
                                LocalDate.of(2017, 12, 12)
                        )));

        Set<Circumstance> actual = rdb.parseBrackets();
        assertEquals(expected, actual);
    }


    @Test
    public void parseCircumstaces_two_brackets_1room__2singledate_1room() throws Exception {
        RoomDateBrackets rdb = parser.prepare(two_brackets_1room__2singledate_1room, null);
        HashSet<Circumstance> expected = Sets.newHashSet(
                new Circumstance(null, new Room("ауд.358"),
                        Sets.newHashSet(
                                LocalDate.of(2017, 10, 12),
                                LocalDate.of(2017, 10, 19)
                        )),
                new Circumstance(null, new Room("ауд.468"),
                        Sets.newHashSet(
                                LocalDate.of(2017, 10, 12),
                                LocalDate.of(2017, 10, 19)
                        )));

        Set<Circumstance> actual = rdb.parseBrackets();
        assertEquals(expected, actual);

    }

    @Test
    public void parseCircumstaces_two_brackets_2singledate_1room__1room() throws Exception {
        RoomDateBrackets rdb = parser.prepare(two_brackets_2singledate_1room__1room, null);
        HashSet<Circumstance> expected = Sets.newHashSet(
                new Circumstance(null, new Room("ауд.358"),
                        Sets.newHashSet(
                                LocalDate.of(2017, 10, 12),
                                LocalDate.of(2017, 10, 19)
                        )),
                new Circumstance(null, new Room("ауд.468"),
                        Sets.newHashSet(
                                LocalDate.of(2017, 10, 12),
                                LocalDate.of(2017, 10, 19)
                        )));

        Set<Circumstance> actual = rdb.parseBrackets();
        assertEquals(expected, actual);
    }
}

class LineMock extends LessonLine {

    public LineMock(DaySheet sheet) {
        this.parent = sheet;
    }

    @Override
    public List<Curriculum> parse() {
        return null;
    }
}

class DaySheetMock extends DaySheet {
    public DaySheetMock(Sheet sheet) {
        this.parent = sheet;
    }
}


class SheetMock extends Sheet {

    public SheetMock(Integer year) {
        this.sheetYear = year;
    }

    @Override
    public Object parseAnchor() {
        return null;
    }
}

