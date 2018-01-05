package com.theopus.repository.service;

import com.theopus.entity.schedule.Group;
import com.theopus.entity.schedule.Lesson;
import com.theopus.entity.schedule.Teacher;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface LessonService {

    List<Lesson> getByGroup(LocalDate date, Group group);

    Map<LocalDate, List<Lesson>> getByGroup(Set<LocalDate> date, Group group);

    List<Lesson> getByTeacher(LocalDate date, Teacher teacher);

    Map<LocalDate, List<Lesson>> getByTeacher(Set<LocalDate> date, Teacher teacher);



}