package com.theopus.repository.service.impl;

import com.theopus.entity.schedule.Circumstance;
import com.theopus.entity.schedule.Room;
import com.theopus.repository.jparepo.CircumstanceRepository;
import com.theopus.repository.service.CircumstanceService;
import com.theopus.repository.service.RoomService;
import com.theopus.repository.service.SimpleService;
import com.theopus.repository.specification.CircumstanceSpecification;
import org.springframework.cache.annotation.Cacheable;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class AppendableCircumstanceService implements CircumstanceService {


    private CircumstanceRepository circumstanceRepository;
    private RoomService roomService;

    public AppendableCircumstanceService(CircumstanceRepository circumstanceRepository, RoomService roomService) {
        this.circumstanceRepository = circumstanceRepository;
        this.roomService = roomService;
    }

    @Override
    public Set<Circumstance> saveAll(Set<Circumstance> circumstances){
        return circumstances.stream()
                .map(c-> {
                    Circumstance saved = this.safeSave(c);
                    saved.getDates().addAll(c.getDates());
                    return saved;
                })
                .collect(Collectors.toSet());
    }

    @Cacheable
    public Circumstance safeSave(Circumstance circumstance){
        Circumstance saved = (Circumstance) circumstanceRepository.findOne(CircumstanceSpecification.sameCircumstance(circumstance));
        if (!Objects.isNull(saved)){
            return saved;
        }

        circumstance.setRoom(roomService.save(circumstance.getRoom()));
        return circumstanceRepository.save(circumstance);
    }


}
