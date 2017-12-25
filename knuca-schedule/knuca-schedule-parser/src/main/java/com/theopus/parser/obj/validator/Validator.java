package com.theopus.parser.obj.validator;

import com.theopus.entity.schedule.Curriculum;
import com.theopus.parser.obj.table.Table;

import java.util.List;

public class Validator {

    private Table table;

    public Validator(Table table) {
        this.table = table;
    }

    public List<Curriculum> validate(List<Curriculum> curriculums) {
        return curriculums;
    }
}
