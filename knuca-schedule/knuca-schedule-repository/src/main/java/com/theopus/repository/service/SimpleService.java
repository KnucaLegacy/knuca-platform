package com.theopus.repository.service;

import com.theopus.entity.schedule.Teacher;

import java.util.Collection;

public interface SimpleService<T> extends CacheableService {
    T save(T object);

    T findByName(String name);

    Long count();

    Collection<T> getAll();

    void delete(T object);

    T get(Long id);
}
