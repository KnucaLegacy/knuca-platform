package com.theopus.repository.service;

import com.theopus.entity.schedule.Course;
import com.theopus.entity.schedule.Teacher;

import java.util.Collection;
import java.util.List;

public interface CourseService extends CacheableService {

    Course save(Course subject);

    Long size();

    Collection<Course> getAll();

    Course unenrollTeacher(Course course,Teacher teacher);

    List<Course> unenrollTeacher(Teacher teacher);
}
