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

    private String single_date_single_room_one_bracket = "15:20 Дiлова українська мова (Лекцiї); [11.09 ауд.302] АРХ-14а, АРХ-14б,\n" +
            "АРХ-16, АРХ-17, АРХ-18 зав.каф. Дикарева Л.Ю.";
    private String twobrackets_4_dates_1_room___1_date_1_room = "15:20 Об`ектно-орiентоване програмування.Альте (лабор.з.); [7.11, 14.11,\n" +
            "28.11, 5.12 ауд.368]; [12.12 ауд.366] ст.викл. Гончаренко Т.А.,\n" +
            "доц. Лисицин О.Б.";

    //manifesto to one brackets pair belongs only one room

    @Test
    public void parseCircumstances_single_date_single_room_one_bracket() throws Exception {
        LessonLine lessonLine = new LessonLine(single_date_single_room_one_bracket);
        Set<Circumstance> initial = new HashSet<Circumstance>(){
            {
                Circumstance circumstance = new Circumstance();
                circumstance.setRooms(new HashSet<Room>(){
                    {
                        Room room = new Room();
                        room.setName("ауд.302");
                        add(room);
                    }
                });
                circumstance.setDates(new HashSet<LocalDate>(){
                    {
                        add(LocalDate.of(2017,9,11));
                    }
                });
                circumstance.setLessonOrder(LessonOrder.FIFTH);
                add(circumstance);
            }
        };

        Set<Circumstance> actual = lessonLine.parseCircumstances();

        assertEquals(initial,actual);
    }

    @Test
    public void parseCircumstances() throws Exception {
        LessonLine lessonLine = new LessonLine(twobrackets_4_dates_1_room___1_date_1_room);
        Set<Circumstance> initial = new HashSet<Circumstance>(){
            {
                Circumstance circumstance = new Circumstance();
                circumstance.setRooms(new HashSet<Room>(){
                    {
                        Room room = new Room();
                        room.setName("ауд.368");
                        add(room);
                    }
                });
                circumstance.setDates(new HashSet<LocalDate>(){
                    {
                        add(LocalDate.of(2017,11,7));
                        add(LocalDate.of(2017,11,14));
                        add(LocalDate.of(2017,11,28));
                        add(LocalDate.of(2017,12,5));
                    }
                });
                circumstance.setLessonOrder(LessonOrder.FIFTH);
                add(circumstance);

                Circumstance circumstance2 = new Circumstance();
                circumstance2.setRooms(new HashSet<Room>(){
                    {
                        Room room = new Room();
                        room.setName("ауд.366");
                        add(room);
                    }
                });
                circumstance2.setDates(new HashSet<LocalDate>(){
                    {
                        add(LocalDate.of(2017,12,12));
                    }
                });
                circumstance2.setLessonOrder(LessonOrder.FIFTH);
                add(circumstance2);
            }
        };

        Set<Circumstance> actual = lessonLine.parseCircumstances();

        assertEquals(initial,actual);
    }
}
