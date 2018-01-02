package com.theopus.parser.obj.line;


import com.theopus.entity.schedule.Subject;
import com.theopus.entity.schedule.Teacher;
import com.theopus.entity.schedule.enums.LessonOrder;
import com.theopus.entity.schedule.enums.LessonType;
import com.theopus.parser.exceptions.IllegalPdfException;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by Oleksandr_Tkachov on 9/14/2017.
 */
public class LessonLineParserTest {

    private String line1 = "13:50 Архiтектурне проектування (Практ.з.); [ ауд.305<C>] ас. Коляда Т.С.,\n " +
            "      доц. Кузьмiна Г.В., ас. Козакова О.М. ";
    private LessonOrder lo1 = LessonOrder.FORUTH;
    private LessonType lt1 = LessonType.PRACTICE;
    private String Fname1 = "доц. Кузьмiна Г.В.";
    private String Sname1 = "ас. Козакова О.М.";
    private String subjectName1 = "Архiтектурне проектування";
    private String line1_5 = "12:20 Дiлова українська мова (Лекцiї); [11.09 ауд.302] АРХ-11б, АРХ-12а,\n" +
            "АРХ-12б, АРХ-13а, АРХ-13б зав.каф. Дикарева Л.Ю.";
    private String line2 = "--\"-- Iсторiя української державностi i культу (Тыхуй) (Лекцiї); [з 18.09 ауд.302]";
    private String subjectName2 = "Iсторiя української державностi i культу (Тыхуй)";
    private LessonOrder lo2 = LessonOrder.THIRD;
    private String line3 = "--\"-- Iсторiя української державностisssssssss i культу (Лекцiї) ;[з 18.09 ауд.302]";

    private String lineNOComa = "--\"-- Iсторiя української державностisssssssss i культу (Лекцiї) [з 18.09 ауд.302]";
    private String no_order_line = "Iсторiя української державностisssssssss i культу (Лекцiї) [з 18.09 ауд.302]";
    private String no_lesson_type = "--\"-- Iсторiя української державностisssssssss i культу [з 18.09 ауд.302]";
    private String line4 = "10:30 Алгоритмiзацiя та програмування (лабор.з.); [8.09, 22.09 ауд.374];";

    private LessonLine lessonLine = LessonLines
            .createGroupLine()
            .defaultPatterns()
            .orderPatterns("src/main/resources/parser-lessonorder.properties")
            .build();

    @Test
    public void parseTeacher_FEW() throws Exception {
        LessonLine line = lessonLine.prepare(line1);
        Set<Teacher> expected = new HashSet<>();
        expected.add(new Teacher("доц. Кузьмiна Г.В."));
        expected.add(new Teacher("ас. Коляда Т.С."));
        expected.add(new Teacher("ас. Козакова О.М."));

        Set<Teacher> actual = line.parseTeachers();

        assertEquals(expected, actual);
    }

    @Test
    public void parseTeacher_NONE() throws Exception {
        LessonLine line = lessonLine.prepare(line3);

        Set<Teacher> actual = line.parseTeachers();

        assertTrue(actual.isEmpty());
    }

    @Test
    public void parseTeacher_One() throws Exception {
        LessonLine line = lessonLine.prepare(line1_5);
        Set<Teacher> expected = new HashSet<>();
        expected.add(new Teacher("зав.каф. Дикарева Л.Ю."));

        Set<Teacher> actual = line.parseTeachers();

        assertEquals(expected, actual);
    }

    @Test
    public void parseTeacher_SEREGA_edition() throws Exception {
        String SergLie = "--\"-- Iсторiя української державностisssssssss i культу (Лекцiї); [з 18.09 ауд.302] хуй.хуй. ЦЮЦЮРА";
        LessonLine line = lessonLine.prepare(SergLie);
        Set<Teacher> expected = new HashSet<>();
        expected.add(new Teacher("хуй.хуй. ЦЮЦЮРА"));

        Set<Teacher> actual = line.parseTeachers();

        assertEquals(expected, actual);
    }

    @Test
    public void parseLessonType_Lab() throws Exception {
        LessonLine line = lessonLine.prepare(line4);
        LessonType expected = LessonType.LAB;

        LessonType actual = line.parseLessonType();

        assertEquals(expected, actual);
    }

    @Test
    public void parseLessonType_Pract() throws Exception {
        LessonLine line = lessonLine.prepare(line1);
        LessonType expected = LessonType.PRACTICE;

        LessonType actual = line.parseLessonType();

        assertEquals(expected, actual);
    }

    @Test
    public void parseLessonType_lekt() throws Exception {
        LessonLine line = lessonLine.prepare(line1_5);
        LessonType expected = LessonType.LECTURE;

        LessonType actual = line.parseLessonType();

        assertEquals(expected, actual);
    }

    @Test(expected = IllegalPdfException.class)
    public void parseLessonType_NoLessonTypeIdentifier() throws Exception {
        LessonLine line = lessonLine.prepare(no_lesson_type);
        LessonType expected = LessonType.PRACTICE;

        LessonType actual = line.parseLessonType();

        assertEquals(expected, actual);
    }

    @Test
    public void parseLessonSubject_NoBrackets() throws Exception {
        LessonLine line = lessonLine.prepare(line1);
        Subject expected = new Subject(subjectName1);

        Subject actual = line.parseSubject();


        assertEquals(expected.getName(), actual.getName());
    }

    @Test
    public void parseLessonSubject_WithBracket() throws Exception {
        LessonLine line = lessonLine.prepare(line2);
        Subject expected = new Subject(subjectName2);

        Subject actual = line.parseSubject();

        assertEquals(expected.getName(), actual.getName());
    }

    @Test(expected = IllegalPdfException.class)
    public void parseLesssonSubject_NO_dotcoma() throws Exception {
        LessonLine line = lessonLine.prepare(lineNOComa);

        line.parseSubject();
        fail("Exception not trowed");

    }

    @Test
    public void parseLessonOrder_FOURTH() throws Exception {
        LessonLine line = lessonLine.prepare(line1);
        LessonOrder actual = line.parseOrder();
        assertEquals(lo1, actual);
    }

    @Test
    public void parseLessonOrder_NOTSPECIFIED_2INAROW() throws Exception {
        LessonOrder _1 = lessonLine.prepare(line1_5).parseOrder();
        LessonOrder _2 = lessonLine.prepare(line2, _1).parseOrder();
        LessonLine line = lessonLine.prepare(line3, _2);
        LessonOrder actual = line.parseOrder();
        assertEquals(lo2, actual);
    }

    @Test(expected = IllegalPdfException.class)
    public void parseLessonOrder_NOTSPECIFIED_PREVIOUS_NULL() {
        LessonLine line = lessonLine.prepare(line2);
        LessonOrder actual = line.parseOrder();
    }

    @Test(expected = IllegalPdfException.class)
    public void parseLessonOrder_NO_ORDER_INFO_AT_ALL() throws Exception {
        LessonLine line = lessonLine.prepare(no_order_line);
        LessonOrder actual = line.parseOrder();
        fail();
    }
}
