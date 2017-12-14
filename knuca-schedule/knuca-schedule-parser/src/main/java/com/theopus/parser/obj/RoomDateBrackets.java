package com.theopus.parser.obj;

import com.theopus.entity.schedule.Circumstance;
import com.theopus.entity.schedule.Room;
import com.theopus.entity.schedule.enums.LessonOrder;
import com.theopus.parser.exceptions.IllegalPDFFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RoomDateBrackets {

    private static Logger log = LoggerFactory.getLogger(RoomDateBrackets.class);

    private String rightSplit;
    private LessonLine parent;
    private LessonOrder lessonOrder;

    public RoomDateBrackets(String rightSplit, LessonLine parent, LessonOrder lessonOrder) {
        this.rightSplit = rightSplit;
        this.parent = parent;
        this.lessonOrder = lessonOrder;
    }

    Set<Circumstance> parseCircumstaces() {
        return parseBrackets();
    }


    private Set<Circumstance> parseBrackets() {
        return null;
    }

    private Bracket splitToBrackets() {
        Matcher matcher = brackets_pattern.matcher(rightSplit);
        Bracket first = null;
        if (matcher.find()) {
            first = new Bracket(matcher.group(1));
        } else {
            throw new IllegalPDFFormatException("Not found brackets in line " + parent.line);
        }
        Bracket tmp = first;
        while (matcher.find()) {
            tmp.next = new Bracket(matcher.group(1), null, tmp);
            tmp = tmp.next;
        }
        return first;
    }


    private static Pattern brackets_pattern = Pattern.compile("\\[([^]]+)\\]");
    private static Pattern aud_pattern = Pattern.compile("ауд\\.([\\wА-я<>]+)");
    private static Pattern single_date_pattern = Pattern.compile("(^|([^доз]\\s))(\\d?\\d\\.\\d\\d)");
    private static Pattern from_date_pattern = Pattern.compile("з\\s(\\d?\\d\\.\\d\\d)");
    private static Pattern to_date_pattern = Pattern.compile("до\\s(\\d?\\d\\.\\d\\d)");
    private static DateTimeFormatter dateTimeFormat = new DateTimeFormatterBuilder()
            .appendPattern("d.MM")
            .parseDefaulting(ChronoField.YEAR, LocalDate.now().getYear())
            .toFormatter();


    class Bracket {
        private Room room;
        private Set<LocalDate> dates;
        private String bracketContent;
        private Bracket next;
        private Bracket prev;

        public Bracket(String bracketContent) {
            this.bracketContent = bracketContent;
        }

        public Bracket(String bracketContent, Bracket next, Bracket prev) {
            this.bracketContent = bracketContent;
            this.next = next;
            this.prev = prev;
        }


        private Room parseRoom() {
            Matcher matcher2 = aud_pattern.matcher(bracketContent);
            if (matcher2.find()) {
                Room room = new Room();
                room.setName(matcher2.group());
                return room;
            } else {
                //ToDo: add Info which line
                log.warn("No room avalible for line. Default has been setted. Line " + parent.line);
                Room room = new Room();
                room.setName("NoAuditory");
                return room;
            }
        }

        private Set<LocalDate> parseDates() {
            Set<LocalDate> localDates = new HashSet<>();
            localDates.addAll(getSingleDates());
//            localDates.addAll(getFromToDates());
            return localDates;

        }

        private Set<LocalDate> getSingleDates() {
            Set<LocalDate> localDates = new HashSet<>();
            Matcher matcher = single_date_pattern.matcher(bracketContent);
            while (matcher.find()) {
                localDates.add(LocalDate.parse(matcher.group(3), dateTimeFormat));
            }
            return localDates;
        }

        private Set<LocalDate> getFromToDates(){
            return null;
        }

        private void parse() {
            this.room = parseRoom();
            this.dates = parseDates();
        }
    }
}
