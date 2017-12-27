package com.theopus.parser.obj.roomdate;

import com.theopus.entity.schedule.Circumstance;
import com.theopus.entity.schedule.Room;
import com.theopus.entity.schedule.enums.LessonOrder;
import com.theopus.parser.exceptions.IllegalPdfException;
import com.theopus.parser.obj.Patterns;
import com.theopus.parser.obj.line.LessonLine;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RoomDateBrackets {

    private static Logger log = LoggerFactory.getLogger(RoomDateBrackets.class);

    private String rightSplit;
    private LessonLine parent;
    private LessonOrder lessonOrder;

    protected RoomDateBrackets() {
    }

    public Set<Circumstance> parseCircumstacnes() {
        Set<Circumstance> circumstances = parseBrackets();
        circumstances.forEach(c -> c.setLessonOrder(lessonOrder));
        return circumstances;
    }


    Set<Circumstance> parseBrackets() {
        Room dummyRoom = new Room(Room.NO_AUDITORY);
        Set<Circumstance> result = new HashSet<>();
        Bracket bracket = splitToBrackets();
        HashSet<Circumstance> emptyRooms = new HashSet<>();
        HashSet<Circumstance> emptyDates = new HashSet<>();
        emptyRooms.clear();

        Room cacheRoom = dummyRoom;
        Set<LocalDate> cacheDates = new HashSet<>();

        for (Bracket current = bracket.parse(); current != null; current = current.next) {
            current.parse();
            Circumstance circumstance = new Circumstance();

            if (!current.dates.isEmpty() && !current.room.equals(dummyRoom)) {
                circumstance.setDates(current.dates);
                circumstance.setRoom(current.room);
                cacheDates = current.dates;
                cacheRoom = current.room;

                Room finalCacheRoom = cacheRoom;
                emptyRooms.forEach(c -> {
                    c.setRoom(finalCacheRoom);
                    result.add(c);
                });
                result.addAll(emptyRooms);
                emptyRooms.clear();
                Set<LocalDate> finalCacheDates = cacheDates;
                emptyDates.forEach(c -> {
                    c.setDates(finalCacheDates);
                    result.add(c);
                });
                result.addAll(emptyDates);
                emptyDates.clear();
                result.add(circumstance);
            }

            if (current.dates.isEmpty() && !current.room.equals(dummyRoom)) {
                circumstance.setRoom(current.room);
                cacheRoom = current.room;
                emptyDates.add(circumstance);
            }
            if (!current.dates.isEmpty() && current.room.equals(dummyRoom)) {
                circumstance.setDates(current.dates);
                cacheDates = current.dates;
                emptyRooms.add(circumstance);
            }
            if (current.dates.isEmpty() && current.room.equals(dummyRoom)) {
                log.warn("Empty [] brackets in {}", parent);
            }
            if (current.next == null) {
                circumstance.setRoom(cacheRoom);
                circumstance.setDates(cacheDates);
                result.add(circumstance);

                Room finalCacheRoom = cacheRoom;
                emptyRooms.forEach(c -> {
                    c.setRoom(finalCacheRoom);
                    result.add(c);
                });
                emptyRooms.clear();
                Set<LocalDate> finalCacheDates = cacheDates;
                emptyDates.forEach(c -> {
                    c.setDates(finalCacheDates);
                    result.add(c);
                });
                emptyDates.clear();
            }
        }
        return result;
    }

    private Bracket splitToBrackets() {
        Matcher matcher = bracketsPattern.matcher(rightSplit);
        Bracket first = null;
        if (matcher.find()) {
            first = new Bracket(matcher.group(1));
        } else {
            throw new IllegalPdfException("Not found brackets in line " + parent.getLine());
        }
        Bracket tmp = first;
        while (matcher.find()) {
            tmp.next = new Bracket(matcher.group(1), null, tmp);
            tmp = tmp.next;
        }
        return first;
    }

    private Pattern bracketsPattern;
    private Pattern audPattern;
    private Pattern singleDatePattern;
    private Pattern fromToPatternW;
    private String fromDelimiter;
    private String toDelimiter;

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
            Matcher matcher2 = audPattern.matcher(bracketContent);
            if (matcher2.find()) {
                Room room = new Room();
                room.setName(matcher2.group());
                return room;
            } else {
                //ToDo: add Info which line
                log.warn("No room avalible for line {} . Default has been setted. ", parent.getLine());
                Room room = new Room();
                room.setName(Room.NO_AUDITORY);
                return room;
            }
        }

        private Set<LocalDate> parseDates() {
            Set<LocalDate> localDates = new HashSet<>();
            localDates.addAll(getSingleDates());
            localDates.addAll(getFromToDates());
            return localDates;

        }

        private Set<LocalDate> getSingleDates() {
            Set<LocalDate> localDates = new HashSet<>();
            Matcher matcher = singleDatePattern.matcher(bracketContent);
            while (matcher.find()) {
                localDates.add(LocalDate.parse(matcher.group(3), dateTimeFormat));
            }
            return localDates;
        }

        private Set<LocalDate> getFromToDates() {
            Set<LocalDate> localDates = new HashSet<>();
            Matcher matcher = fromToPatternW.matcher(bracketContent);
            Stack<Pair<String, Boolean>> fromStack = new Stack<>();
            Stack<Pair<String, Boolean>> toStack = new Stack<>();
            while (matcher.find()) {
                boolean weekSkip = false;
                if (!Objects.isNull(matcher.group(3))) {
                    weekSkip = true;
                }

                String group = matcher.group(1);
                if (group.equals(fromDelimiter)) {
                    fromStack.push(new Pair<>(matcher.group(2), weekSkip));
                } else if (group.equals(toDelimiter)) {
                    if (fromStack.empty()) {
                        toStack.add(new Pair<>(matcher.group(2), weekSkip));
                    } else {
                        Pair<String, Boolean> from = fromStack.pop();
                        String to = matcher.group(2);
                        localDates.addAll(fromToRange(
                                convert(from.getKey()), convert(to),
                                from.getValue() | weekSkip));
                    }
                }
            }
            fromStack.forEach(pair -> localDates.addAll(fromToRange(
                    convert(pair.getKey()),
                    parent.getParent().getParent().getTable().getToBound(parent.getParent().getDayOfWeek()),
                    pair.getValue()
            )));
            toStack.forEach(pair -> localDates.addAll(fromToRange(
                    parent.getParent().getParent().getTable().getFromBound(parent.getParent().getDayOfWeek()),
                    convert(pair.getKey()),
                    pair.getValue()
            )));
            return localDates;
        }

        private LocalDate convert(String date) {
            return LocalDate.parse(date, dateTimeFormat);
        }

        private Set<LocalDate> fromToRange(LocalDate start, LocalDate end, boolean isWeekSkip) {
            System.out.println(start);
            System.out.println(end);
            int delta = isWeekSkip ? 7 * 2 : 7;
            int until = (int) start.until(end, ChronoUnit.DAYS);
            return Stream.iterate(start, d -> d.plusDays(delta))
                    .limit(until / delta + 1)
                    .collect(Collectors.toSet());
        }

        private Bracket parse() {
            this.room = parseRoom();
            this.dates = parseDates();
            return this;
        }
    }

    public RoomDateBrackets parent(LessonLine lessonLine) {
        this.parent = lessonLine;
        return this;
    }

    public RoomDateBrackets prepare(String rightSplit, LessonOrder lessonOrder) {
        this.rightSplit = rightSplit;
        this.lessonOrder = lessonOrder;
        return this;
    }

    public static RoomDateBrackets.Builder create() {
        return new RoomDateBrackets().new Builder();
    }

    public class Builder {

        public RoomDateBrackets build() {
            return RoomDateBrackets.this;
        }

        public Builder defaultPatterns() {
            bracketsPattern = Pattern.compile(Patterns.RoomDates.BRACKETS);
            audPattern = Pattern.compile(Patterns.RoomDates.AUDITORY);
            singleDatePattern = Pattern.compile(Patterns.RoomDates.SINGLE_DATE);
            fromToPatternW = Pattern.compile(Patterns.RoomDates.FROM_TO_DATE_W);
            fromDelimiter = Patterns.RoomDates.FROM_DELIMITER;
            toDelimiter = Patterns.RoomDates.TO_DELIMITER;
            return this;
        }
    }
}
