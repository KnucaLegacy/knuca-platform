package com.theopus.repository.service;

import com.theopus.entity.schedule.Circumstance;

import java.util.Set;

public interface CircumstanceService{

    Set<Circumstance> saveAll(Set<Circumstance> circumstances);

    Circumstance save(Circumstance circumstance);
}
