package com.theopus.parser.api;

import com.theopus.entity.schedule.Lesson;

import java.util.List;


//ToDo make implementations of this interface (File parser, String parser, Pdf parser, Directory parser, etc.)
public interface ParserKnuca {

    List<Lesson> parseAll();

    //trivial
//    List<Lesson> parseAllDistinct();
//    List<Lesson> parseByGroup(String groupName);
//    List<Lesson> parseByTeacher(String teacher);
}
