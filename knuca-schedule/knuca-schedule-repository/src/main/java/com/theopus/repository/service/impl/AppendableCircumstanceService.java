package com.theopus.repository.service.impl;

import com.theopus.entity.schedule.Circumstance;
import com.theopus.entity.schedule.Group;
import com.theopus.repository.jparepo.CircumstanceRepository;
import com.theopus.repository.service.CircumstanceIsolatedCache;
import com.theopus.repository.service.CircumstanceService;
import com.theopus.repository.service.RoomService;
import com.theopus.repository.specification.CircumstanceSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.List;
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
        Circumstance andSave = circumstanceIsolatedCache.findAndSave(circumstance);
        andSave.getDates().addAll(dates);
        return circumstanceRepository.save(andSave);
    }

    @Override
    public List<Circumstance> withGroup(Group group) {
        return (List<Circumstance>) circumstanceRepository.findAll(CircumstanceSpecification.withGroup(group));
    }

    @Override
    public Long deleteWithGroupAfter(Group group, LocalDate localDate) {
        List<Circumstance> circumstances = withGroup(group);

        return circumstances.stream().peek(circumstance -> circumstance.setDates(circumstance.getDates()
                .stream().filter(localDate1 -> localDate1.isBefore(localDate)).peek(System.out::println).collect(Collectors.toSet())))
                .map(circumstanceRepository::save)
                .count();
    }

    @Override
    public void flush() {
        roomService.flush();
        circumstanceIsolatedCache.flush();
    }
}
