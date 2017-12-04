package com.theopus.repository.service;

import java.util.Collection;

public interface SimpleService<T> {
    T save(T subject);

    T save(String name);

    T findByName(String name);

    Long size();

    Collection<T> getAll();
}
