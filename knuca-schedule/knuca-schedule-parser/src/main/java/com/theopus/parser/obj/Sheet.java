package com.theopus.parser.obj;

import com.theopus.entity.schedule.Curriculum;
import com.theopus.entity.schedule.Group;
import com.theopus.parser.ParserUtils;
import com.theopus.parser.exceptions.IllegalParserFileException;
import com.theopus.parser.utl.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents view of full sheet with T anchor type.
 *
 * @param <T> anchor type.
 */

public abstract class Sheet<T> {

    private static final Logger LOG = LoggerFactory.getLogger(Sheet.class);
    protected Table table;
    protected String content;
    private T anchor;

    private FileSheet<T> parent;
    private DaySheet<T> child;

    private Pattern dayOfWeekSplitter;
    protected String tableBound;

    protected Pattern anchorPattern;
    private Validator validator;
    protected Integer sheetYear;
    private DateTimeFormatter dateTimeFormat;

    public Table getTable() {
        return table;
    }

    private ValidatorReport validatorReport;

    public List<Curriculum> parse() {
        List<Curriculum> parsed = splitToDays().entrySet().stream().map(dc -> {
            child.prepare(dc.getKey(), dc.getValue());
            return child.parse();
        }).reduce((c1, c2) -> {
            c1.addAll(c2);
            return c1;
        }).orElseThrow(() -> new IllegalParserFileException("Daysheet"));
        ValidatorReport validate = validator.validate(parsed);
        this.validatorReport = validate;
        int violationsCount = validate.getViolationsCount();
        if (violationsCount != 0) {
            LOG.info("Errors after validation: {}, anchor: {}", violationsCount, anchor);
            int criticalViolationsCount = validate.getCriticalViolationsCount();
            if (criticalViolationsCount != 0) {
                LOG.error("Critical errors after validation: {}!", violationsCount);
                LOG.error("{}", validate);
            }
        }
        return parsed;
    }

    Map<DayOfWeek, String> splitToDays() {
        Matcher matcher = dayOfWeekSplitter.matcher(content);
        matcher.region(content.lastIndexOf(tableBound), content.length() - 1);
        Map<DayOfWeek, Pair<Integer, Integer>> map = new LinkedHashMap<>();
        while (matcher.find()) {
            map.put(ParserUtils.converUkrDayOfWeek(matcher.group()), new Pair<>(matcher.start(), matcher.end()));
        }
        map.put(null, new Pair<>(content.length(), content.length()));
        Map<DayOfWeek, String> result = new LinkedHashMap<>();
        map.entrySet().stream().reduce((d1, d2) -> {
            result.put(d1.getKey(), ParserUtils
                    .trimWhitespaces(content.substring(d1.getValue().getValue(), d2.getValue().getKey())
                            .replaceAll("---", "")));
            return d2;
        });
        return result;
    }

    public T getAnchor() {
        return anchor;
    }

    public Sheet<T> child(DaySheet<T> daySheet) {
        this.child = daySheet;
        daySheet.parent(this);
        return this;
    }

    public Sheet<T> link(FileSheet<T> fileSheet) {
        this.parent = fileSheet;
        fileSheet.child(this);
        return this;
    }

    public abstract T parseAnchor() throws IllegalParserFileException;

    public static Sheet<Group>.Builder createGroupSheet() {
        return new GroupSheet().new Builder();
    }

    public Sheet<T> prepare(String content) {
        this.content = content;
        this.sheetYear = parseYear();
        this.initFormatter();
        this.table.prepare(content);
        this.anchor = parseAnchor();
        this.validatorReport = null;
        return this;
    }

    public Sheet<T> prepare(String content, Integer year) {
        this.content = content;
        this.sheetYear = year;
        this.initFormatter();
        this.table.prepare(content);
        this.anchor = parseAnchor();
        return this;
    }

    public void initFormatter() {
        this.dateTimeFormat = new DateTimeFormatterBuilder()
                .appendPattern("d.MM")
                .parseDefaulting(ChronoField.YEAR, sheetYear)
                .toFormatter();
    }

    private Integer parseYear() {
        Matcher matcher = Pattern.compile("\\d\\d\\d\\d").matcher(content);
        matcher.region(0, content.indexOf(tableBound));
        if (matcher.find()) {
            return Integer.valueOf(matcher.group(0));
        }
        throw new IllegalParserFileException("Cannot parse date from sheet " + this);
    }

    public Sheet<T> parent(FileSheet<T> fileSheet) {
        this.parent = fileSheet;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Sheet<T> validator(Validator validator) {
        this.validator = validator;
        validator.parent(this);
        return this;
    }

    public FileSheet<T> getParent() {
        return parent;
    }

    public LocalDate convert(String date) {
        LocalDate parse = LocalDate.parse(date, dateTimeFormat);
        if (parent.isAutumn()) {
            if (parse.getMonthValue() < 3) {
                parse = parse.plusYears(1);
            }

        } else {
            if (parse.getMonthValue() >= 8) {
                parent.setAutumn(true);
            }
        }
        return parse;
    }

    public ValidatorReport getValidatorReport() {
        if (Objects.isNull(validatorReport)) {
            throw new RuntimeException("Report not ready");
        }
        return validatorReport;
    }

    public class Builder {

        public Sheet<T>.Builder defaultPatterns() {
            Sheet.this.dayOfWeekSplitter = Pattern.compile(
                    ParserUtils.replaceEngToUkr(Patterns.Sheet.DAY_OF_WEEK_SPLITTER),
                    Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.DOTALL | Pattern.MULTILINE
            );
            return this;
        }

        public Builder anchorPattern(String pattern) {
            Sheet.this.anchorPattern = Pattern.compile(pattern);
            return this;
        }

        public Sheet<T>.Builder deafultTableBound() {
            Sheet.this.tableBound = Patterns.Sheet.TABLE_BOUND;
            return this;
        }

        public Sheet<T>.Builder table(Table table) {
            Sheet.this.table = table;
            table.parent(Sheet.this);
            return this;
        }

        public Sheet<T> build() {
            return Sheet.this;
        }
    }

    @Override
    public String toString() {
        return "Sheet{" +
                "content='" + content + '\'' +
                '}';
    }

    public Integer getSheetYear() {
        return sheetYear;
    }
}
