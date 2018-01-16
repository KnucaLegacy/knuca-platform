package com.theopus.parser.obj;

import com.theopus.entity.schedule.Curriculum;
import com.theopus.parser.ParserUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileSheet<T> {

    protected Sheet<T> child;
    private String fullFile;
    private Pattern delimiterPattern;
    private int position;
    private List<String> sheets;
    private DateTimeFormatter dateTimeFormat;
    private boolean autumn;

    public FileSheet() {

    }

    public List<Curriculum> parseAll() {
        List<Curriculum> result = new ArrayList<>();
        while (next()) {
            result.addAll(parse());
        }
        return result;
    }

    public List<Curriculum> parse() {
        child.prepare(sheets.get(position++));
        return child.parse();
    }

    public boolean next() {
        return position < sheets.size();
    }


    List<String> splitToSheets() {
        Matcher matcher = delimiterPattern.matcher(fullFile);
        List<String> result = new ArrayList<>();
        List<Integer> map = new ArrayList<>();
        while (matcher.find()) {
            map.add(matcher.start());
        }
        map.add(fullFile.length());
        map.stream().reduce((d1, d2) -> {
            result.add(ParserUtils.trimWhitespaces(fullFile.substring(d1, d2)));
            return d2;
        });
        return result;
    }

    public FileSheet<T> prepare(String fullFile) {
        this.position = 0;
        this.fullFile = ParserUtils.replaceEngToUkr(fullFile);
        sheets = splitToSheets();
        return this;
    }

    public FileSheet<T> child(Sheet<T> sheet) {
        this.child = sheet;
        sheet.parent(this);
        return this;
    }

    public static <T> FileSheet<T>.Builder create() {
        return new FileSheet<T>().new Builder();
    }

    public Integer getTotal() {
        return sheets.size();
    }

    public boolean isAutumn() {
        return autumn;
    }

    public void setAutumn(boolean autumn) {
        this.autumn = autumn;
    }

    public Sheet<T> getCurrent() {
        return child.prepare(sheets.get(position));
    }

    public class Builder {

        public FileSheet<T> build() {
            return FileSheet.this;
        }

        public Builder delimiter(String delimiter) {
            FileSheet.this.delimiterPattern = Pattern.compile(delimiter);
            return this;
        }
    }
}
