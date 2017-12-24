package com.theopus.repository.service.impl;

import com.theopus.entity.schedule.Room;
import com.theopus.repository.jparepo.RoomRepository;
import com.theopus.repository.service.RoomService;
import com.theopus.repository.specification.RoomSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.Collection;

public class CacheableRoomService implements RoomService {

    private static final Logger LOG = LoggerFactory.getLogger(CacheableRoomService.class);

    private RoomRepository repository;

    public CacheableRoomService(RoomRepository repository) {
        this.repository = repository;
    }

    @Cacheable("rooms")
    @Override
    public Room save(Room room) {
        Room saved = findByName(room.getName());
        if (saved != null) {
            return saved;
        }
        return repository.save(room);
    }

    @Cacheable("rooms")
    @Override
    public Room findByName(String name) {
        return (Room) repository.findOne(RoomSpecification.getByName(name));
    }

    @Override
    public Long count() {
        return repository.count();
    }

    @Override
    public Collection<Room> getAll() {
        return repository.findAll();
    }

    @Override
    public void delete(Room object) {

    }

    @CacheEvict(value = "rooms", allEntries = true)
    @Override
    public void flush() {
        LOG.info("Cleared 'rooms' cache.");
    }
}
