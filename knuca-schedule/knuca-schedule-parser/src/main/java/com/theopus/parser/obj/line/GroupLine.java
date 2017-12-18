package com.theopus.parser.obj.line;

import com.theopus.entity.schedule.Curriculum;
import com.theopus.entity.schedule.Group;
import com.theopus.entity.schedule.enums.LessonOrder;
import com.theopus.parser.obj.sheets.Sheet;

import java.util.List;

/**
 * Created by Oleksandr_Tkachov on 17.12.2017.
 */
public class GroupLine extends LessonLine {

    private Group group;

    @Override
    protected List<Curriculum> parse() {
        Sheet<Group> parent = parrent.getParent();
        group = parent.getAnchor();
        return null;
    }
}
