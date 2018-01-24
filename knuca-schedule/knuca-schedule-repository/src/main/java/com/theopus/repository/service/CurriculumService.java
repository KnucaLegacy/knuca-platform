package com.theopus.repository.service;

import com.theopus.entity.schedule.*;
import com.theopus.entity.schedule.enums.LessonOrder;

import java.time.LocalDate;
import java.util.List;

public interface CurriculumService extends CacheableService {
    Curriculum save(Curriculum curriculum);

    List<Curriculum> saveAll(List<Curriculum> curriculumList);

    void delete(Curriculum curriculum);

    List<Curriculum> getAtDayByGroup(LocalDate date, Group group);

    List<Curriculum> getWithCourseAtDate(LocalDate date, Course course, LessonOrder order);

    List<Curriculum> getAtDayByTeacher(LocalDate date, Teacher teacher);

    List<Curriculum> getAtDayByRoom(LocalDate date, Room room);
}
