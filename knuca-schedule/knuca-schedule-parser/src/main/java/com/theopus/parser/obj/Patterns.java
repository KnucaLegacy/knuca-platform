package com.theopus.parser.obj;

/**
 * Created by Oleksandr_Tkachov on 17.12.2017.
 */
public class Patterns {


    public static final class Sheet {

        public static final String DAY_OF_WEEK_SPLITTER = ("(понедiлок)|(Вiвторок)|(Середа)|(Четвер)|(П'ятниця)|(Субота)|(Неділя)");
        public static final String TABLE_BOUND = ("===");
        public static final String EXACT_GROUP_PATTERN = "(академгрупа)\\s([А-яA-zIiІіЇїЄє()\\s]{1,9}-?\\S{1,6})";
        public static final String SHEET_DELIMITER = "<\\s?КТОУП\\s?>";
    }

    public static final class LessonLine {
        public static final String LAB = "\\(лаб.*\\)";
        public static final String PRACT = "\\(практ\\..*\\)";
        public static final String LECTURE = "\\(лек.*\\)";
        public static final String LECTURE_2 = "\\(практикум\\)";
        public static final String EXAM = "\\(екз.*\\)";
        public static final String CONSULTATION = "\\(конс.*\\)";
        public static final String INDIVIDUAL = "\\(сам.*\\)";
        public static final String FACULTY = "\\((факу.*)|(інд\\.роб\\.)\\)";
        public static final String TEACHER = "\\b((([^.,\\s\\d\\p{Punct}]{2,5}.)?" +
                "[^.,\\s\\d\\p{Punct}]{2,4}\\.)|[^.,\\d\\s]{3,}\\.)\\s[^.,\\s\\d]+(\\s[^.,\\d\\s]\\.)?" +
                "([^.,\\d\\s]\\.?)?";
        public static final String GROUP = "";
        public static final String BEGIN_LESSON_SPLITTER = ("((\\d?\\d:\\d\\d)|(--\"--))");
        public static final String END_LESSON_SPLITTER = ("((\\d?\\d:\\d\\d)|(--\"--)|(-{4,5}))");
    }

    public static final class RoomDates {
        public static final String BRACKETS = ("\\[([^]]+)\\]");
        public static final String AUDITORY = ("ауд\\.([\\wА-я<>0-9]+)");
        public static final String SINGLE_DATE = ("(^|([^доз]\\s))(\\d?\\d\\.\\d\\d)");

        public static final String FROM_DELIMITER = "з";
        public static final String TO_DELIMITER = "до";
        public static final String FROM_TO_DATE_W = ("(" + FROM_DELIMITER + "|" + TO_DELIMITER + ")\\s" +
                "(\\d?\\d\\.\\d\\d)(\\(Ч/Т\\))?");


        public static final String WEAK_SKIP_PATTERN = "(\\(Ч/Т\\))";
    }
}
