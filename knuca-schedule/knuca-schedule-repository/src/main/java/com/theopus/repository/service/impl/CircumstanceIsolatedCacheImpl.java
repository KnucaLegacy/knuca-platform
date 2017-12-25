package com.theopus.repository.service.impl;

import com.theopus.entity.schedule.Circumstance;
import com.theopus.repository.jparepo.CircumstanceRepository;
import com.theopus.repository.service.CircumstanceIsolatedCache;
import com.theopus.repository.service.CurriculumIsolatedCache;
import com.theopus.repository.specification.CircumstanceSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;

import java.util.Objects;


/**
 * Created by Oleksandr_Tkachov on 24.12.2017.
 */
public class CircumstanceIsolatedCacheImpl implements CircumstanceIsolatedCache {

    private static final Logger LOG = LoggerFactory.getLogger(CircumstanceIsolatedCacheImpl.class);

    private CircumstanceRepository repository;
    private CurriculumIsolatedCache curriculumIsolatedCache;

    public CircumstanceIsolatedCacheImpl(CircumstanceRepository repository, CurriculumIsolatedCache curriculumIsolatedCache) {
        this.repository = repository;
        this.curriculumIsolatedCache = curriculumIsolatedCache;
    }

    @CacheEvict(value = "circumstance", allEntries = true)
    @Override
    public void flush() {
        curriculumIsolatedCache.flush();
        LOG.info("Cleared 'circumstance' cache.");
    }

    @Override
    public Circumstance findAndSave(Circumstance circumstance) {
        System.out.println("in cache");
        System.out.println(circumstance.getCurriculum());
        Circumstance saved = (Circumstance) repository.findOne(CircumstanceSpecification.sameCircumstance(circumstance));
        if (!Objects.isNull(saved)) {
            return saved;
        }
        return repository.saveAndFlush(circumstance);
    }
}
