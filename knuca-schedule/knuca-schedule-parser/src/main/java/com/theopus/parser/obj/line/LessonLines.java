package com.theopus.parser.obj.line;

import com.theopus.entity.schedule.Group;

/**
 * Created by Oleksandr_Tkachov on 17.12.2017.
 */
public final class LessonLines {

    public static GroupLine.Builder createGroupLine() {
        return new GroupLine().new Builder();
    }
}
