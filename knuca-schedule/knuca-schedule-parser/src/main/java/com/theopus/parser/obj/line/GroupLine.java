package com.theopus.parser.obj.line;

import com.theopus.entity.schedule.*;
import com.theopus.entity.schedule.enums.LessonOrder;
import com.theopus.entity.schedule.enums.LessonType;
import com.theopus.parser.obj.sheets.Sheet;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by Oleksandr_Tkachov on 17.12.2017.
 */
public class GroupLine extends LessonLine {

    @Override
    public List<Curriculum> parse() {
        Sheet<Group> parent = this.parent.getParent();

        Set<Circumstance> circumstances = child
                .prepare(
                        getGetSplits(false),
                        parseOrder()
                ).parseCircumstacnes();
        return Collections
                .singletonList(new Curriculum(
                new Course(
                        parseSubject(),
                        parseLessonType(),
                        parseTeachers()
                ),
                parent.getAnchor(),circumstances));
    }
}
