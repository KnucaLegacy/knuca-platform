package com.theopus.repository.service;

import com.theopus.entity.schedule.Group;
import com.theopus.entity.schedule.Lesson;
import com.theopus.entity.schedule.Teacher;

import java.time.LocalDate;
import java.util.List;

public interface LessonService {

    List<Lesson> getByGroup(LocalDate date, Group group);

    List<Lesson> getByTeacher(LocalDate date, Teacher teacher);
}