package com.theopus.repository.service;

/**
 * Created by Oleksandr_Tkachov on 24.12.2017.
 */
public interface IsolatedCache<T> extends CacheableService {

    T findAndSave(T entity);
}
