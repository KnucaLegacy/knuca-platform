package com.theopus.parser.obj.sheets;

import com.google.common.collect.Lists;
import com.theopus.entity.schedule.Group;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class DaySheetTest {

    String day =
            "9:00 Образотворче мистецтво (Практ.з.); [ ауд.302<A>] доц. Коломiєць Ю.Д.\n " +
                    "10:30 Образотворче мистецтво (Практ.з.); [ ауд.302<A>] доц. Коломiєць Ю.Д.\n" +
                    "12:20 Дiлова українська мова (Лекцiї); [11.09 ауд.302] АРХ-11б, АРХ-12а, АРХ-12б, АРХ-13а, АРХ-13б зав.каф. Дикарева Л.Ю.\n" +
                    "--\"-- Iсторiя української державностi i культу (Лекцiї); [з 18.09 ауд.302] АРХ-11б, АРХ-12а, АРХ-12б, АРХ-13а, АРХ-13б, ДН-11, ДН-12, ДН-13 проф. Деревiнський В.Ф.\n" +
                    "13:50 Вища математика (Лекцiї); [ ауд.302] АРХ-11б, АРХ-12а, АРХ-12б, АРХ-13а, АРХ-13б доц. Турчанiнова Л.I.\n" +
                    "15:20 Iсторiя архiтектури (Лекцiї); [ ауд.101<A>] АРХ-11б, АРХ-12а, АРХ-12б, АРХ-13а, АРХ-13б доц. Горбик 0.0.\n";

    DaySheet<Group> groupDaySheet = DaySheet
            .<Group>create()
            .defaultPatterns()
            .build();

    @Test
    public void splitToLines() throws Exception {
        List<String> expected = Lists.newArrayList(
                "9:00 Образотворче мистецтво (Практ.з.); [ ауд.302<A>] доц. Коломiєць Ю.Д.",
                "10:30 Образотворче мистецтво (Практ.з.); [ ауд.302<A>] доц. Коломiєць Ю.Д.",
                "12:20 Дiлова українська мова (Лекцiї); [11.09 ауд.302] АРХ-11б, АРХ-12а, АРХ-12б, АРХ-13а, АРХ-13б зав.каф. Дикарева Л.Ю.",
                "--\"-- Iсторiя української державностi i культу (Лекцiї); [з 18.09 ауд.302] АРХ-11б, АРХ-12а, АРХ-12б, АРХ-13а, АРХ-13б, ДН-11, ДН-12, ДН-13 проф. Деревiнський В.Ф.",
                "13:50 Вища математика (Лекцiї); [ ауд.302] АРХ-11б, АРХ-12а, АРХ-12б, АРХ-13а, АРХ-13б доц. Турчанiнова Л.I.",
                "15:20 Iсторiя архiтектури (Лекцiї); [ ауд.101<A>] АРХ-11б, АРХ-12а, АРХ-12б, АРХ-13а, АРХ-13б доц. Горбик 0.0."
        );
        List<String> actual = groupDaySheet.prepare(null, day).splitToLines();
        assertEquals(expected, actual);
    }

}