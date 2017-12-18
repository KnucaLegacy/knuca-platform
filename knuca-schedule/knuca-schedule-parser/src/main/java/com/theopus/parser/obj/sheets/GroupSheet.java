package com.theopus.parser.obj.sheets;

import com.theopus.entity.schedule.Curriculum;
import com.theopus.entity.schedule.Group;
import com.theopus.parser.obj.table.Table;

import java.util.List;

public class GroupSheet implements Sheet<Group> {

    private Table table;
    private Group group;

    @Override
    public Group getAnchor() {
        return group;
    }

    @Override
    public Table getTable() {
        return table;
    }

    @Override
    public List<Curriculum> parse() {
        return null;
    }
}
