package com.theopus.parser.obj;

import java.util.regex.Pattern;

/**
 * Created by Oleksandr_Tkachov on 17.12.2017.
 */
public class Patterns {

    public static class LessonLine {
        public static String LAB = "\\(лаб.*\\)";
        public static String PRACT = "\\(пра.*\\)";
        public static String LECTURE = "\\(лек.*\\)";
        public static String EXAM = "";
        public static String CONSULTATION = "";
        public static String TEACHER = "\\b((([^.,\\s\\d\\p{Punct}]{2,5}.)?" +
                "[^.,\\s\\d\\p{Punct}]{2,4}\\.)|[^.,\\d\\s]{3,}\\.)\\s[^.,\\s\\d]+(\\s[^.,\\d\\s]\\.)?" +
                "([^.,\\d\\s]\\.?)?";
        public static String GROUP = "";
    }

    public static class RoomDates {
        public static String BRACKETS = ("\\[([^]]+)\\]");
        public static String AUDITORY = ("ауд\\.([\\wА-я<>]+)");
        public static String SINGLE_DATE = ("(^|([^доз]\\s))(\\d?\\d\\.\\d\\d)");
        public static String FROM_DATE = ("з\\s(\\d?\\d\\.\\d\\d)");
        public static String TO_DATE = ("до\\s(\\d?\\d\\.\\d\\d)");
    }
}
