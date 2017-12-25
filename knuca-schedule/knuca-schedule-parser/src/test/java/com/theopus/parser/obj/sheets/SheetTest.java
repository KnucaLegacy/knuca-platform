package com.theopus.parser.obj.sheets;

import com.theopus.entity.schedule.Group;
import com.theopus.parser.obj.Patterns;
import org.junit.Test;

import java.time.DayOfWeek;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class SheetTest {
    String sheetString = "< КТОУП > 8 вересня 2017\n" +
            " академгрупа АРХ-11а\n" +
            "===========================================================================\n" +
            "Тиждень|Понедiл|Вiвторо|Середа |Четвер |П'ятниц|Субота | Сума |Вiкна | Днi\n" +
            "===========================================================================\n" +
            " 8.09|░░░░░░░|░░░░░░░|░░░░░░░|░░░░░░░|ПП.....|.......| 4 | - | 1\n" +
            " 11.09|ППЛЛЛ..|ЛЛ.Л...|ЛППП...|.......|ПП.....|.......| 28 | 1 | 4\n" +
            " 18.09|ППЛЛЛ..|ЛЛПЛ...|ЛППП...|.......|ПППП...|.......| 34 | - | 4\n" +
            " 25.09|ППЛЛЛ..|ЛЛПЛ...|ЛППП...|.......|ПППП...|.......| 34 | - | 4\n" +
            " 2.10|ППЛЛЛ..|ЛЛПЛ...|ЛППП...|.......|░░░░░░░|░░░░░░░| 26 | - | 3\n" +
            " Всього 126 | 1 | 16\n" +
            "===========================================================================\n" +
            " Понедiлок\n" +
            "---------------------------------------------------------------------------\n" +
            "9:00 Образотворче мистецтво (Практ.з.); [ ауд.302<A>] доц. Коломiєць Ю.Д.\n" +
            "10:30 Образотворче мистецтво (Практ.з.); [ ауд.302<A>] доц. Коломiєць Ю.Д.\n" +
            "12:20 Дiлова українська мова (Лекцiї); [11.09 ауд.302] АРХ-11б, АРХ-12а,\n" +
            " АРХ-12б, АРХ-13а, АРХ-13б зав.каф. Дикарева Л.Ю.\n" +
            "--\"-- Iсторiя української державностi i культу (Лекцiї); [з 18.09 ауд.302]\n" +
            " АРХ-11б, АРХ-12а, АРХ-12б, АРХ-13а, АРХ-13б, ДН-11, ДН-12, ДН-13\n" +
            " проф. Деревiнський В.Ф.\n" +
            "13:50 Вища математика (Лекцiї); [ ауд.302] АРХ-11б, АРХ-12а, АРХ-12б,\n" +
            " АРХ-13а, АРХ-13б доц. Турчанiнова Л.I.\n" +
            "15:20 Iсторiя архiтектури (Лекцiї); [ ауд.101<A>] АРХ-11б, АРХ-12а, АРХ-12б,\n" +
            " АРХ-13а, АРХ-13б доц. Горбик 0.0.\n" +
            "---------------------------------------------------------------------------\n" +
            " Вiвторок\n" +
            "---------------------------------------------------------------------------\n" +
            "9:00 Матерiалознавство (Лекцiї); [ ауд.302] АРХ-11б, АРХ-12а, АРХ-12б,\n" +
            " АРХ-13а, АРХ-13б доц. Кочевих М.О.\n" +
            "10:30 Нарисна геометрiя (Лекцiї); [ ауд.302] АРХ-11б, АРХ-12а, АРХ-12б,\n" +
            " АРХ-13а, АРХ-13б проф. Михайленко В.Є., доц. Цой М.П.\n" +
            "12:20 Дiлова українська мова (Практ.з.); [з 19.09 ауд.107<A>]\n" +
            " зав.каф. Дикарева Л.Ю.\n" +
            "13:50 Теорiя архiтектури АП (Лекцiї); [ ауд.101<A>] АРХ-11б, АРХ-12а,\n" +
            " АРХ-12б, АРХ-13а, АРХ-13б доц. Щербаков О.В.\n" +
            "---------------------------------------------------------------------------\n" +
            " Середа\n" +
            "---------------------------------------------------------------------------\n" +
            "9:00 Композицiя (Лекцiї); [ ауд.101<A>] АРХ-11б, АРХ-12а, АРХ-12б, АРХ-13а,\n" +
            " АРХ-13б проф. Зимiна С.Б.\n" +
            "10:30 Архiтектурне проектування (Практ.з.); [ ауд.107<A>] ас. Козятник I.П.,\n" +
            " ас. Сингаевська М.А., доц. Василенко Л.Г.\n" +
            "12:20 Архiтектурне проектування (Практ.з.); [ ауд.107<A>] ас. Козятник I.П.,\n" +
            " ас. Сингаевська М.А., доц. Василенко Л.Г.\n" +
            "13:50 фiзвиховання (Практ.з.); []\n" +
            "---------------------------------------------------------------------------\n" +
            " П'ятниця\n" +
            "---------------------------------------------------------------------------\n" +
            "9:00 Архiтектурне проектування (Практ.з.); [ ауд.107<A>] ас. Козятник I.П.,\n" +
            " ас. Сингаевська М.А., доц. Василенко Л.Г.\n" +
            "10:30 Архiтектурне проектування (Практ.з.); [ ауд.107<A>] ас. Козятник I.П.,\n" +
            " ас. Сингаевська М.А., доц. Василенко Л.Г.\n" +
            "12:20 Вища математика (Практ.з.); [22.09, 29.09 ауд.107<A>]\n" +
            " доц. Турчанiнова Л.I.\n" +
            "13:50 Нарисна геометрiя (Практ.з.); [22.09, 29.09 ауд.107<A>]\n" +
            " доц. Mоcтовенко О.В.\n" +
            "---------------------------------------------------------------------------\n";

    private Sheet<Group> sheet = Sheet
            .<Group>createGroupSheet()
            .anchorPattern(Patterns.Sheet.EXACT_GROUP_PATTERN)
            .deafultTableBound()
            .defaultPatterns()
            .build();

    @Test
    public void splitToDays() throws Exception {

        Map<DayOfWeek, String> expected = new LinkedHashMap<>();

        expected.put(DayOfWeek.MONDAY,
                        "9:00 Образотворче мистецтво (Практ.з.); [ ауд.302<A>] доц. Коломiєць Ю.Д.\n" +
                        "10:30 Образотворче мистецтво (Практ.з.); [ ауд.302<A>] доц. Коломiєць Ю.Д.\n" +
                        "12:20 Дiлова українська мова (Лекцiї); [11.09 ауд.302] АРХ-11б, АРХ-12а,\n" +
                        " АРХ-12б, АРХ-13а, АРХ-13б зав.каф. Дикарева Л.Ю.\n" +
                        "--\"-- Iсторiя української державностi i культу (Лекцiї); [з 18.09 ауд.302]\n" +
                        " АРХ-11б, АРХ-12а, АРХ-12б, АРХ-13а, АРХ-13б, ДН-11, ДН-12, ДН-13\n" +
                        " проф. Деревiнський В.Ф.\n" +
                        "13:50 Вища математика (Лекцiї); [ ауд.302] АРХ-11б, АРХ-12а, АРХ-12б,\n" +
                        " АРХ-13а, АРХ-13б доц. Турчанiнова Л.I.\n" +
                        "15:20 Iсторiя архiтектури (Лекцiї); [ ауд.101<A>] АРХ-11б, АРХ-12а, АРХ-12б,\n" +
                        " АРХ-13а, АРХ-13б доц. Горбик 0.0.");
        expected.put(DayOfWeek.TUESDAY,
                        "9:00 Матерiалознавство (Лекцiї); [ ауд.302] АРХ-11б, АРХ-12а, АРХ-12б,\n" +
                        " АРХ-13а, АРХ-13б доц. Кочевих М.О.\n" +
                        "10:30 Нарисна геометрiя (Лекцiї); [ ауд.302] АРХ-11б, АРХ-12а, АРХ-12б,\n" +
                        " АРХ-13а, АРХ-13б проф. Михайленко В.Є., доц. Цой М.П.\n" +
                        "12:20 Дiлова українська мова (Практ.з.); [з 19.09 ауд.107<A>]\n" +
                        " зав.каф. Дикарева Л.Ю.\n" +
                        "13:50 Теорiя архiтектури АП (Лекцiї); [ ауд.101<A>] АРХ-11б, АРХ-12а,\n" +
                        " АРХ-12б, АРХ-13а, АРХ-13б доц. Щербаков О.В.");
        expected.put(DayOfWeek.WEDNESDAY,
                        "9:00 Композицiя (Лекцiї); [ ауд.101<A>] АРХ-11б, АРХ-12а, АРХ-12б, АРХ-13а,\n" +
                        " АРХ-13б проф. Зимiна С.Б.\n" +
                        "10:30 Архiтектурне проектування (Практ.з.); [ ауд.107<A>] ас. Козятник I.П.,\n" +
                        " ас. Сингаевська М.А., доц. Василенко Л.Г.\n" +
                        "12:20 Архiтектурне проектування (Практ.з.); [ ауд.107<A>] ас. Козятник I.П.,\n" +
                        " ас. Сингаевська М.А., доц. Василенко Л.Г.\n" +
                        "13:50 фiзвиховання (Практ.з.); []");
        expected.put(DayOfWeek.FRIDAY,
                        "9:00 Архiтектурне проектування (Практ.з.); [ ауд.107<A>] ас. Козятник I.П.,\n" +
                        " ас. Сингаевська М.А., доц. Василенко Л.Г.\n" +
                        "10:30 Архiтектурне проектування (Практ.з.); [ ауд.107<A>] ас. Козятник I.П.,\n" +
                        " ас. Сингаевська М.А., доц. Василенко Л.Г.\n" +
                        "12:20 Вища математика (Практ.з.); [22.09, 29.09 ауд.107<A>]\n" +
                        " доц. Турчанiнова Л.I.\n" +
                        "13:50 Нарисна геометрiя (Практ.з.); [22.09, 29.09 ауд.107<A>]\n" +
                        " доц. Mоcтовенко О.В.");

        Map<DayOfWeek, String> actual = sheet.prepare(sheetString).splitToDays();
        expected.forEach((dayOfWeek, s) -> expected.put(dayOfWeek,s));

        assertEquals(expected,actual);
    }

    @Test
    public void groupАnchorParseTest() throws Exception {
        sheet.prepare(sheetString);
        Group expected = new Group("АРХ-11а");
        Group actual = sheet.parseAnchor();

        assertEquals(expected, actual);
    }
}