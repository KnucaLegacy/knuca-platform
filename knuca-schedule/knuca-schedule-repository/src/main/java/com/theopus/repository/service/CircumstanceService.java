package com.theopus.repository.service;

import com.theopus.entity.schedule.Circumstance;

import java.util.Set;

public interface CircumstanceService extends CacheableService {

    Set<Circumstance> saveAll(Set<Circumstance> circumstances);

    Circumstance save(Circumstance circumstance);
}
