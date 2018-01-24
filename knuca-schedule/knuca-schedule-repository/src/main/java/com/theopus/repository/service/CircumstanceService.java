package com.theopus.repository.service;

import com.theopus.entity.schedule.Circumstance;
import com.theopus.entity.schedule.Group;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface CircumstanceService extends CacheableService {

    Set<Circumstance> saveAll(Set<Circumstance> circumstances);

    Circumstance save(Circumstance circumstance);

    List<Circumstance> withGroup(Group group);

    Long deleteWithGroupAfter(Group group, LocalDate localDate);

    void delete(Circumstance circumstance);
}
