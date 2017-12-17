package com.theopus.parser.api;

import com.theopus.entity.schedule.Curriculum;
import com.theopus.parser.obj.line.LessonLine;
import com.theopus.parser.obj.roomdate.RoomDateBrackets;
import com.theopus.parser.obj.sheets.Sheet;

import java.util.List;


//ToDo make implementations of this interface (File parser, String parser, Pdf parser, Directory parser, etc.)
public interface ParserKnuca {

    ParserKnuca sheet(Sheet sheet);
    ParserKnuca lessonLine(LessonLine lessonLine);
    ParserKnuca roomDateBracket(RoomDateBrackets roomDateBrackets);

    List<Curriculum> parseCurriculums();
}
