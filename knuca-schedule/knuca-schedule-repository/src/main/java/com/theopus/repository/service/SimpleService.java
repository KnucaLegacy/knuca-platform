package com.theopus.repository.service;

import java.util.Collection;

public interface SimpleService<T> extends CacheableService {
    T save(T object);

    T findByName(String name);

    Long count();

    Collection<T> getAll();

    void delete(T object);
}
