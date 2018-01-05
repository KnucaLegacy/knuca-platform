package com.theopus.repository.service;

import com.theopus.entity.schedule.Course;
import com.theopus.entity.schedule.Curriculum;
import com.theopus.entity.schedule.Group;
import com.theopus.entity.schedule.Teacher;
import com.theopus.entity.schedule.enums.LessonOrder;

import java.time.LocalDate;
import java.util.List;

public interface CurriculumService extends CacheableService {
    Curriculum save(Curriculum curriculum);

    List<Curriculum> saveAll(List<Curriculum> curriculumList);

    void delete(Curriculum curriculum);

    List<Curriculum> getByGroup(LocalDate date, Group group);

    List<Curriculum> getWithCourseAtDate(LocalDate date, Course course, LessonOrder order);

    List<Curriculum> getByTeacher(LocalDate date, Teacher teacher);
}
