package com.theopus.parser;

import com.theopus.entity.schedule.enums.LessonOrder;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;


public class LessonLine {

    private String line;
    private LessonLine previous;
    private static Map<Pattern, Integer> lessonMap = loadPropherties();
    private static Map<Pattern, Integer> loadPropherties() {
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

    public LessonLine(String line) {
        this.line = line;
    }

    public LessonLine(String line, LessonLine previous) {
        this.line = line;
        this.previous = previous;
    }

    public LessonOrder parseOrder() {
        LessonOrder lessonOrder = Arrays.stream(LessonOrder.values())
                .filter(l -> (lessonMap.entrySet().stream()
                        .filter(e -> e.getKey().matcher(line).find())
                        .findFirst().orElse(new AbstractMap.SimpleEntry<>(null, 0)).getValue() == l.ordinal()))
                .findFirst().orElse(LessonOrder.NONE);
        if (lessonOrder.ordinal() == 0) {
            if (previous != null)
                //kinda recursion;
                return previous.parseOrder();
            else
                return LessonOrder.NONE;
        } else
            return lessonOrder;
//        return  Arrays.stream(LessonOrder.values()).findAny().filter(lessonMap
//                .entrySet()
//                .stream()
//                .findAny()
//                .filter(e -> e.getKey().matcher(line).find())
//                .orElse(new AbstractMap.SimpleEntry<>(null, 0)).getValue()::equals).orElse(LessonOrder.NONE);

    }
}
