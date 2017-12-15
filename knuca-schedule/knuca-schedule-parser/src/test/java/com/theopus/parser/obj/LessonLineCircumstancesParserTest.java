package com.theopus.parser.obj;

import com.theopus.entity.schedule.Circumstance;
import com.theopus.entity.schedule.Room;
import com.theopus.entity.schedule.enums.LessonOrder;
import org.junit.Test;

import java.time.LocalDate;
import java.util.HashSet;
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

    //manifesto to one brackets pair belongs only one room


    @Test
    public void parseCircumstaces_four_brackets_1room__2dates__1room__1room_1SinlgeDate() throws Exception {

    }

    @Test
    public void parseCircumstances_four_brackets_1room__2dates_1room_1room() throws Exception {

    }

    @Test
    public void parseCircumstances_single_date_single_room_one_bracket() throws Exception {

    }

    @Test
    public void parse_twobrackets_4_dates_1_room___1_date_1_room() throws Exception {
        RoomDateBrackets rdb = new RoomDateBrackets(twobrackets_4_dates_1_room___1_date_1_room,null,null);
        Set<Circumstance> circumstances = rdb.parseCircumstaces();
        circumstances.forEach(circumstance -> {
            System.out.println(circumstance);
        });
    }


    @Test
    public void parseCircumstaces_two_brackets_1room__2singledate_1room() throws Exception {
        RoomDateBrackets rdb = new RoomDateBrackets(two_brackets_1room__2singledate_1room,null,null);

    }

    @Test
    public void parseCircumstaces_two_brackets_2singledate_1room__1room() throws Exception {
        RoomDateBrackets rdb = new RoomDateBrackets(two_brackets_2singledate_1room__1room,null,null);
    }
}
