package com.theopus.parser.facade;

import com.google.common.collect.ImmutableMap;
import com.theopus.entity.schedule.Group;
import com.theopus.entity.schedule.enums.LessonOrder;
import com.theopus.parser.obj.*;

public class Parser {

    public static FileSheet<Group> groupDefaultPatternsParser() {
        return FileSheet.<Group>create()
                .delimiter(Patterns.Sheet.SHEET_DELIMITER)
                .build()
                .child(GroupSheet.<Group>createGroupSheet()
                        .deafultTableBound()
                        .defaultPatterns()
                        .anchorPattern(Patterns.Sheet.EXACT_GROUP_PATTERN)
                        .table(new SimpleTable().defaultPatternsMap())
                        .build()
                        .validator(new Validator())
                        .child(DaySheet.<Group>create()
                                .defaultPatterns()
                                .build()
                                .child(LessonLines.createGroupLine()
                                        .defaultPatterns()
                                        .orderPatterns(
                                                new ImmutableMap.Builder<String, LessonOrder>()
                                                .put("--\"--", LessonOrder.NONE)
                                                .put("9:00", LessonOrder.FIRST)
                                                .put("10:30", LessonOrder.SECOND)
                                                .put("12:20", LessonOrder.THIRD)
                                                .put("13:50", LessonOrder.FORUTH)
                                                .put("15:20", LessonOrder.FIFTH)
                                                .put("16:50", LessonOrder.SIXTH)
                                                .put("18:20", LessonOrder.SEVENTH)
                                                .build()
                                        )
                                        .build()
                                        .child(RoomDateBrackets.create()
                                                .defaultPatterns()
                                                .build()
                                        )
                                )
                        )
                );
    }
}
