package com.theopus.repository.service;

import com.theopus.entity.schedule.Course;
import com.theopus.entity.schedule.Subject;
import com.theopus.entity.schedule.Teacher;

import java.util.Collection;
import java.util.List;

public interface CourseService extends CacheableService {

    Course save(Course subject);

    Long count();

    Collection<Course> getAll();

    List<Course> withSubject(Subject subject);

    void deleteWithSubject(Subject subject);

    void delete(Course course);
}
