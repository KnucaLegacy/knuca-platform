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

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
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

    /**
     * Methods whitch delete dates dates after passed date(including it) and returns curriculums with dates which was deleted.
     * @param group     - anchor on which based deletion.
     * @param localDate - from what date delete (including).
     * @return List of deleted difference.
     */
    @Override
    public List<Circumstance> deleteWithGroupAfter(Group group, LocalDate localDate) {
        List<Circumstance> circumstances = withGroup(group);
        return deleteAfter(circumstances, localDate);
    }

    @Override
    public void delete(Circumstance circumstance) {
        circumstanceRepository.delete(circumstance);
    }

    @Override
    public void flush() {
        roomService.flush();
        circumstanceIsolatedCache.flush();
    }

    private Set<LocalDate> equalAndAfter(Set<LocalDate> dates, LocalDate localDate) {
        return dates.stream()
                .filter(ld -> ld.isAfter(localDate) || ld.isEqual(localDate))
                .collect(Collectors.toSet());
    }

    private List<Circumstance> deleteAfter(List<Circumstance> circumstances, LocalDate localDate) {
        Map<Long, Set<LocalDate>> collect = circumstances.stream().collect(Collectors.toMap(Circumstance::getId, Circumstance::getDates));
        collect.entrySet()
                .forEach(longSetEntry -> longSetEntry.setValue(equalAndAfter(longSetEntry.getValue(), localDate)));

        List<Circumstance> afterDelete = circumstances.stream().peek(circumstance -> circumstance.setDates(circumstance.getDates()
                .stream().filter(localDate1 -> localDate1.isBefore(localDate)).collect(Collectors.toSet())))
                .map(circumstanceRepository::save)
                .collect(Collectors.toList());

        afterDelete.forEach(circumstance -> circumstance.setDates(collect.get(circumstance.getId())));
        return afterDelete;
    }
}
