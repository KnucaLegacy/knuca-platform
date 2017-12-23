package com.theopus.repository.service;

import com.theopus.entity.schedule.Course;

import java.util.Collection;

public interface CourseService extends CacheableService{

    Course save(Course subject);

    Long size();

    Collection<Course> getAll();
}
