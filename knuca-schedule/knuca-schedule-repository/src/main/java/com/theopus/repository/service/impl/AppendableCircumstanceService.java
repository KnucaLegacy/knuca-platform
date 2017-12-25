package com.theopus.repository.service.impl;

import com.theopus.entity.schedule.Circumstance;
import com.theopus.repository.jparepo.CircumstanceRepository;
import com.theopus.repository.service.CircumstanceIsolatedCache;
import com.theopus.repository.service.CircumstanceService;
import com.theopus.repository.service.RoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

public class AppendableCircumstanceService implements CircumstanceService {

    private static final Logger LOG = LoggerFactory.getLogger(AppendableCircumstanceService.class);

    private final CircumstanceRepository circumstanceRepository;
    private final RoomService roomService;
    private final CircumstanceIsolatedCache circumstanceIsolatedCache;

    public AppendableCircumstanceService(CircumstanceRepository circumstanceRepository, RoomService roomService,
                                         CircumstanceIsolatedCache circumstanceIsolatedCache) {
        this.circumstanceRepository = circumstanceRepository;
        this.roomService = roomService;
        this.circumstanceIsolatedCache = circumstanceIsolatedCache;
    }

    @Override
    public Set<Circumstance> saveAll(Set<Circumstance> circumstances) {
        return circumstances.stream()
                .map(this::save)
                .collect(Collectors.toSet());
    }

    @Override
    public Circumstance save(Circumstance circumstance) {
        circumstance.setRoom(roomService.save(circumstance.getRoom()));
        Set<LocalDate> dates = circumstance.getDates();
        System.out.println("in app");
        System.out.println(circumstance.getCurriculum());
        Circumstance andSave = circumstanceIsolatedCache.findAndSave(circumstance);
        andSave.getDates().addAll(dates);
        return circumstanceRepository.save(andSave);
    }

    @Override
    public void flush() {
        roomService.flush();
        circumstanceIsolatedCache.flush();
    }
}
