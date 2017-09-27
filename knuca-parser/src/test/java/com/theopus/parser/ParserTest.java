package com.theopus.parser;


import com.theopus.entity.schedule.enums.LessonOrder;
import com.theopus.entity.schedule.enums.LessonType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Oleksandr_Tkachov on 9/14/2017.
 */
public class ParserTest {

    private String line1 = "13:50 Архiтектурне проектування (Практ.з.); [ ауд.305<C>] ас. Коляда Т.С.,\n       доц. Кузьмiна Г.В., ас. Козакова О.М. ";
    private LessonOrder lo1 = LessonOrder.FORUTH;
    private LessonType lt1 = LessonType.PRACTICE;
    private String Fname1 = "доц. Кузьмiна Г.В.";
    private String Sname1 = "ас. Козакова О.М.";


    private String line1_5 = "12:20 Дiлова українська мова (Лекцiї); [11.09 ауд.302] АРХ-11б, АРХ-12а,\n" +
            "АРХ-12б, АРХ-13а, АРХ-13б зав.каф. Дикарева Л.Ю.";
    private String line2 = "--\"-- Iсторiя української державностi i культу (Лекцiї); [з 18.09 ауд.302]";
    private LessonOrder lo2 = LessonOrder.THIRD;
    private String line3 = "--\"-- Iсторiя української державностisssssssss i культу (Лекцiї); [з 18.09 ауд.302]";



    @Test
    public void parseLessonOrder_FOURTH() throws Exception {
        LessonLine line = new LessonLine(line1);
        LessonOrder actual = line.parseOrder();
        assertEquals(lo1,actual);
    }

    @Test
    public void parseLessonOrder_NOTSPECIFIED() throws Exception {
        LessonLine previous = new LessonLine(line1_5);
        LessonLine line = new LessonLine(line2, previous);
        LessonOrder lessonOrder = line.parseOrder();
        assertEquals(lo2,lessonOrder);
    }

    @Test
    public void parseLessonOrder_NOTSPECIFIED_2INAROW() throws Exception {
        LessonLine previous2 = new LessonLine(line1_5);
        LessonLine previous = new LessonLine(line2, previous2);
        LessonLine line = new LessonLine(line3, previous);
        LessonOrder actual = line.parseOrder();
        assertEquals(lo2,actual);
    }

    @Test
    public void parseLessonOrder_NOTSPECIFIED_PREVIOUS_NULL(){
        LessonLine line = new LessonLine(line2);
        LessonOrder actual = line.parseOrder();
        assertEquals(LessonOrder.NONE, actual);
    }


}
