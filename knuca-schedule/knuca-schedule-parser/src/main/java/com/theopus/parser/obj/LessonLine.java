package com.theopus.parser.obj;

import com.theopus.entity.schedule.*;
import com.theopus.entity.schedule.enums.LessonOrder;
import com.theopus.entity.schedule.enums.LessonType;
import com.theopus.parser.exceptions.IllegalPdfException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public abstract class LessonLine {

    protected static final Logger LOG = LoggerFactory.getLogger(LessonLine.class);

    protected DaySheet parent;
    protected RoomDateBrackets child;

    protected String line;
    private Map<Pattern, Integer> lessonOrderMap;
    private Pattern practPattern;
    private Pattern labPattern;
    private Pattern lecPattern;
    private Pattern labPattern2;
    private Pattern facPattern;
    private Pattern consPattern;
    private Pattern indPattern;
    private Pattern exPattern;

    private Pattern teacherPattern;
    private LessonOrder previousOrder;


    private String normalize(String line) {
        return line.replaceAll("\n", " ");
    }

    protected String getGetSplits(boolean isLeft) {
        String[] split = line.split(";");
        if (split.length <= 1) {
            //ToDo add to Throw message parent info(Sheet info (Group, Date, etc.))
            throw new IllegalPdfException("Lesson line '" + line + "' does not contains ';' splitting symbol.");
        }

        if (isLeft) {
            return split[0];
        } else {
            String s = Arrays.stream(split).skip(1).reduce(String::concat).get();
            LOG.debug("{}={}", line, s);
            return s;
        }
    }

    public abstract List<Curriculum> parse();

    public LessonOrder parseOrder() {
        LessonOrder lessonOrder = Arrays.stream(LessonOrder.values())
                .filter(l -> (lessonOrderMap.entrySet().stream()
                        .filter(e -> e.getKey().matcher(line).find())
                        .findFirst().orElse(new AbstractMap.SimpleEntry<>(null, 0)).getValue() == l.ordinal()))
                .findFirst().orElse(LessonOrder.NONE);
        if (lessonOrder.ordinal() == 0) {
            if (previousOrder != null && !previousOrder.equals(LessonOrder.NONE)) {
                return previousOrder;
            } else {
                throw new IllegalPdfException("Lesson line " + line + "not contains any info about order");
            }
        } else {
            return lessonOrder;
        }
    }

    protected Subject parseSubject() {
        String leftSide = getGetSplits(true);
        String[] leftsideArr = leftSide.split(" ");
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 1; i < leftsideArr.length - 1; i++) {
            stringBuilder.append(leftsideArr[i]).append(" ");
        }

        return new Subject(stringBuilder.toString().trim());
    }

    protected LessonType parseLessonType() {
        Matcher m = lecPattern.matcher(line.toLowerCase());
        if (m.find()) {
            return LessonType.LECTURE;
        }
        m = practPattern.matcher(line.toLowerCase());
        if (m.find()) {
            return LessonType.PRACTICE;
        }
        m = labPattern2.matcher(line.toLowerCase());
        if (m.find()) {
            return LessonType.LAB;
        }
        m = labPattern.matcher(line.toLowerCase());
        if (m.find()) {
            return LessonType.LAB;
        }
        m = facPattern.matcher(line.toLowerCase());
        if (m.find()) {
            return LessonType.FACULTY;
        }
        m = exPattern.matcher(line.toLowerCase());
        if (m.find()) {
            return LessonType.EXAM;
        }
        m = consPattern.matcher(line.toLowerCase());
        if (m.find()) {
            return LessonType.CONSULTATION;
        }
        m = indPattern.matcher(line.toLowerCase());
        if (m.find()) {
            return LessonType.INDIVIDUAL;
        }
        throw new IllegalPdfException("Lesson line '" + line + "' does not have lesson type." + "Parent: " + parent);
    }

    protected Set<Teacher> parseTeachers() {
        Matcher m = teacherPattern.matcher(getGetSplits(false));
        Set<Teacher> result = new HashSet<>();
        while (m.find()) {
            String teacherName = m.group();
            result.add(new Teacher(teacherName));
        }
        return result;
    }

    protected Set<Group> parseGroups() {
        //TODO: add group parsing avilability;
        return null;
    }

    protected Set<Circumstance> parseCircumstances() {
        child.prepare(getGetSplits(false), parseOrder());
        return child.parseCircumstacnes();
    }

    public LessonLine prepare(String line) {
        this.line = normalize(line);
        return this;
    }

    public LessonLine prepare(String line, LessonOrder previousOrder) {
        this.line = normalize(line);
        this.previousOrder = previousOrder;
        return this;
    }

    public LessonLine parent(DaySheet daySheet) {
        this.parent = daySheet;
        return this;
    }

    public LessonLine child(RoomDateBrackets roomDateBrackets) {
        LessonLine.this.child = roomDateBrackets;
        roomDateBrackets.parent(this);
        return this;
    }

    public class Builder {

        public LessonLine build() {
            return LessonLine.this;
        }

        public Builder defaultPatterns() {
            practPattern = Pattern.compile(Patterns.LessonLine.PRACT, Pattern.DOTALL | Pattern.MULTILINE);
            labPattern = Pattern.compile(Patterns.LessonLine.LAB, Pattern.DOTALL | Pattern.MULTILINE);
            lecPattern = Pattern.compile(Patterns.LessonLine.LECTURE, Pattern.DOTALL | Pattern.MULTILINE);
            labPattern2 = Pattern.compile(Patterns.LessonLine.LECTURE_2, Pattern.DOTALL | Pattern.MULTILINE);
            facPattern = Pattern.compile(Patterns.LessonLine.FACULTY, Pattern.DOTALL | Pattern.MULTILINE);
            consPattern = Pattern.compile(Patterns.LessonLine.CONSULTATION, Pattern.DOTALL | Pattern.MULTILINE);
            teacherPattern = Pattern.compile(Patterns.LessonLine.TEACHER, Pattern.DOTALL | Pattern.MULTILINE);
            indPattern = Pattern.compile(Patterns.LessonLine.INDIVIDUAL, Pattern.DOTALL | Pattern.MULTILINE);
            exPattern = Pattern.compile(Patterns.LessonLine.EXAM, Pattern.DOTALL | Pattern.MULTILINE);
            return this;
        }


        public Builder orderPatterns(String filePath) {
            //ToDo move to UTIL class
            HashMap<Pattern, Integer> patternMap = new HashMap<>();
            try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
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
            LessonLine.this.lessonOrderMap = patternMap;
            return this;
        }

        public Builder orderPatterns(Map<String, LessonOrder> orderMap) {
            HashMap<Pattern, Integer> patternMap = new HashMap<>();
            orderMap.forEach((s, order) -> {
                patternMap.put(Pattern.compile(s), order.ordinal());
            });
            LessonLine.this.lessonOrderMap = patternMap;
            return this;
        }

    }

    public String getLine() {
        return line;
    }

    public DaySheet getParent() {
        return parent;
    }

    @Override
    public String toString() {
        return "LessonLine{" +
                "line='" + line + '\'' +
                '}';
    }
}
