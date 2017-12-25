package com.theopus.repository.service.impl;

import com.theopus.entity.schedule.Curriculum;
import com.theopus.repository.jparepo.CurriculumRepository;
import com.theopus.repository.service.CurriculumIsolatedCache;
import com.theopus.repository.specification.CurriculumSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;

/**
 * Created by Oleksandr_Tkachov on 24.12.2017.
 */
public class CurriculumIsolatedCacheImpl implements CurriculumIsolatedCache {

    private static final Logger LOG = LoggerFactory.getLogger(CurriculumIsolatedCacheImpl.class);

    private CurriculumRepository repository;

    public CurriculumIsolatedCacheImpl(CurriculumRepository repository) {
        this.repository = repository;
    }

    @Override
    public Curriculum findAndSave(Curriculum curriculum) {
        Curriculum dbCuriculum = (Curriculum) repository.findOne(CurriculumSpecification.sameCurriculum(curriculum));
        if (dbCuriculum != null) {
            return dbCuriculum;
        } else {
            return repository.save(curriculum);
        }
    }

    @CacheEvict(value = "curriculum", allEntries = true)
    @Override
    public void flush() {
        LOG.info("Cleared 'curriculum' cache.");
    }
}
