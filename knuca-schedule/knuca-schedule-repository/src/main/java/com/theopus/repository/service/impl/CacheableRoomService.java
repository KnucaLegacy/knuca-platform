package com.theopus.repository.service.impl;

import com.theopus.entity.schedule.Room;
import com.theopus.repository.jparepo.RoomRepository;
import com.theopus.repository.service.SimpleService;
import com.theopus.repository.specification.RoomSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

import java.util.Collection;

public class CacheableRoomService<T> implements SimpleService<Room> {

    private RoomRepository repository;

    @Autowired
    public CacheableRoomService(RoomRepository repository) {
        this.repository = repository;
    }

    @Cacheable
    @Override
    public Room save(Room room) {
        Room saved = findByName(room.getName());
        if (saved != null) {
            return saved;
        }
        return repository.save(room);
    }

    @Cacheable
    @Override
    public Room save(String name) {
        Room room = findByName(name);
        if (room != null) {
            return room;
        }
        room = new Room();
        room.setName(name);
        return repository.save(room);
    }

    @Cacheable
    @Override
    public Room findByName(String name) {
        return (Room) repository.findOne(RoomSpecification.getByName(name));
    }

    @Override
    public Long size() {
        return repository.count();
    }

    @Override
    public Collection<Room> getAll() {
        return repository.findAll();
    }
}
