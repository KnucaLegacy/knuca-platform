package com.theopus.parser.obj.table;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.theopus.entity.schedule.enums.LessonOrder;
import com.theopus.entity.schedule.enums.LessonType;
import com.theopus.parser.obj.sheets.Sheet;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class SimpleTableTest {

    SimpleTable simpleTable = new SimpleTable()
            .defaultPatternsMap()
            .parent(new Sheet() {
                {
                    this.sheetYear = 2017;
                }
                @Override
                public Object parseAnchor() {
                    return null;
                }
            });
    String file =  new String(Files.readAllBytes(Paths.get("src/test/resources/pdfs/test_file.txt")), "UTF-8");

    String table = "" +
            "======================================================" +
            " pizda" +
            "========================================================================\n" +
            " 6.10|░░░░░░░|░░░░░░░|░░░░░░░|░░░░░░░|ППл....|.......| 6 | - | 1\n" +
            " 9.10|.лЛП...|ЛППП...|ЛЛЛ....|Л.Л....|лП.....|.......| 28 | 1 | 5\n" +
            "===========================================================================";

    public SimpleTableTest() throws IOException {
    }

    @Test
    public void parseDayLocal() throws Exception {
        simpleTable.prepare(table);

        Map<LocalDate, Set<Table.TableEntry>> expected  = new HashMap<>();
        expected.put(LocalDate.of(2017, 10, 6), Sets.newHashSet(
                new Table.TableEntry(LessonOrder.FIRST, LessonType.PRACTICE),
                new Table.TableEntry(LessonOrder.SECOND, LessonType.PRACTICE),
                new Table.TableEntry(LessonOrder.THIRD, LessonType.LAB)
        ));
        expected.put(LocalDate.of(2017, 10, 7), Sets.newHashSet(

        ));
        expected.put(LocalDate.of(2017, 10, 9), Sets.newHashSet(
                new Table.TableEntry(LessonOrder.SECOND, LessonType.LAB),
                new Table.TableEntry(LessonOrder.THIRD, LessonType.LECTURE),
                new Table.TableEntry(LessonOrder.FORUTH, LessonType.PRACTICE)
        ));
        expected.put(LocalDate.of(2017, 10, 10), Sets.newHashSet(
                new Table.TableEntry(LessonOrder.FIRST, LessonType.LECTURE),
                new Table.TableEntry(LessonOrder.SECOND, LessonType.PRACTICE),
                new Table.TableEntry(LessonOrder.THIRD, LessonType.PRACTICE),
                new Table.TableEntry(LessonOrder.FORUTH, LessonType.PRACTICE)
        ));
        expected.put(LocalDate.of(2017, 10, 11), Sets.newHashSet(
                new Table.TableEntry(LessonOrder.FIRST, LessonType.LECTURE),
                new Table.TableEntry(LessonOrder.SECOND, LessonType.LECTURE),
                new Table.TableEntry(LessonOrder.THIRD, LessonType.LECTURE)
        ));
        expected.put(LocalDate.of(2017, 10, 12), Sets.newHashSet(
                new Table.TableEntry(LessonOrder.FIRST, LessonType.LECTURE),
                new Table.TableEntry(LessonOrder.THIRD, LessonType.LECTURE)
        ));
        expected.put(LocalDate.of(2017, 10, 13), Sets.newHashSet(
                new Table.TableEntry(LessonOrder.FIRST, LessonType.LAB),
                new Table.TableEntry(LessonOrder.SECOND, LessonType.PRACTICE)
        ));
        expected.put(LocalDate.of(2017, 10, 14), Sets.newHashSet(

        ));

        Map<LocalDate, Set<Table.TableEntry>> actual = simpleTable.getScheduleMap();

        assertEquals(expected, actual);
    }

    @Test
    public void test() throws Exception {
        simpleTable.prepare(file);
        Map<LocalDate, String> localDateStringMap = simpleTable.parseToDays(simpleTable.parseTable(file));

        LocalDate expectedTo = LocalDate.of(2017, 12,  8);
        LocalDate toBound = simpleTable.getToBound(DayOfWeek.FRIDAY);
        LocalDate expectedFrom = LocalDate.of(2017, 10, 6);
        LocalDate fromBound = simpleTable.getFromBound(DayOfWeek.FRIDAY);

        assertEquals(expectedTo, toBound);
        assertEquals(expectedFrom, fromBound);

    }
}