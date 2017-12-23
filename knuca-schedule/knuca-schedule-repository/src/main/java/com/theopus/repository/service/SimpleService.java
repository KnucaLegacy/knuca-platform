package com.theopus.repository.service;

import java.util.Collection;

public interface SimpleService<T> extends CacheableService {
    T save(T subject);

    T findByName(String name);

    Long size();

    Collection<T> getAll();
}
