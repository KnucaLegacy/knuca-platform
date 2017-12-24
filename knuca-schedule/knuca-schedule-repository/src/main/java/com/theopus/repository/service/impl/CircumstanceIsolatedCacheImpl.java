package com.theopus.repository.service.impl;

import com.theopus.entity.schedule.Circumstance;
import com.theopus.repository.jparepo.CircumstanceRepository;
import com.theopus.repository.service.CircumstanceIsolatedCache;
import com.theopus.repository.specification.CircumstanceSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.Objects;


/**
 * Created by Oleksandr_Tkachov on 24.12.2017.
 */
public class CircumstanceIsolatedCacheImpl implements CircumstanceIsolatedCache {

    private static final Logger LOG = LoggerFactory.getLogger(CircumstanceIsolatedCacheImpl.class);

    private CircumstanceRepository repository;

    public CircumstanceIsolatedCacheImpl(CircumstanceRepository repository) {
        this.repository = repository;
    }

    @CacheEvict(value = "circumstance", allEntries = true)
    @Override
    public void flush() {
        LOG.info("Cleared 'circumstance' cache.");
    }

    @Cacheable("circumstance")
    @Override
    public Circumstance findAndSave(Circumstance circumstance) {
        Circumstance saved = (Circumstance) repository.findOne(CircumstanceSpecification.sameCircumstance(circumstance));
        if (!Objects.isNull(saved)){
            return saved;
        }
        return repository.save(circumstance);
    }
}
