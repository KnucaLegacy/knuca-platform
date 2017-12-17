package com.theopus.parser.obj;

import com.theopus.entity.schedule.Circumstance;
import com.theopus.entity.schedule.Lesson;
import com.theopus.entity.schedule.Subject;
import com.theopus.entity.schedule.Teacher;
import com.theopus.entity.schedule.enums.LessonOrder;
import com.theopus.entity.schedule.enums.LessonType;
import com.theopus.parser.exceptions.IllegalPDFFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LessonLine {

    private final static Logger log = LoggerFactory.getLogger(LessonLine.class);

    private RoomDateBrackets roomDateBrackets;

    String line;
    private Map<Pattern, Integer> lessonOrderMap = loadProperties();
    private Pattern practPattern;
    private Pattern labPattern;
    private Pattern lecPattern;

    private Pattern teacherPattern;
    private LessonOrder previousOrder;

    private static Map<Pattern, Integer> loadProperties() {
        //ToDo move to UTIL class
        HashMap<Pattern, Integer> patternMap = new HashMap<>();
        try (FileInputStream fileInputStream = new FileInputStream("src/main/resources/parser-lessonorder.properties")) {
            Properties prop = new Properties();
            prop.load(fileInputStream);
            Enumeration<?> e = prop.propertyNames();
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                String value = prop.getProperty(key);
                patternMap.put(Pattern.compile(value), Integer.valueOf(key));
            }
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return patternMap;
    }

    LessonOrder parseOrder() {
        LessonOrder lessonOrder = Arrays.stream(LessonOrder.values())
                .filter(l -> (lessonOrderMap.entrySet().stream()
                        .filter(e -> e.getKey().matcher(line).find())
                        .findFirst().orElse(new AbstractMap.SimpleEntry<>(null, 0)).getValue() == l.ordinal()))
                .findFirst().orElse(LessonOrder.NONE);
        if (lessonOrder.ordinal() == 0) {
            if (previousOrder != null && !previousOrder.equals(LessonOrder.NONE)) {
                return previousOrder;
            } else {
                throw new IllegalPDFFormatException("Lesson line " + line + "not contains any info about order");
            }
        } else {
            return lessonOrder;
        }
    }

    public LessonLine() {
    }

    LessonLine(String line) {
        this.line = normalize(line);
    }

    private String normalize(String line) {
        return line.replaceAll("\n", " ");
    }

    Subject parseSubject() {
        String leftSide = getGetSplits(true);
        String[] leftsideArr = leftSide.split(" ");
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 1; i < leftsideArr.length - 1; i++) {
            stringBuilder.append(leftsideArr[i]).append(" ");
        }

        return new Subject(stringBuilder.toString().trim());
    }

    private String getGetSplits(boolean isLeft) {
        String[] split = line.split(";");
        if (split.length <= 1) {
            //ToDo add to Throw message parent info(Sheet info (Group, Date, etc.))
            throw new IllegalPDFFormatException("Lesson line '" + line + "' does not contains ';' splitting symbol.");
        }

        if (isLeft) {
            return split[0];
        } else {
            return Arrays.stream(split).skip(1).reduce(String::concat).get();
        }
    }

    LessonType parseLessonType() {
        Matcher m = practPattern.matcher(line.toLowerCase());
        if (m.find()) {
            return LessonType.PRACTICE;
        }
        m = labPattern.matcher(line.toLowerCase());
        if (m.find()) {
            return LessonType.LAB;
        }
        m = lecPattern.matcher(line.toLowerCase());
        if (m.find()) {
            return LessonType.LECTURE;
        } else throw new IllegalPDFFormatException("Lesson line '" + line + "' does not have lesson type.");
    }

    Set<Teacher> parseTeachers() {
        Matcher m = teacherPattern.matcher(line);
        Set<Teacher> result = new HashSet<>();
        while (m.find()) {
            String teacherName = m.group();
            result.add(new Teacher(teacherName));
        }
        return result;
    }

    Set<Circumstance> parseCircumstances() {
        return roomDateBrackets.parseCircumstacnes();
    }

    public LessonLine prepare(String line2) {
        this.line = line2;
        return this;
    }

    public static LessonLine build(){
        return new LessonLine();
    }

    public LessonLine orderChain(LessonOrder order) {
        this.previousOrder = order;
        return this;
    }

    public LessonLine roomDateBrackets(RoomDateBrackets roomDateBrackets){
        this.roomDateBrackets = roomDateBrackets;
        return this;
    }

    public LessonLine defaultPatterns(){
        practPattern = Pattern.compile(Patterns.LessonLine.PRACT);
        labPattern = Pattern.compile(Patterns.LessonLine.LAB);
        lecPattern = Pattern.compile(Patterns.LessonLine.LECTURE);
        teacherPattern = Pattern.compile(Patterns.LessonLine.TEACHER);
        return this;
    }
}
