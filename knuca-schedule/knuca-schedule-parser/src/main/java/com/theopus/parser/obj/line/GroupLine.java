package com.theopus.parser.obj.line;

import com.theopus.entity.schedule.Circumstance;
import com.theopus.entity.schedule.Course;
import com.theopus.entity.schedule.Curriculum;
import com.theopus.entity.schedule.Group;
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

        LOG.info("{}", line);
        Set<Circumstance> circumstances = parseCircumstances();
        return Collections
                .singletonList(new Curriculum(
                        new Course(
                                parseSubject(),
                                parseLessonType(),
                                parseTeachers()
                        ),
                        parent.getAnchor(), circumstances));
    }
}
