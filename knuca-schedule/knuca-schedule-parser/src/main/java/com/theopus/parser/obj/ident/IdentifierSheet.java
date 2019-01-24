package com.theopus.parser.obj.ident;

import com.theopus.entity.schedule.Curriculum;
import com.theopus.parser.obj.Sheet;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public abstract class IdentifierSheet<T> extends Sheet<T> {

    protected final String tableBound;
    protected final Pattern anchorPattern;

    public IdentifierSheet(String tableBound, String pattern) {
        this.tableBound = tableBound;
        this.anchorPattern = Pattern.compile(pattern);
    }

    @Override
    public List<Curriculum> parse() {
        return Collections.emptyList();
    }

    @Override
    public Sheet<T> prepare(String content) {
        this.content = content;
        return this;
    }
}
