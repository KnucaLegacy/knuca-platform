package com.theopus.parser.obj;

import com.theopus.entity.schedule.Subject;
import com.theopus.entity.schedule.Teacher;
import com.theopus.entity.schedule.enums.LessonOrder;
import com.theopus.entity.schedule.enums.LessonType;
import com.theopus.parser.exceptions.IllegalPDFFormatException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LessonLine {

    private String line;
    private LessonLine previous;
    private static Map<Pattern, Integer> lessonOrderMap = loadProperties();

    //ToDO: add another pattern for case where teacher does not have academic degree
    private static Pattern teacherPattern =
            Pattern.compile("\\b((([^.,\\s\\d\\p{Punct}]{2,5}.)?[^.,\\s\\d\\p{Punct}]{2,4}\\.)|[^.,\\d\\s]{3,}\\.)\\s[^.,\\s\\d]+(\\s[^.,\\d\\s]\\.)?([^.,\\d\\s]\\.?)?");

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
            if (previous != null)
                //kinda recursion;
                return previous.parseOrder();
            else
                throw new IllegalPDFFormatException("Lesson line " + line + "not contains any info about order");
        } else
            return lessonOrder;
    }

    LessonLine(String line) {
        this.line = line;
    }

    LessonLine(String line, LessonLine previous) {
        this.line = line;
        this.previous = previous;
    }

    Subject parseSubject() {
        //Todo: Replace to RegEx
        String[] split = line.split(";");
        if (split.length <= 1){
            //ToDo add to Throw message parent info(Sheet info (Group, Date, etc.))
            throw new IllegalPDFFormatException("Lesson line '" + line + "' does not contains ';' splitting symbol.");
        }
        String leftSide = split[0];
        String[] leftsideArr = leftSide.split(" ");
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 1; i < leftsideArr.length - 1; i++) {
            stringBuilder.append(leftsideArr[i]).append(" ");
        }

        return new Subject(stringBuilder.toString().trim());
    }

    LessonType parseLessonType() {

        //ToDo: Move stringPatterns to property file
        Pattern practPattern = Pattern.compile("\\(пра.*\\)");
        Pattern labPattern = Pattern.compile("\\(лаб.*\\)");
        Pattern lecPattern = Pattern.compile("\\(лек.*\\)");
        Matcher m = practPattern.matcher(line.toLowerCase());
        if (m.find()){
            return LessonType.PRACTICE;
        }
        m = labPattern.matcher(line.toLowerCase());
        if (m.find()){
            return LessonType.LAB;
        }
        m = lecPattern.matcher(line.toLowerCase());
        if (m.find()){
            return LessonType.LECTURE;
        }
        else throw new IllegalPDFFormatException("Lesson line '" + line + "' does not have lesson type.");
    }

    Set<Teacher> parseTeachers() {
        Matcher m = teacherPattern.matcher(line);
        Set<Teacher> result = new HashSet<>();
        while (m.find()){
            String teacherName = m.group();
            result.add(new Teacher(teacherName));
        }
        return result;
    }
}
