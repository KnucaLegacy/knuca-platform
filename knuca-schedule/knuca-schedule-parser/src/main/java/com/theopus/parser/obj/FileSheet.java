package com.theopus.parser.obj;

import com.theopus.entity.schedule.Curriculum;
import com.theopus.parser.ParserUtils;
import com.theopus.parser.exceptions.IllegalParserFileException;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    private Integer definedYear;

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
        if (!Objects.isNull(definedYear) && definedYear >= 2016) { //FIXME: CHE NAHOI `2016`
            child.prepare(sheets.get(position++), definedYear);
        } else {
            child.prepare(sheets.get(position++));
        }
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
        if (sheets.isEmpty()) {
            throw new IllegalParserFileException("File does not contain any schedule");
        }
        return this;
    }

    public FileSheet<T> prepare(String fullFile, Integer definedYear) {
        this.definedYear = definedYear;
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

    public ValidatorReport getReportOfCurrentSheet() {
        return child.getValidatorReport();
    }

    public int getPosition() {
        return position;
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
