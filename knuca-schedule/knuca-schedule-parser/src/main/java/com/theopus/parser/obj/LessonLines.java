package com.theopus.parser.obj;

/**
 * Created by Oleksandr_Tkachov on 17.12.2017.
 */
public final class LessonLines {

    private LessonLines() {
    }

    public static GroupLine.Builder createGroupLine() {
        return new GroupLine().new Builder();
    }
}
