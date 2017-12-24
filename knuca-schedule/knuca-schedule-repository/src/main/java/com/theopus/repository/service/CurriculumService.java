package com.theopus.repository.service;

import com.theopus.entity.schedule.Curriculum;

import java.util.List;

public interface CurriculumService {
    Curriculum save(Curriculum curriculum);

    List<Curriculum> saveAll(List<Curriculum> curriculumList);
}
