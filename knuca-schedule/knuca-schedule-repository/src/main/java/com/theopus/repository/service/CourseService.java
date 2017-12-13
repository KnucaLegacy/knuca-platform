package com.theopus.repository.service;

import com.theopus.entity.schedule.Course;

import java.util.Collection;

public interface CourseService {

    Course save(Course subject);

    Long size();

    Collection<Course> getAll();
}
