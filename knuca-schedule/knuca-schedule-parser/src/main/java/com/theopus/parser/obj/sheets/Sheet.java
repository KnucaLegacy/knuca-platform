package com.theopus.parser.obj.sheets;

import com.theopus.entity.schedule.Curriculum;
import com.theopus.parser.exceptions.IllegalPDFFormatException;
import com.theopus.parser.obj.table.Table;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents view of full sheet with T anchor type.
 *
 * @param <T> anchor type.
 */

public class Sheet<T> {

    private Table table;
    private String content;
    private T anchor;

    private FileSheet<T> parent;
    private DaySheet<T> child;

    public Table getTable() {
        return table;
    }

    public List<Curriculum> parse() {
        return splitToDays().entrySet().stream().map(dc -> {
            child.prepare(dc.getKey(), dc.getValue());
            return child.parse();
        }).reduce((c1, c2) -> {
            c1.addAll(c2);
            return c1;
        }).orElseThrow(() -> new IllegalPDFFormatException("Daysheet"));

    }

    Map<DayOfWeek, String> splitToDays() {
        return new HashMap<>();
    }

    public T getAnchor() {
        return anchor;
    }

    public Sheet<T> child(DaySheet<T> daySheet) {
        this.child = daySheet;
        return this;
    }

    public Sheet<T> anchor(T anchor) {
        this.anchor = anchor;
        return this;
    }

    public Sheet<T> parent(FileSheet<T> fileSheet) {
        this.parent = fileSheet;
        return this;
    }
}
