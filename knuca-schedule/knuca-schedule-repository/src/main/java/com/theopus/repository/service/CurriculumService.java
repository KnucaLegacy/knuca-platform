package com.theopus.repository.service;

import com.theopus.entity.schedule.Curriculum;

import java.util.List;

public interface CurriculumService extends CacheableService {
    Curriculum save(Curriculum curriculum);

    List<Curriculum> saveAll(List<Curriculum> curriculumList);

    void delete(Curriculum curriculum);
}
