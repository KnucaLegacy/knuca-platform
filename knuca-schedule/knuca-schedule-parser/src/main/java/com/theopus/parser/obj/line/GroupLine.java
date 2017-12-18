package com.theopus.parser.obj.line;

import com.theopus.entity.schedule.Curriculum;
import com.theopus.entity.schedule.Group;
import com.theopus.entity.schedule.enums.LessonOrder;

import java.util.List;

/**
 * Created by Oleksandr_Tkachov on 17.12.2017.
 */
public class GroupLine extends LessonLine {

    private Group group;

    @Override
    protected List<Curriculum> parse() {
        return null;
    }

    public LessonLine prepare(String line, Group group) {
        super.prepare(line);
        this.group = group;
        return this;
    }

    public LessonLine prepare(String line, LessonOrder previousOrder, Group group) {
        super.prepare(line, previousOrder);
        this.group = group;
        return this;
    }
}
