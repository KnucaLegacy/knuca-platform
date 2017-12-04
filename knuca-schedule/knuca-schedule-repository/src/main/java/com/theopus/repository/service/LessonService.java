package com.theopus.repository.service;

import com.theopus.entity.schedule.Lesson;

import java.util.Collection;

public interface LessonService {

    Lesson save(Lesson subject);

    Long size();

    Collection<Lesson> getAll();
}
