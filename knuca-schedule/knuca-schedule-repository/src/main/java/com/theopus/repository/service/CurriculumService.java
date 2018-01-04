package com.theopus.repository.service;

import com.theopus.entity.schedule.Curriculum;
import com.theopus.entity.schedule.Group;

import java.time.LocalDate;
import java.util.List;

public interface CurriculumService extends CacheableService {
    Curriculum save(Curriculum curriculum);

    List<Curriculum> saveAll(List<Curriculum> curriculumList);

    void delete(Curriculum curriculum);

    List<Curriculum> getByGroup(LocalDate date, Group group);
}
