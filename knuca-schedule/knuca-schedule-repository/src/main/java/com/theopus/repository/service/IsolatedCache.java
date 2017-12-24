package com.theopus.repository.service;

import com.theopus.entity.schedule.Circumstance;
import com.theopus.entity.schedule.Curriculum;

/**
 * Created by Oleksandr_Tkachov on 24.12.2017.
 */
public interface IsolatedCache<Entity> extends CacheableService {

    Entity findAndSave(Entity entity);
}
